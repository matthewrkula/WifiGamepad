import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class WifiGamepadServer {
	
	ServerSocket serverSocket;
	Socket activeSocket;
	
	DataInputStream socketReader;
	boolean isRunning = false;
	
	Robot robot;
	
	public WifiGamepadServer() throws UnknownHostException, IOException, AWTException {
		serverSocket = new ServerSocket(4848);
		
		System.out.println("My address is " + InetAddress.getLocalHost().getHostAddress());
		
		activeSocket = serverSocket.accept();
		socketReader = new DataInputStream(activeSocket.getInputStream());
		robot = new Robot();
	}
	
	public void start() throws IOException {
		System.out.println("Client connected!");
		isRunning = true;
		int button;
		boolean isDown;
		
		while(isRunning) {
			button = socketReader.readInt();
			isDown = socketReader.readBoolean();
			pressKey(button, isDown);
			System.out.println(button + " was " + (isDown ? "pressed" : "released"));
		}
	}
	
	public void pressKey(int i, boolean b){
		int keyEvent = 0;
		switch(i){
			case 1:
				keyEvent = KeyEvent.VK_LEFT;
				break;
			case 2:
				keyEvent = KeyEvent.VK_UP;
				break;
			case 3:
				keyEvent = KeyEvent.VK_RIGHT;
				break;
			case 4:
				keyEvent = KeyEvent.VK_DOWN;
				break;
		}
		if(b){
			robot.keyPress(keyEvent);
		} else {
			robot.keyRelease(keyEvent);
		}
	}
	
	public static void main(String[] args){
		try {
			new WifiGamepadServer().start();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
