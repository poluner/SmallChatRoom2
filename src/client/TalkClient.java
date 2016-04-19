package client;

import java.net.*;

public class TalkClient {// 客户端将发送和接收分两个线程，使得发送接收不互相干扰
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 4700);
			new SendThread(socket).start();//发送线程
			new ReceiveThread(socket).start();//接收线程
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}