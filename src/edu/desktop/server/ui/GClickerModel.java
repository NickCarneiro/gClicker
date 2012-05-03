package edu.desktop.server.ui;

import java.awt.Font;
import java.net.InetAddress;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import edu.desktop.server.QuestionManager;
import edu.desktop.server.Server;
import edu.shared.Question;


public class GClickerModel extends Observable
{
	public static final Font bigFont = new Font("sansserif", Font.BOLD, 16);
	public static final Font smallerFont = new Font("sansserif", Font.BOLD, 14);
	public static final Font monoSpaceBig = new Font("monospaced", Font.BOLD, 20);
	public static final int MAX_ANSWERS = 5;
	//number of possible answer choices
	public static final int NUM_CLICK_POSS = 40;
	public static final int NUM_CLICK_COLS = 10;

	private Server server;
	private QuestionManager qm;
	public GPerson[] clients;
	private Question currentQuestion;

	public GClickerModel() {
		clients = new GPerson[NUM_CLICK_POSS];
		qm = new QuestionManager(this);
	}

	public GPerson getPersonFromEid(String eid){
		for(GPerson person : clients){
			if(person != null && person.getEID().equals(eid)){
				return person;
			}
		}
		return null;
	}
	// gClicker Server should call this method whenever any clicker
	// connects/reconnects to the server. It will return an int that
	// represents a clickerID for the clicker that just connected.
	
	// It blocks so two people don't get the same clicker id
	public synchronized int clickerConnected(String eid, InetAddress clickerAddress){
		GPerson gPerson = null;
		//first check if this person is already in the array
		for(GPerson person : clients){
			if(person != null && person.getEID().equals(eid)){
				gPerson = person;
			}
		}
		//else:
		//the person with this eid is connecting for the first time
		if (gPerson == null){

			//find first available clicker id for person
			for(int i = 0; i < clients.length; i++){
				if(clients[i] == null){
					gPerson = new GPerson(eid, clickerAddress, i);
					clients[i] = gPerson;
					break;
				}
				if(i == clients.length - 1){
					System.out.println("Error: No more room for clickers.");
				}
			}

			setChanged();
			notifyObservers();
		}
		// else, if a client connects, disconnects, 
		// then reconnects he can keep his old clicker_id and object.

		// we're going to pretend there's an authentication layer in the handshake
		// that validates eids. In real life we would call some utexas.edu api.

		return gPerson.getClickerID();
	}

	// Any time an answer is submitted, this method should be called
	// by the gClicker server
	public void answerSubmitted(String eid, int choice){
		for(GPerson person : clients){
			if(person.getEID().equals(eid)){
				person.changeAnswer(choice);
				setChanged();
				notifyObservers();
				return;
			}
		}
		// we may get an eid we don't recognize

		System.out.println("Did not recognize eid " + eid);


	}

	// This method will be called by GClicker to send out questions to
	// all GClickers. It contains a method to be implemented in Server.
	// It will traverse create a Question object and then submit it 
	// individually to each gClicker address.
	public void broadcastQuestion(String question, String[] answers){
		currentQuestion = new Question(question);
		for (int i = 0; i < answers.length; i++){
			if(answers[i] != null && ! "".equals(answers[i])){
				currentQuestion.addChoice(answers[i]);
			}

		}
		qm.sendQuestion(currentQuestion);
	}

	
	public void resetClickers(){
		for(GPerson person : clients){
			if(person != null){
				person.reset();
			}
			
		}

		setChanged();
		notifyObservers();
	}

	public GPerson[] getPeople() { return clients; }
	public Question getBroadcastedQuestion() { return currentQuestion; }
}