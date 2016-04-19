package iostream;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class IOStream {
	public OutputStream os;
	public InputStream is;
	public Scanner sin;

	public IOStream(Socket socket) throws Exception {
		os = socket.getOutputStream();
		is = socket.getInputStream();
		sin = new Scanner(System.in);
	}

	public String getMessage() throws Exception {
		int c;
		String message = "";
		while ((c = is.read()) != 255) {
			message += (char) c;
		}
		return message;
	}

	public void transTo(IOStream yourStream) throws IOException {
		int c;
		while ((c = is.read()) != 255) {
			yourStream.os.write(c);
		}
	}
}
