package client;

import java.awt.FileDialog;
import java.awt.event.*;
import javax.swing.*;

public class WinClient extends JFrame implements ActionListener, KeyListener {
	int myId;// ��¼�Լ���ID
	boolean isFile = false;// Ĭ�ϲ������ļ�
	String pathName = null;
	KeyEvent keyEvent = null;
	JTextArea receiveText;
	JTextField sendText, receiverIdText;
	JButton selectFileButton;
	JLabel receiverIdLbael;

	public WinClient() {
		receiveText = new JTextArea(20, 40);
		receiverIdLbael = new JLabel("client Id: ");
		receiverIdText = new JTextField(2);
		sendText = new JTextField(20);
		selectFileButton = new JButton("select a file");

		JPanel p = new JPanel();
		p.add(receiveText);
		p.add(receiverIdLbael);
		p.add(receiverIdText);
		p.add(sendText);
		p.add(selectFileButton);
		add(p);

		setSize(470, 500);
		setVisible(true);

		selectFileButton.addActionListener(this);
		sendText.addKeyListener(this);
	}

	void showImage(String fileName) {// ���·��
		ImageIcon imageIcon = new ImageIcon(fileName);
		JLabel l = new JLabel();
		l.setIcon(imageIcon);
		JDialog d = new JDialog(this, fileName);
		d.add(l);
		d.setSize(750, 750);
		d.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectFileButton && receiverIdText.getText().length() != 0) {
			FileDialog fd = new FileDialog(this, "select a file", FileDialog.LOAD);
			fd.setVisible(true);

			pathName = fd.getDirectory();
			if (pathName == null)
				return;
			pathName += fd.getFile();
			isFile = true;// ��ʱȷ�������ļ�
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {// ��¼�س�
		keyEvent = e;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public static void main(String[] args) {
		new WinClient();
	}

}
