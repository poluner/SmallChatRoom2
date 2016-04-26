package client;

import java.awt.event.KeyEvent;
import java.net.Socket;
import iostream.IOStream;

public class SendThread extends Thread {
	IOStream ioStream;
	WinClient winClient;

	public SendThread(Socket socket, WinClient winClient) throws Exception {
		ioStream = new IOStream(socket);
		this.winClient = winClient;
	}

	public void run() {// �����߳�
		try {
			while (true) {// �����߳�
				String historyMessage = winClient.receiveText.getText();
				if (historyMessage.length() != 0)
					historyMessage += '\n';

				if (winClient.isFile == false) {
					if (winClient.keyEvent == null || winClient.keyEvent.getKeyCode() != KeyEvent.VK_ENTER
							|| winClient.sendText.getText().length() == 0) {
						sleep(10);// ���Ϳ�δ���»س����߷�����ϢΪ�ն�Ҫsleep
						continue;// Ҫ��ȥ�ж���һ�������ļ������ļ�
					}
					if (winClient.receiverIdText.getText().length() == 0) {// ���û�����������ID�ž����²���
						winClient.keyEvent = null;
						continue;// ���û������Ŀ��ͻ�ID�����²���
					}

					winClient.receiveText
							.setText(historyMessage + winClient.myId + ": " + winClient.sendText.getText());

					ioStream.os.write(Integer.parseInt(winClient.receiverIdText.getText()));// Ŀ��ͻ�
					ioStream.os.write(0);// ����������0��ʾ
					ioStream.os.write(winClient.sendText.getText().getBytes("GBK"));// ��ֹ��������

					winClient.sendText.setText("");// ����֮��ͽ����������
					winClient.keyEvent = null;// ������Ϣ��ͽ��س��¼�����null

				} else {
					if (winClient.receiverIdText.getText().length() == 0)
						continue;// ���û�����������ID�����²���
					ioStream.os.write(Integer.parseInt(winClient.receiverIdText.getText()));// Ŀ��ͻ�
					ioStream.os.write(1);// �����ļ���1��ʾ

					ioStream.fileToStream(winClient.pathName);

					winClient.isFile = false;// ����֮��ͽ�isFile��ֵfalse

					winClient.receiveText.setText(historyMessage + winClient.myId + ": File Send Successful!");
				}

				ioStream.addEOS();
				ioStream.os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
