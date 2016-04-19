package client;

import java.net.Socket;
import iostream.IOStream;

public class ReceiveThread extends Thread {
	IOStream ioStream;

	public ReceiveThread(Socket socket) throws Exception {
		ioStream = new IOStream(socket);
	}

	public void run() {// 接收线程
		try {
			while (true) {
				int yourId = ioStream.is.read();
				String message = ioStream.getMessage();
				System.out.println("client " + yourId + " say:" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
