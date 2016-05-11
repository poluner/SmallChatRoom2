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

	public void fileToStream(int yourId, String pathName) throws Exception {// 将文件pathName写入到流中
		os.write(yourId);// 上传目标客户
		os.write(1);// 发文件用1表示

		File file = new File(pathName);
		String fileType = pathName.substring(pathName.indexOf('.'), pathName.length());
		byte b[] = fileType.getBytes("GBK");

		os.writeLong(file.length() + b.length + 8);// 总长度（long型）=上传文件长度+文件类型长度+8
		os.writeLong(b.length);// 上传文件类型长度（8byte）
		os.write(b);// 上传文件类型

		FileInputStream fis = new FileInputStream(file);
		int c;
		while ((c = fis.read()) != -1) {// 文件结束标志是-1
			os.write(c);
		}
		fis.close();
		os.flush();
	}

	public String fileFromStream() throws Exception {// 从流中获得文件，返回文件名
		long length = is.readLong();
		int fileTypeLength = (int) is.readLong();// 读入8字节的long型在转为int
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
		long length = is.readLong();// 总长度（long型）
		yourStream.os.write(isFile);// 文件或者文字
		yourStream.os.writeLong(length);// 总长度
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
		os.write(yourId);// 上传目标客户
		os.write(0);// 发送文字用0表示

		byte b[] = message.getBytes("GBK");
		os.writeLong(b.length);// 总长度（long型），这里只有文字
		os.write(b);// 防止中文乱码
		os.flush();
	}

	public String messageFromStream() throws Exception {// 返回收到的信息
		int wordLength = (int) is.readLong();
		byte b[] = new byte[wordLength];
		for (int i = 0; i < wordLength; i++) {
			b[i] = is.readByte();
		}
		return new String(b, "GBK");// 防止中文乱码
	}
}
