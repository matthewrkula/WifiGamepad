import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class TestClient {
	
	Socket socket;
	DataOutputStream outputStream;
	
	public TestClient() throws Exception {
		socket = new Socket("10.0.0.4", 4848);
		outputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	public void start() throws Exception {
		Scanner s = new Scanner(System.in);
		
		int i;
		while(true){
			i = s.nextInt();
			outputStream.writeInt(i);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		new TestClient().start();
	}

}
