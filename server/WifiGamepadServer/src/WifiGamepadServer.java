import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class WifiGamepadServer {
	
	int desiredPort = 4848;
	ServerSocket serverSocket;
	
	DataInputStream socketReader;
	boolean isRunning = true;
	
	public WifiGamepadServer() throws UnknownHostException, IOException {
		boolean socketFound = false;
		
		while(!socketFound){
			try {
				serverSocket = new ServerSocket(desiredPort);
				socketFound = true;
			} catch (BindException e){
				desiredPort++;
			}
		}
		
		System.out.println("IPAddress: " + NetworkUtils.getIp());
		System.out.println("Port: " + desiredPort);
	}
	
	public void start() throws IOException, AWTException {
		Socket activeSocket;
		while (isRunning) {
			activeSocket = serverSocket.accept();
			new Thread(new Session(activeSocket)).start();
		}
	}

	public static void main(String[] args) {
		try {
			new WifiGamepadServer().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
