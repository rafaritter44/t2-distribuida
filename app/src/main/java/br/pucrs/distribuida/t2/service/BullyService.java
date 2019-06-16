package br.pucrs.distribuida.t2.service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import br.pucrs.distribuida.t2.model.Node;
import io.rsocket.Payload;

@Singleton
public class BullyService {
	
	public static final String ELECTION = "election";
	public static final String OK = "ok";
	public static final String I_AM_COORDINATOR = "I'm the new coordinator";
	
	private static final long ELECTION_TIME = 5L;
	
	private final NodeService nodeService;
	private final ClientService clientService;
	private final CoordinatorService coordinatorService;
	private Boolean calledElection;
	private Boolean wonElection;
	
	@Inject
	public BullyService(NodeService nodeService, ClientService clientService, CoordinatorService coordinatorService) {
		this.nodeService = nodeService;
		this.clientService = clientService;
		this.coordinatorService = coordinatorService;
		calledElection = Boolean.FALSE;
		wonElection = Boolean.FALSE;
	}
	
	public Boolean calledElection() {
		return calledElection;
	}
	
	public void callElection() {
		System.out.println("Calling election!");
		calledElection = Boolean.TRUE;
		wonElection = Boolean.TRUE;
		nodeService.biggerThanMe().forEach(node -> clientService.send(ELECTION, node));
		scheduleElectionResultsChecking();
	}
	
	private void scheduleElectionResultsChecking() {
		Executors.newSingleThreadScheduledExecutor()
				.schedule(this::checkElectionResult, ELECTION_TIME, TimeUnit.SECONDS);
	}
	
	private void checkElectionResult() {
		if (wonElection) {
			nodeService.setCoordinator(nodeService.getSelf());
			coordinatorService.start();
			nodeService.getNodes().forEach(node -> clientService.send(I_AM_COORDINATOR, node));
			wonElection = Boolean.FALSE;
		}
		calledElection = Boolean.FALSE;
	}
	
	public void answerElection(Payload payload) {
		Node sender = nodeService.getSender(payload);
		clientService.send(OK, sender);
		callElection();
	}
	
	public boolean okFromBiggerNode(Payload payload) {
		Node sender = nodeService.getSender(payload);
		return payload.getDataUtf8().equals(OK) && sender.getId() > nodeService.getSelf().getId();
	}
	
	public void didNotWinElection() {
		wonElection = Boolean.FALSE;
	}
	
}
