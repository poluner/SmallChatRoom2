package client;

import java.net.Socket;
import iostream.IOStream;

public class ReceiveThread extends Thread {
	IOStream ioStream;
	WinClient winClient;

	public ReceiveThread(Socket socket, WinClient winClient) throws Exception {
		ioStream = new IOStream(socket);
		this.winClient = winClient;
	}

	public void run() {// 接收线程
		try {
			winClient.setTitle("Hi client " + ioStream.is.read());// 设置客户自己的ID

			while (true) {
				int yourId = ioStream.is.read();
				int isFile = ioStream.is.read();

				String historyMessage = winClient.receiveText.getText();
				if (historyMessage.length() != 0)
					historyMessage += "\n";// 如果没有历史纪录就不加换行符

				if (isFile == 0) {
					winClient.receiveText.setText(historyMessage +"client "+ yourId + " say: " + ioStream.messageFromStream());// 保留历史纪录
				} else {
					String fileName = ioStream.fileFromStream();
					winClient.receiveText.setText(historyMessage + "received a file from " + yourId + " it has been saved as " + fileName);
					showIfImage(fileName);// 如果是图片就会展示出来
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void showIfImage(String fileName) {
		String fileType = fileName.substring(fileName.indexOf('.'), fileName.length());
		String imageType[] = { ".jpg", ".jpeg", ".png", ".bmp" };
		for (int i = 0; i < 4; i++) {
			if (fileType.equalsIgnoreCase(imageType[i])) {
				winClient.showImage(fileName);
				break;
			}
		}
	}
}
