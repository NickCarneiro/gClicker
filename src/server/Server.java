package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Server implements Runnable{
	ServerSocket listener;
	int port = 3000;
	QuestionManager qm;
	//run TCP server upon initialization
	public Server(QuestionManager qm){
		this.qm = qm;
		try{
			//whenever a client connects, add that connection to our clientlist
			listener = new ServerSocket(this.port);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			System.out.println("Server ready for client connections.");
			while(true){

				
				Socket clientSocket = listener.accept();
				System.out.println("Got connection from " + clientSocket.getInetAddress());
				ClientObserver co = new ClientObserver(clientSocket);
				
				qm.addObserver(co);
				System.out.println("Added " + clientSocket.getInetAddress() +" as an observer.");


			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
