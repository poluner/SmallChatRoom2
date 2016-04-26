package iostream;

import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.*;

import server.MultiTalkServer;

public class IOStream {
	final static int EOSsize = 1000;// EndOfStream,��������־����
	public OutputStream os;
	public InputStream is;
	public Scanner sin;

	public IOStream(Socket socket) throws Exception {
		os = socket.getOutputStream();
		is = socket.getInputStream();
		sin = new Scanner(System.in);
	}

	public void addEOS() throws Exception {// EOS��һ��254��EOSsize��255��ɣ������ļ���β��255�����г�EOS
		os.write(254);
		for (int i = 0; i < EOSsize; i++) {
			os.write(255);
		}
	}

	public Vector<Integer> mayBeEOS() throws IOException {// ֻ�з�����һ��254��EOSsize��255����������EOS
		Vector<Integer> vi = new Vector<Integer>();

		vi.addElement(is.read());
		if (vi.lastElement() != 254)
			return vi;
		for (int i = 0; i < EOSsize; i++) {
			vi.addElement(is.read());
			if (vi.lastElement() != 255)
				return vi;
		}
		return vi;// ���ﷵ�صĲ���EOS
	}

	public void fileToStream(String pathName) throws Exception {// ���ļ�pathNameд�뵽����
		String fileType = pathName.substring(pathName.indexOf('.'), pathName.length());
		os.write(fileType.getBytes());// �����ļ�����
		os.write(128);// �����ļ������������ַ�����Χ��0~127����128��Ϊ�ָ�������

		FileInputStream fis = new FileInputStream(pathName);
		int c;
		while ((c = fis.read()) != -1) {// �ļ�������־��-1
			os.write(c);
		}
		fis.close();
	}

	public String fileFromStream() throws Exception {// �����л���ļ��������ļ���
		String fileType = "";// �ļ�����
		int c0;
		while ((c0 = is.read()) != 128) {// �ļ����������ַ���Χ0~127��������128��Ϊ�ָ���
			fileType += (char) c0;
		}

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = dateFormat.format(date);
		String fileName = time + fileType;
		FileOutputStream fos = new FileOutputStream(fileName);

		while (true) {
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// �����������EOS�ͽ�����

			for (Integer c : mayBeEos) {
				fos.write(c);
			}
		}
		fos.close();
		return fileName;
	}

	public void transTo(IOStream yourStream) throws Exception {
		while (true) {
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// �����������EOS�ͽ�����

			for (Integer c : mayBeEos) {
				yourStream.os.write(c);
			}
		}
	}

	public void transToAllExcept(int myId) throws Exception {
		// ��һ������ÿһ���ͻ�д���Լ���ID
		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this))
				continue;// ��Ϣ�����͸��Լ�
			yourStream.os.write(myId);// ��֪Ŀ��ͻ��Լ���ID
		}
		// �ڶ�����ȡ���Լ����е���Ϣ�����͸�����ÿһ���ͻ�
		while (true) {// ������Ϣ��һ��һ�δ���ģ�����ֻ�ܷ������ߣ����ɺϳ�һ��
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// �����������EOS�ͽ�����

			for (IOStream yourStream : MultiTalkServer.allStream) {
				if (yourStream.equals(this))
					continue;// ��Ϣ�����͸��Լ�
				for (Integer c : mayBeEos) {
					yourStream.os.write(c);
				}
			}
		}
		// ��������ˢ��ÿһ���ͻ������
		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this))
				continue;// ��Ϣ�����͸��Լ�
			yourStream.addEOS();
			yourStream.os.flush();
		}
	}

	public String messageFromStream() throws Exception {// �����յ�����Ϣ
		byte[] b = new byte[102400];
		int cnt = 0;

		while (true) {
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// �����������EOS�ͽ�����

			for (Integer c : mayBeEos) {
				b[cnt++] = c.byteValue();
			}
		}
		return new String(b, "GBK");// ��ֹ��������
	}
}
