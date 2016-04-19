package server;

import java.net.*;
import java.util.Vector;
import iostream.IOStream;

public class MultiTalkServer {
	static Vector<IOStream> allStream = new Vector<IOStream>();// �������пͻ�����

	public static void main(String args[]) {
		try {
			ServerSocket serverSocket = new ServerSocket(4700); // ����һ��ServerSocket�ڶ˿�4700�����ͻ�����

			while (true) {// ѭ������
				Socket socket = serverSocket.accept();
				allStream.addElement(new IOStream(socket));
				new ServerThread().start();
			}
		} catch (Exception e) {
			System.out.println("Could not listen on port:4700.");// ������ӡ������Ϣ
		}
	}
}
