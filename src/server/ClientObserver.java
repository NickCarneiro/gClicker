package server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import shared.Question;

public class ClientObserver implements Observer{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	public ClientObserver(Socket s){
		this.socket = s;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@Override
	public void update(Observable o, Object arg1) {
		try {
			QuestionManager qm = (QuestionManager) o;
			Question question = qm.getQuestion();
			if(question == null){
				System.out.println("Error: should not have gotten a null question in ClientObserver");
				return;
			}



			System.out.println("Got a question " + question);

			//serialize this question and send it over the socket
			ObjectOutputStream op = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			op.writeObject(question);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
