package client;

import java.net.*;
import iostream.*;

public class TalkClient {
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 4700);
			IOStream ioStream = new IOStream(socket);
			while (true) {// ���ڷ����ڽ���ǰ�棬����ÿ��Ҫ�Լ�����֮����ܽ��յ���Ϣ
				// ����
				String sendLine = ioStream.sin.nextLine();// �����0�ſͻ���"hello"����ô������"0hello"
				ioStream.os.println(sendLine);
				ioStream.os.flush();

				// ����
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