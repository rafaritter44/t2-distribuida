package br.pucrs.distribuida.t2.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.pucrs.distribuida.t2.model.Node;
import br.pucrs.distribuida.t2.model.Request;

public class NetworkService {
	
	private static final int PACKET_LENGTH = 1024;
	
	private final DatagramSocket client;
	private final DatagramSocket server;
	
	public NetworkService(int port) {
		try {
			client = new DatagramSocket();
			server = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void send(String message, Node receiver) {
		byte[] data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip(receiver), receiver.getPort());
		send(packet);
	}
	
	private void send(DatagramPacket packet) {
		try {
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private InetAddress ip(Node node) {
		try {
			return InetAddress.getByName(node.getHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Request receive() {
		DatagramPacket packet = new DatagramPacket(new byte[PACKET_LENGTH], PACKET_LENGTH);
		receive(packet);
		return requestFrom(packet);
	}
	
	private void receive(DatagramPacket packet) {
		try {
			server.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private Request requestFrom(DatagramPacket packet) {
		String message = new String(packet.getData());
		String host = packet.getAddress().getHostAddress();
		Integer port = packet.getPort();
		return new Request(message, host, port);
	}
	
}
