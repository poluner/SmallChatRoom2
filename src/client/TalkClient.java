package client;

import java.net.*;

public class TalkClient {// �ͻ��˽����ͺͽ��շ������̣߳�ʹ�÷��ͽ��ղ��������
	public static void main(String args[]) {
		try {
			WinClient winClient = new WinClient();

			Socket socket = new Socket("127.0.0.1", 4700);
			SendThread sendThread = new SendThread(socket, winClient);// �����߳�
			ReceiveThread receiveThread = new ReceiveThread(socket, winClient);// �����߳�
			sendThread.start();
			receiveThread.start();
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}