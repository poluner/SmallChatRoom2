package server;

import iostream.IOStream;

public class ServerThread extends Thread {
	// 只有服务器需要处理发送者和接收者的io流，客户端只管自己的io流
	// 每一个线程都是对应客户在服务器上布置的socket，所以每一个线程都是发送者；
	// 客户端将信息发送到自己在服务器上布置的socket，然后服务器监听到之后将信息取出，得到目标客户的Id（接收者），
	// 然后将信息打入到接收者在服务器上布置的socket中；

	IOStream myStream;
	int myId;

	public ServerThread() {
		myStream = MultiTalkServer.allStream.lastElement();
		myId = MultiTalkServer.allStream.size() - 1;
	}

	public void run() { // 服务器线程
		try {
			myStream.os.write(myId);// 服务器端告知客户ID，相当于为客户分配ID
			myStream.os.flush();
			while (true) {// 服务器永远运行
				// 从自己在服务器中布置的socket中取出自己发送的信息，分离出自己要发送的目标客户的Id

				int yourId = myStream.is.read();// 目标客户
				if (yourId == myId) {// 如果youId==myId就将消息送达其他每一个客户(除了自己)，从而实现群聊
					myStream.transToAllExcept(myId);
				} else {// 否则送达指定客户
					IOStream yourStream = MultiTalkServer.allStream.elementAt(yourId);
					yourStream.os.write(myId);// 上传我的ID
					myStream.transTo(yourStream);
				}
			}
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
}
