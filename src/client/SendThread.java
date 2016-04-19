package client;

import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream;

	public SendThread(Socket socket) throws Exception {
		ioStream = new IOStream(socket);
	}

	public void run() {// 发送线程
		try {
			while (true) {
				int yourId = ioStream.sin.nextInt();//目标客户
				String pathName = ioStream.sin.next();//文件名
				ioStream.os.write(yourId);
				ioStream.fileToStream(pathName);
				ioStream.addEOS();
				ioStream.os.flush();
				System.out.println("file sended!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
