package server;

import java.net.*;
import java.util.Vector;
import iostream.IOStream;

public class MultiTalkServer {
	static Vector<IOStream> allStream = new Vector<IOStream>();// 保存所有客户的流

	public static void main(String args[]) {
		try {
			ServerSocket serverSocket = new ServerSocket(4700); // 创建一个ServerSocket在端口4700监听客户请求

			while (true) {// 循环监听
				Socket socket = serverSocket.accept();
				allStream.addElement(new IOStream(socket));
				new ServerThread().start();
			}
		} catch (Exception e) {
			System.out.println("Could not listen on port:4700.");// 出错，打印出错信息
		}
	}
}
