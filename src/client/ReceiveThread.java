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
				String line = ioStream.is.readLine();
				int yourId = Integer.parseInt(line.substring(0, 1));
				String message = line.substring(1, line.length());

				System.out.println("client " + yourId + " say:" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
