package iostream;

import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.*;

import server.MultiTalkServer;

public class IOStream {
	public DataOutputStream os;
	public DataInputStream is;
	public Scanner sin;

	public IOStream(Socket socket) throws Exception {
		os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
		sin = new Scanner(System.in);
	}

	public void fileToStream(int yourId, String pathName) throws Exception {// ���ļ�pathNameд�뵽����
		os.write(yourId);// �ϴ�Ŀ��ͻ�
		os.write(1);// ���ļ���1��ʾ

		File file = new File(pathName);
		String fileType = pathName.substring(pathName.indexOf('.'), pathName.length());
		byte b[] = fileType.getBytes("GBK");

		os.writeLong(file.length() + b.length + 8);// �ܳ��ȣ�long�ͣ�=�ϴ��ļ�����+�ļ����ͳ���+8
		os.writeLong(b.length);// �ϴ��ļ����ͳ��ȣ�8byte��
		os.write(b);// �ϴ��ļ�����

		FileInputStream fis = new FileInputStream(file);
		int c;
		while ((c = fis.read()) != -1) {// �ļ�������־��-1
			os.write(c);
		}
		fis.close();
		os.flush();
	}

	public String fileFromStream() throws Exception {// �����л���ļ��������ļ���
		long length = is.readLong();
		int fileTypeLength = (int) is.readLong();// ����8�ֽڵ�long����תΪint
		long fileLength = length - fileTypeLength - 8;

		byte b[] = new byte[fileTypeLength];
		for (int i = 0; i < fileTypeLength; i++) {
			b[i] = is.readByte();
		}
		String fileType = new String(b, "GBK");

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dateFormat.format(date);
		String fileName = time + fileType;
		FileOutputStream fos = new FileOutputStream(fileName);

		while (fileLength-- > 0) {
			fos.write(is.read());
		}
		fos.close();
		return fileName;
	}

	public void transTo(IOStream yourStream) throws Exception {
		int isFile = is.read();
		long length = is.readLong();// �ܳ��ȣ�long�ͣ�
		yourStream.os.write(isFile);// �ļ���������
		yourStream.os.writeLong(length);// �ܳ���
		while (length-- > 0) {
			yourStream.os.write(is.read());
		}
		os.flush();
	}

	public void transToAllExcept(int myId) throws Exception {
		int isFile = is.read();
		long length = is.readLong();

		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this) == false) {
				yourStream.os.write(myId);
				yourStream.os.write(isFile);
				yourStream.os.writeLong(length);
			}
		}

		while (length-- > 0) {
			int c = is.read();
			for (IOStream yourStream : MultiTalkServer.allStream) {
				if (yourStream.equals(this) == false) {
					yourStream.os.write(c);
				}
			}
		}
		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this) == false) {
				yourStream.os.flush();
			}
		}
	}

	public void messageToStream(int yourId, String message) throws Exception {
		os.write(yourId);// �ϴ�Ŀ��ͻ�
		os.write(0);// ����������0��ʾ

		byte b[] = message.getBytes("GBK");
		os.writeLong(b.length);// �ܳ��ȣ�long�ͣ�������ֻ������
		os.write(b);// ��ֹ��������
		os.flush();
	}

	public String messageFromStream() throws Exception {// �����յ�����Ϣ
		int wordLength = (int) is.readLong();
		byte b[] = new byte[wordLength];
		for (int i = 0; i < wordLength; i++) {
			b[i] = is.readByte();
		}
		return new String(b, "GBK");// ��ֹ��������
	}
}
