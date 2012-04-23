package server_ui;

import java.awt.Font;
import java.net.InetAddress;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import server.QuestionManager;
import server.Server;
import shared.Question;

public class GClickerModel extends Observable
{
   public static final Font bigFont = new Font("sansserif", Font.BOLD, 16);
   public static final Font smallerFont = new Font("sansserif", Font.BOLD, 14);
   public static final Font monoSpaceBig = new Font("monospaced", Font.BOLD, 20);
   public static final int NUM_ANSWERS = 5;
   public static final int NUM_CLICK_POSS = 40;
   public static final int NUM_CLICK_COLS = 10;
   
   private QuestionManager qm;
   private TreeMap<String, GPerson> clients;
   private Question currentQuestion;
   
   public GClickerModel()
   {
      clients = new TreeMap<String, GPerson>();
      qm = new QuestionManager(this); // I'd like to just be able to construct a server like this
   }
   
      // gClicker Server should call this method whenever any clicker
      // connects/reconnects to the server. It will return an int that
      // represents a clickerID for the clicker that just connected.
   public int clickerConnected(String eid, InetAddress clickerAddress)
   {
      GPerson gPerson = clients.get(eid);
      if (gPerson == null)
      {
         gPerson = new GPerson(eid, clickerAddress);
         clients.put(gPerson.getEID(), gPerson);
         setChanged();
         notifyObservers();
      }
      
      return gPerson.getClickerID();
   }
   
      // Any time an answer is submitted, this method should be called
      // by the gClicker server
   public void answerSubmitted(String eid, int choice)
   {
      GPerson gPerson = clients.get(eid);
      gPerson.changeAnswer(choice);
      setChanged();
      notifyObservers();
   }
   
      // This method will be called by GClicker to send out questions to
      // all GClickers. It contains a method to be implemented in Server.
      // It will traverse create a Question object and then submit it 
      // individually to each gClicker address.
   public void broadcastQuestion(String question, String[] answers){
	  System.out.println("broadcasting question: " + question);
      Question query = new Question(question);
      for (int i = 0; i < answers.length; i++){
    	 if(answers[i] != null && ! "".equals(answers[i])){
    		 query.addChoice(answers[i]);
    	 }
         
      }
      
      qm.sendQuestion(query);
   }
   
   public void resetClickers()
   {
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
}
