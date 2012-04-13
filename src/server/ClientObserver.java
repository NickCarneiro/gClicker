package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import shared.Answer;
import shared.Question;

public class ClientObserver implements Observer{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public InetAddress ip;
	public String eid;
	private Thread t; //runs ResponseMonitor
	public ClientObserver(Socket s){
		this.socket = s;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			//prevent inputstream on the client from blocking
			out.flush();
			in =  new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			//wait for dummy answer with EID
			Answer dummyAnswer = (Answer) in.readObject();
			
			
			this.eid = dummyAnswer.eid;
			this.ip = socket.getInetAddress();
			ClickerModel.clickerConnected(this);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@SuppressWarnings("deprecation")
	@Override
	public void update(Observable o, Object arg1) {
		try {
			QuestionManager qm = (QuestionManager) o;
			Question question = qm.getQuestion();
			if(question == null){
				System.out.println("Error: should not have gotten a null question in ClientObserver");
				return;
			}



			//System.out.println("Got a question:");
			//System.out.println(question);

			//serialize this question and send it over the socket
			
			out.writeObject(question);
			out.flush();
			
			if(t != null){
				t.stop();
			}
			//wait for an answer in another thread
			t = new Thread(new ResponseMonitor(qm, in));
			t.start();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
