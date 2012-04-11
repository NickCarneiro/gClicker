package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

import shared.Question;

public class QuestionManager extends Observable{
	Question current_question;
	
	
	public Question getQuestion(){
		return current_question;
	}
	public static void main(String[] args){
		QuestionManager qm = new QuestionManager();
		//The server runs in the background
		Thread t = new Thread(new Server(qm));
		t.start();
		
		
		qm.serverShell();
		//we're blocked here until app is quit.
		
	}
	
	
	/**
	 * Processes commands for QuestionManager
	 */
	private void serverShell(){
		try {
			//prompt for IP address
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			//current question isn't set until the user creates one.
			current_question = null;
			
			System.out.println("Welcome to the interactive QuestionManager shell.");
			System.out.println("Commands: /question, /send, /clear");
			while(true){
				line = reader.readLine();
				if(line.equals("exit")){
					break;
				}
				
				if(line.contains("/question")){
					current_question = new Question(line.replace("/question ", ""));
					System.out.println("Added new question: " + current_question);
				} else if(line.contains("/send")) {
					//broadcast question if it's valid
					if(current_question == null){
						System.out.println("Question must be created with /question");
					}
					else if(current_question.size() < 2){
						System.out.println("A question must have at least 2 choices.");
					} else {
						System.out.println("Broadcasting question to " + this.countObservers() + " clients.");
						System.out.println(current_question);
						//broadcast the question
						this.setChanged();
						this.notifyObservers();
					}
				}
				else if(line.contains("/clear")){
					System.out.println("clearing question");
					current_question = null;
				}
				else {
					//assume it's an answer choice
					if(current_question == null){
						System.out.println("Must create a question before adding answer choices.");
					} else {
						current_question.addChoice(line);
						char letter = (char) (current_question.size() + 64);
						System.out.println("Added answer choice: " + letter + ": " + line);
					}
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}