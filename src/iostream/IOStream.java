package iostream;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class IOStream {
	public PrintWriter os;
	public BufferedReader is;
	public Scanner sin;

	public IOStream(Socket socket) {
		try {
			os = new PrintWriter(socket.getOutputStream());// ��Socket����õ��������������PrintWriter����
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));// ��Socket����õ�����������������Ӧ��BufferedReader����
			sin = new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
