package client;

import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream;

	public SendThread(Socket socket) {
		ioStream = new IOStream(socket);
	}

	public void run() {// 发送线程
		while (true) {
			String yourId = ioStream.sin.nextLine();// 这里只能用nextLine输入，否则下面一行的nextLine会读入回车
			String message = ioStream.sin.nextLine();
			ioStream.os.println(yourId);
			ioStream.os.println(message);
			ioStream.os.flush();
		}
	}
}
