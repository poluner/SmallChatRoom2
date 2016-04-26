package client;

import java.net.*;

public class TalkClient {// 客户端将发送和接收分两个线程，使得发送接收不互相干扰
	public static void main(String args[]) {
		try {
			WinClient winClient = new WinClient();

			Socket socket = new Socket("127.0.0.1", 4700);
			SendThread sendThread = new SendThread(socket, winClient);// 发送线程
			ReceiveThread receiveThread = new ReceiveThread(socket, winClient);// 接收线程
			sendThread.start();
			receiveThread.start();
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}