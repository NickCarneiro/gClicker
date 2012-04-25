package edu.desktop.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import edu.desktop.server.ui.GPerson;
import edu.shared.Answer;
import edu.shared.Question;


public class ClientObserver implements Observer{
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public InetAddress ip;
	public String eid;
	public int clicker_id;
	private Thread t; //runs ResponseMonitor
	public ClientObserver(Socket s, QuestionManager qm){
		this.socket = s;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			//prevent inputstream on the client from blocking
			out.flush();
			in =  new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			//wait for dummy answer with EID
			Answer dummyAnswer = (Answer) in.readObject();
			
			
			this.eid = dummyAnswer.eid;
			System.out.println("Got eid " + this.eid + " from " + s.getInetAddress().getHostAddress());
			this.ip = socket.getInetAddress();
			
			
			
			GPerson person = qm.model.clients.get(this.eid);
			if(person != null){
				//we've seen this eid before. reuse the clicker id.
				this.clicker_id = person.getClickerID();
			} else {
				//unrecognized person. generate a unique new id.
				this.clicker_id = qm.getClickerId();
			}
			
			//now send a dummy question to assign the clicker its id number
			Question dummyQuestion = new Question("");
			dummyQuestion.message = true;
			
			dummyQuestion.clicker_id = this.clicker_id;
			out.writeObject(dummyQuestion);
			out.flush();
			
			//tell UI that we got a new person
			qm.model.clickerConnected(this.eid, this.ip, this.clicker_id);
			
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
