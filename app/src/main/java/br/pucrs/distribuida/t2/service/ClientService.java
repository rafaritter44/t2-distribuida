package br.pucrs.distribuida.t2.service;

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
		RSocketFactory.connect()
				.transport(TcpClientTransport.create(receiver.getHost(), receiver.getPort()))
				.start()
				.flatMap(rsocket -> rsocket.fireAndForget(DefaultPayload.create(message, getIpAndPort(nodeService.getSelf()))))
				.subscribe();
	}
	
	private String getIpAndPort(Node node) {
		return String.format("%s:%s", node.getHost(), node.getPort());
	}
	
}
