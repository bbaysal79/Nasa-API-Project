/*
 * COMP416 - Project1: StratoNet: NASA-API based Network Application
 * Server Side
 * Hüseyin Burak Baysal - 61319
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int PORT = 4499;

	public static void main(String[] args) throws IOException {
		new Server().runServer();
	}
	
	public void runServer() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(PORT);
		print("Server is ready for connection...");
		while(true) {
			Socket s = ss.accept();
			new ServerThread(s).start();
		}
	}
	
	public static void print(String msg)
	{
        System.out.println(msg);
    }

}
