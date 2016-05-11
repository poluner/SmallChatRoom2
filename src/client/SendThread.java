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

	public void run() {// 发送线程
		try {
			while (true) {// 发送线程
				String historyMessage = winClient.receiveText.getText();
				if (historyMessage.length() != 0)
					historyMessage += '\n';

				if (winClient.isFile == false) {
					if (winClient.keyEvent == null || winClient.keyEvent.getKeyCode() != KeyEvent.VK_ENTER
							|| winClient.sendText.getText().length() == 0) {
						sleep(10);// 发送框未按下回车或者发送消息为空都要sleep
						continue;// 要回去判断下一个发送文件还是文件
					}
					if (winClient.receiverIdText.getText().length() == 0) {// 如果没有输入接收者ID号就重新操作
						winClient.keyEvent = null;
						continue;// 如果没有输入目标客户ID就重新操作
					}

					winClient.receiveText
							.setText(historyMessage + winClient.myId + ": " + winClient.sendText.getText());

					ioStream.messageToStream(Integer.parseInt(winClient.receiverIdText.getText()),
							winClient.sendText.getText());// 上传文字

					winClient.sendText.setText("");// 发送之后就将发送区清空
					winClient.keyEvent = null;// 发完消息后就将回车事件赋上null

				} else {
					if (winClient.receiverIdText.getText().length() == 0)
						continue;// 如果没有输入接收者ID就重新操作

					ioStream.fileToStream(Integer.parseInt(winClient.receiverIdText.getText()), winClient.pathName);

					winClient.isFile = false;// 发送之后就将isFile赋值false
					winClient.receiveText.setText(historyMessage + winClient.myId + ": File Send Successful!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
