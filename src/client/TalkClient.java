package client;

import java.net.*;
import iostream.*;

public class TalkClient {
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 4700);
			IOStream ioStream = new IOStream(socket);
			while (true) {// 由于发送在接收前面，所以每次要自己发送之后才能接收到消息
				// 发送
				String sendLine = ioStream.sin.nextLine();// 如果向0号客户发"hello"，那么就输入"0hello"
				ioStream.os.println(sendLine);
				ioStream.os.flush();

				// 接收
				String line = ioStream.is.readLine();
				int yourId = Integer.parseInt(line.substring(0, 1));
				String message = line.substring(1, line.length());
				System.out.println("client " + yourId + " say:" + message);
			}
		} catch (Exception e) {
			System.out.println("ErrorClient:" + e);
		}
	}
}