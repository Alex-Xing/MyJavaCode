package Tools;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import CreatNewAndroidSystem.SystemParams;

public class SampleTCPServer implements Runnable {
	//private boolean RunFlag = true;
	private String IP = null;
	private ServerSocket welcomeSocket;
	private Socket connectionSocket;
	public boolean isRun = false;
	//private SystemParams MyParams = new SystemParams();

	public SampleTCPServer() {
		try {
			welcomeSocket = new ServerSocket(
					Integer.parseInt(SystemParams.LocalHostTestPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String GetClientIP() {
		return IP;
	}

	public void Stop() {
		//RunFlag = false;
		isRun = false;
		CloseProt(Integer.parseInt(SystemParams.LocalHostTestPort));
		
		if (connectionSocket == null) {
			return;
		}
		try {
			if (!connectionSocket.isClosed()) {
				connectionSocket.close();
			}
			if (!welcomeSocket.isClosed()) {
				welcomeSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void CloseProt(int prot) {
		try {
			Socket s = new Socket("127.0.0.1", prot);
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	private String SendText() {
		String Text = "";
		Text = Text + "HTTP/1.1 200 OK\r\n";
		Text = Text + "Date: Tue, 04 Mar 2014 07:06:17 GMT\r\n";
		Text = Text + "Server: Apache/2.2.25 (Win32) PHP/5.4.25\r\n";
		Text = Text + "Last-Modified: Sat, 20 Nov 2004 07:16:24 GMT\r\n";
		Text = Text + "ETag: \"100000000c2bc-2c-3e94b66a46200\"\r\n";
		Text = Text + "Accept-Ranges: bytes\r\n";
		Text = Text + "Content-Length: 44\r\n";
		Text = Text + "Keep-Alive: timeout=5, max=100\r\n";
		Text = Text + "Connection: Keep-Alive\r\n";
		Text = Text + "Content-Type: text/html\r\n\r\n";
		Text = Text + "<html><body><h1>It works!</h1></body></html>";
		return Text;
	}

	public void run() {

		try {
			isRun = true;
			connectionSocket = welcomeSocket.accept();
			// System.out.println(connectionSocket.getInetAddress());
			IP = connectionSocket.getInetAddress().toString();
			IP = IP.substring(1);
			OutputStream out = connectionSocket.getOutputStream();
			out.write(SendText().getBytes(), 0, SendText().getBytes().length);
			connectionSocket.close();
			welcomeSocket.close();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			// e.printStackTrace();
			if (!connectionSocket.isClosed()) {
				try {
					connectionSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (!welcomeSocket.isClosed()) {
				try {
					welcomeSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SampleTCPServer TestServer = new SampleTCPServer();
		Thread T1 = new Thread(TestServer);
		T1.start();
	}

}
