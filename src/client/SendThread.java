package client;

import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream;

	public SendThread(Socket socket) {
		ioStream = new IOStream(socket);
	}

	public void run() {// �����߳�
		while (true) {
			String yourId = ioStream.sin.nextLine();// ����ֻ����nextLine���룬��������һ�е�nextLine�����س�
			String message = ioStream.sin.nextLine();
			ioStream.os.println(yourId);
			ioStream.os.println(message);
			ioStream.os.flush();
		}
	}
}
