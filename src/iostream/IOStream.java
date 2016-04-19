package iostream;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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

	public void addEOS() throws Exception {
		for (int i = 0; i < EOSsize; i++) {
			os.write(255);
		}
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

		String fileName = "tmp" + fileType;
		FileOutputStream fos = new FileOutputStream(fileName);

		int c[] = new int[EOSsize];
		while (true) {
			for (int i = 0; i < EOSsize; i++)
				c[i] = -1;

			for (int i = 0; i < EOSsize; i++) {
				if ((c[i] = is.read()) != 255)
					break;
			}
			if (c[EOSsize - 1] == 255)
				break;
			for (int i = 0; i < EOSsize; i++) {
				if (c[i] == -1)
					break;
				fos.write(c[i]);
			}
		}
		fos.close();
		return fileName;
	}

	public void transTo(IOStream yourStream) throws Exception {
		int c[] = new int[EOSsize];
		while (true) {
			for (int i = 0; i < EOSsize; i++)
				c[i] = -1;
			for (int i = 0; i < EOSsize; i++) {
				if ((c[i] = is.read()) != 255)
					break;
			}
			if (c[EOSsize - 1] == 255)
				break;
			for (int i = 0; i < EOSsize; i++) {
				if (c[i] == -1)
					break;
				yourStream.os.write(c[i]);
			}
		}
	}
}
