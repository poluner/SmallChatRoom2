package client;

import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream ;

	public SendThread(Socket socket) {
		ioStream = new IOStream(socket);
	}

	public void run() {// ·¢ËÍÏß³Ì
		while (true) {
			String sendLine = ioStream.sin.nextLine();
			ioStream.os.println(sendLine);
			ioStream.os.flush();
		}
	}
}
