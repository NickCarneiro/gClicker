package desktop_client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

import shared.Question;

/*
 * command line client
 */
public class ConsoleClient{
	private static String server_ip = "127.0.0.1:3000";
	private static BufferedReader in;
	private static PrintWriter out;
	public static void main(String[] args){
		try {
			//prompt for IP address
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter server's IP");
			String ip = reader.readLine();
			if(!ip.trim().equals("")){
				server_ip = ip;
			}
			
			//connect to server
			
			System.out.println("Connecting to " + server_ip);
			int port = Integer.parseInt(server_ip.split(":")[1]);
			String ia = server_ip.split(":")[0];
			Socket socket = new Socket(ia, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
			System.out.println("Waiting for question.");
			Question question = (Question) input.readObject();
			while(question != null){
				System.out.println(question);
				question = (Question) input.readObject();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
