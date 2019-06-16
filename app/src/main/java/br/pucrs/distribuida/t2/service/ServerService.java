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
	private final BullyService bullyService;
	
	@Inject
	public ServerService(CoordinatorService coordinatorService, NodeService nodeService, BullyService bullyService) {
		this.coordinatorService = coordinatorService;
		this.nodeService = nodeService;
		this.bullyService = bullyService;
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
		System.out.println(String.format("Received '%s' from %s", payload.getDataUtf8(), payload.getMetadataUtf8()));
		if (payload.getDataUtf8().equals(BullyService.I_AM_COORDINATOR)) {
			nodeService.setCoordinator(nodeService.getSender(payload));
			return Mono.empty();
		}
		if (payload.getDataUtf8().equals(BullyService.ELECTION)) {
			bullyService.answerElection(payload);
			return Mono.empty();
		}
		if (bullyService.calledElection() && bullyService.okFromBiggerNode(payload)) {
			bullyService.didNotWinElection();
			return Mono.empty();
		}
		if (coordinatorService.isRunning() && coordinatorService.handle(payload)) {
			return Mono.empty();
		}
		return Mono.empty();
	}
	
}
