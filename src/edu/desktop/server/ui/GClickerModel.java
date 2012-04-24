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
   public static final int NUM_CLICK_POSS = 40;
   public static final int NUM_CLICK_COLS = 10;
   
   private Server server;
   private QuestionManager qm;
   public TreeMap<String, GPerson> clients;
   private Question currentQuestion;
   
   public GClickerModel() {
      clients = new TreeMap<String, GPerson>();
      qm = new QuestionManager(this);
   }
   
      // gClicker Server should call this method whenever any clicker
      // connects/reconnects to the server. It will return an int that
      // represents a clickerID for the clicker that just connected.
   public int clickerConnected(String eid, InetAddress clickerAddress, int clicker_id){
      GPerson gPerson = clients.get(eid);
      //if person with this eid is connecting for the first time
      if (gPerson == null){
         gPerson = new GPerson(eid, clickerAddress, clicker_id);
         
         clients.put(gPerson.getEID(), gPerson);
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
      GPerson gPerson = clients.get(eid);
      // we may get an eid we don't recognize
      if(gPerson != null){
    	  gPerson.changeAnswer(choice);
          setChanged();
          notifyObservers();
      } else {
    	  System.out.println("Did not recognize eid " + eid);
      }
      
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
      GPerson gPerson;
      for (Map.Entry<String, GPerson> entry : clients.entrySet())
      {
         gPerson = entry.getValue();
         gPerson.reset();
      }
      
      setChanged();
      notifyObservers();
   }
   
   public TreeMap<String, GPerson> getPeople() { return clients; }
   public Question getBroadcastedQuestion() { return currentQuestion; }
}