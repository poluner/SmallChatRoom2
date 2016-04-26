package iostream;

import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.*;

import server.MultiTalkServer;

public class IOStream {
	final static int EOSsize = 1000;// EndOfStream,流结束标志长度
	public OutputStream os;
	public InputStream is;
	public Scanner sin;

	public IOStream(Socket socket) throws Exception {
		os = socket.getOutputStream();
		is = socket.getInputStream();
		sin = new Scanner(System.in);
	}

	public void addEOS() throws Exception {// EOS由一个254加EOSsize个255组成，避免文件结尾是255而误判成EOS
		os.write(254);
		for (int i = 0; i < EOSsize; i++) {
			os.write(255);
		}
	}

	public Vector<Integer> mayBeEOS() throws IOException {// 只有发现了一个254和EOSsize个255才是真正的EOS
		Vector<Integer> vi = new Vector<Integer>();

		vi.addElement(is.read());
		if (vi.lastElement() != 254)
			return vi;
		for (int i = 0; i < EOSsize; i++) {
			vi.addElement(is.read());
			if (vi.lastElement() != 255)
				return vi;
		}
		return vi;// 这里返回的才是EOS
	}

	public void fileToStream(String pathName) throws Exception {// 将文件pathName写入到流中
		String fileType = pathName.substring(pathName.indexOf('.'), pathName.length());
		os.write(fileType.getBytes());// 传入文件类型
		os.write(128);// 由于文件类型是西文字符，范围在0~127，用128作为分隔符即可

		FileInputStream fis = new FileInputStream(pathName);
		int c;
		while ((c = fis.read()) != -1) {// 文件结束标志是-1
			os.write(c);
		}
		fis.close();
	}

	public String fileFromStream() throws Exception {// 从流中获得文件，返回文件名
		String fileType = "";// 文件类型
		int c0;
		while ((c0 = is.read()) != 128) {// 文件名是西文字符范围0~127，所以用128作为分隔符
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
				break;// 如果是真正的EOS就结束了

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
				break;// 如果是真正的EOS就结束了

			for (Integer c : mayBeEos) {
				yourStream.os.write(c);
			}
		}
	}

	public void transToAllExcept(int myId) throws Exception {
		// 第一步：向每一个客户写入自己的ID
		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this))
				continue;// 消息不发送个自己
			yourStream.os.write(myId);// 告知目标客户自己的ID
		}
		// 第二步：取出自己流中的消息，发送给其他每一个客户
		while (true) {// 由于消息是一段一段传输的，所以只能分三步走，不可合成一步
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// 如果是真正的EOS就结束了

			for (IOStream yourStream : MultiTalkServer.allStream) {
				if (yourStream.equals(this))
					continue;// 消息不发送个自己
				for (Integer c : mayBeEos) {
					yourStream.os.write(c);
				}
			}
		}
		// 第三步：刷新每一个客户输出流
		for (IOStream yourStream : MultiTalkServer.allStream) {
			if (yourStream.equals(this))
				continue;// 消息不发送个自己
			yourStream.addEOS();
			yourStream.os.flush();
		}
	}

	public String messageFromStream() throws Exception {// 返回收到的信息
		byte[] b = new byte[102400];
		int cnt = 0;

		while (true) {
			Vector<Integer> mayBeEos = mayBeEOS();
			if (mayBeEos.size() == EOSsize + 1)
				break;// 如果是真正的EOS就结束了

			for (Integer c : mayBeEos) {
				b[cnt++] = c.byteValue();
			}
		}
		return new String(b, "GBK");// 防止中文乱码
	}
}
