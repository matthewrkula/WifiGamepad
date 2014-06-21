import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class WifiGamepadServer {
	
	ServerSocket serverSocket;
	Socket activeSocket;
	
	DataInputStream socketReader;
	boolean isRunning = false;
	
	Robot robot;
	HashMap<Integer, Integer> keyMap;
	
	public WifiGamepadServer() throws UnknownHostException, IOException, AWTException {
		serverSocket = new ServerSocket(4848);
		
		System.out.println("My address is " + NetworkUtils.getIp());
		
		activeSocket = serverSocket.accept();
		socketReader = new DataInputStream(activeSocket.getInputStream());
		robot = new Robot();
		generateKeyMap();
	}
	
	private void generateKeyMap(){
		keyMap = new HashMap<Integer, Integer>();
		keyMap.put(1, KeyEvent.VK_LEFT);
		keyMap.put(2, KeyEvent.VK_UP);
		keyMap.put(3, KeyEvent.VK_RIGHT);
		keyMap.put(4, KeyEvent.VK_DOWN);
		keyMap.put(5, KeyEvent.VK_D);
		keyMap.put(6, KeyEvent.VK_S);
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
		int keyEvent = keyMap.get(i);
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
