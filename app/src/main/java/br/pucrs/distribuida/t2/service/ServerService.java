package br.pucrs.distribuida.t2.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import br.pucrs.distribuida.t2.model.Node;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;

@Singleton
public class ServerService extends AbstractRSocket {
	
	private final CoordinatorService coordinatorService;
	private final NodeService nodeService;
	
	@Inject
	public ServerService(CoordinatorService coordinatorService, NodeService nodeService) {
		this.coordinatorService = coordinatorService;
		this.nodeService = nodeService;
	}
	
	public void start() {
		Node self = nodeService.getSelf();
		RSocketFactory.receive()
				.acceptor((setupPayload, reactiveSocket) -> Mono.just(this))
				.transport(TcpServerTransport.create(self.getHost(), self.getPort()))
				.start()
				.subscribe();
	}
	
	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		System.out.println("Received: " + payload.getDataUtf8());
		if (coordinatorService.isRunning()) {
			coordinatorService.handle(payload);
		}
		return Mono.empty();
	}
	
}
