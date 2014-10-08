import java.awt.AWTException;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
	
	Robot robot;
	
	Socket socket;
	DataInputStream socketReader;
	
	boolean isRunning;
	
	public Session(Socket socket) throws IOException, AWTException {
		this.socket = socket;
		this.socketReader = new DataInputStream(socket.getInputStream());
		this.robot = new Robot();
	}

	@Override
	public void run() {
		System.out.println("Client connected!");
		isRunning = true;
		int button;
		boolean isDown;

		while (isRunning) {
			try {
				button = socketReader.readInt();
				isDown = socketReader.readBoolean();

				if (button == -1) {
					isRunning = false;
				} else {
					pressKey(button, isDown);
				}

			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}
	
	public void pressKey(int i, boolean b){
		int keyEvent = i;
		System.out.println("Received key: " + i);
		if (b) {
			robot.keyPress(keyEvent);
		} else {
			robot.keyRelease(keyEvent);
		}
	}

}
