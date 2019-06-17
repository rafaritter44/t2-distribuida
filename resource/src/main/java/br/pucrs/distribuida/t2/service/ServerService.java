package br.pucrs.distribuida.t2.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;

@Singleton
public class ServerService extends AbstractRSocket {
	
	private static final String IP = "172.18.0.22";
	private static final int PORT = 10_000;
	
	private final FileService fileService;
	
	@Inject
	public ServerService(FileService fileService) {
		this.fileService = fileService;
	}
	
	public void start() {
		RSocketFactory.receive()
				.acceptor((setupPayload, reactiveSocket) -> Mono.just(this))
				.transport(TcpServerTransport.create(IP, PORT))
				.start()
				.subscribe();
	}
	
	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		System.out.println(payload.getDataUtf8());
		fileService.writeToFile(payload.getDataUtf8());
		return Mono.empty();
	}
	
}
