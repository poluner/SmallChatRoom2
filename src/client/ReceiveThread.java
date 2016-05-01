package client;

import java.net.Socket;
import iostream.IOStream;

public class ReceiveThread extends Thread {
	IOStream ioStream;

	public ReceiveThread(Socket socket) {
		ioStream = new IOStream(socket);
	}

	public void run() {// 接收线程
		try {
			while (true) {
				int yourId = Integer.parseInt(ioStream.is.readLine());
				String message = ioStream.is.readLine();

				System.out.println("client " + yourId + " say:" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
