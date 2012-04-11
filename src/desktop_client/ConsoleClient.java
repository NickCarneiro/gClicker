package desktop_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * command line client
 */
public class ConsoleClient {
	private static String server_ip = "127.0.0.1:3000";

	public static void main(String[] args){
		try {
			//prompt for IP address
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String ip = reader.readLine();
			if(!ip.trim().equals("")){
				server_ip = ip;
			}
			
			//connect to server
			
			System.out.println("Connecting to " + server_ip);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
