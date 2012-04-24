package desktop_client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

import shared.Answer;
import shared.Question;

/*
 * command line client
 */
public class ConsoleClient{
	private static String server_ip = "127.0.0.1:3000";
	private static BufferedReader in;
	private static int clicker_id;
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
			String eid = "njc487";

			if(args.length == 1){
				eid = args[0];
			}
			System.out.println("My eid is " + eid);
			System.out.println("Connecting to " + server_ip);
			int port = Integer.parseInt(server_ip.split(":")[1]);
			String ia = server_ip.split(":")[0];
			Socket socket = new Socket(ia, port);

			ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));


			Answer dummyAnswer = new Answer(0, Integer.MAX_VALUE, eid);
			output.writeObject(dummyAnswer);
			//prevent InputStream on the server from blocking
			output.flush();

			System.out.println("Waiting for clicker id.");
			Question question = (Question) input.readObject();
			if(question.message == true){
				clicker_id = question.clicker_id;
				System.out.println("Clicker id assigned by server: " + clicker_id);
			}

			System.out.println("Waiting for question.");
			question = (Question) input.readObject();
			while(question != null){
				System.out.println(question);
				//read answer choice (blocks until user hits enter)
				int choice = readAnswer(reader, question.size());
				Answer answer = new Answer(choice, question.id, eid);
				//send back to the server
				output.writeObject(answer);
				output.flush();
				//wait for next question (block here)
				System.out.println("Answer sent. Waiting for next question.");
				question = (Question) input.readObject();
			}


		} catch(EOFException e){
				System.out.println("Server disconnected.");
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}

	/*
	 * return an integer representing the user's answer.
	 * A -> 0
	 * b -> 1, etc.
	 * 
	 * choice_length is the number of possible answer choices the user has to choose from.
	 */
	private static int readAnswer(BufferedReader reader, int choice_length){
		try{

			String line;
			int result = 0;
			while((line = reader.readLine()) != null){
				String choice = line.toLowerCase().trim();
				if(choice.length() != 1){
					System.out.println("Please enter a letter.");
					continue;
				}
				char answer_char = choice.charAt(0);
				result = (int) answer_char - 97;
				if(result < 0 || result > choice_length - 1){
					System.out.println("Please enter a valid letter for one of the answer choices.");
					continue;
				}

				break;
			}

			return result;
		} catch(IOException e){
			e.printStackTrace();
			return 0;
		} 

	}
}
