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
			myStream.os.write(myId);// �������˸�֪�ͻ�ID���൱��Ϊ�ͻ�����ID
			myStream.os.flush();
			while (true) {// ��������Զ����
				// ���Լ��ڷ������в��õ�socket��ȡ���Լ����͵���Ϣ��������Լ�Ҫ���͵�Ŀ��ͻ���Id

				int yourId = myStream.is.read();// Ŀ��ͻ�
				if (yourId == myId) {// ���youId==myId�ͽ���Ϣ�ʹ�����ÿһ���ͻ�(�����Լ�)���Ӷ�ʵ��Ⱥ��
					myStream.transToAllExcept(myId);
				} else {// �����ʹ�ָ���ͻ�
					IOStream yourStream = MultiTalkServer.allStream.elementAt(yourId);
					yourStream.os.write(myId);// �ϴ��ҵ�ID
					myStream.transTo(yourStream);
				}
			}
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
}
