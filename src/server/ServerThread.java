package server;

import iostream.IOStream;

public class ServerThread extends Thread {
	// ֻ�з�������Ҫ�������ߺͽ����ߵ�io�����ͻ���ֻ���Լ���io��
	// ÿһ���̶߳��Ƕ�Ӧ�ͻ��ڷ������ϲ��õ�socket������ÿһ���̶߳��Ƿ����ߣ�
	// �ͻ��˽���Ϣ���͵��Լ��ڷ������ϲ��õ�socket��Ȼ�������������֮����Ϣȡ�����õ�Ŀ��ͻ���Id�������ߣ���
	// Ȼ����Ϣ���뵽�������ڷ������ϲ��õ�socket�У�

	IOStream myStream;
	int myId;

	public ServerThread() {
		myStream = MultiTalkServer.allStream.lastElement();
		myId = MultiTalkServer.allStream.size() - 1;
	}

	public void run() { // �������߳�
		try {
			while (true) {// ��������Զ����
				// ���Լ��ڷ������в��õ�socket��ȡ���Լ����͵���Ϣ��������Լ�Ҫ���͵�Ŀ��ͻ���Id

				int yourId = myStream.is.read();

				// ȷ��Ŀ��ͻ�
				IOStream yourStream = MultiTalkServer.allStream.elementAt(yourId);

				// ����Ϣ����Ŀ��ͻ���socket��
				yourStream.os.write(myId);
				myStream.transTo(yourStream);
				yourStream.addEOS();
				yourStream.os.flush();
			}
		} catch (Exception e) {
			System.out.println("Error:" + e);// ������ӡ������Ϣ
		}
	}
}
