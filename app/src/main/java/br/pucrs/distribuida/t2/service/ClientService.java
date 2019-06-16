package br.pucrs.distribuida.t2.service;

import java.util.function.Consumer;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import br.pucrs.distribuida.t2.model.Node;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;

@Singleton
public class ClientService {
	
	private final NodeService nodeService;
	
	@Inject
	public ClientService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
	public void send(String message, Node receiver) {
		send(message, receiver, e -> System.out.println(String.format("Node %d is oos!", receiver.getId())));
	}
	
	public void send(String message, Node receiver, Consumer<? super Throwable> fallback) {
		System.out.println(String.format("Sent '%s' to node %d", message, receiver.getId()));
		RSocketFactory.connect()
				.transport(TcpClientTransport.create(receiver.getHost(), receiver.getPort()))
				.start()
				.flatMap(rsocket -> rsocket.fireAndForget(DefaultPayload.create(message, getIpAndPort(nodeService.getSelf()))))
				.doOnError(fallback)
				.subscribe();
	}
	
	private String getIpAndPort(Node node) {
		return String.format("%s:%s", node.getHost(), node.getPort());
	}
	
}
