package client;

import java.net.*;

public class TalkClient {// �ͻ��˽����ͺͽ��շ������̣߳�ʹ�÷��ͽ��ղ��������
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 4700);
			new SendThread(socket).start();//�����߳�
			new ReceiveThread(socket).start();//�����߳�
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}