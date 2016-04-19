package client;

import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream;

	public SendThread(Socket socket) throws Exception {
		ioStream = new IOStream(socket);
	}

	public void run() {// ·¢ËÍÏß³Ì
		try {
			while (true) {
				int yourId = ioStream.sin.nextInt();
				String sendLine = ioStream.sin.next();
				ioStream.os.write(yourId);
				ioStream.os.write(sendLine.getBytes());
				ioStream.os.write(255);
				ioStream.os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
