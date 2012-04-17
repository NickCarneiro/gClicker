package desktop_client;

import java.awt.Font;
import java.net.InetAddress;
import java.util.Observable;

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
   
   private Server server;
   
   public GClickerModel()
   {
//      server = new Server(this); // I'd like to just be able to construct a server like this
   }
   
      // gClicker Server should call this method whenever any clicker
      // connects/reconnects to the server. It will return an int that
      // represents a clickerID for the clicker that just connected.
   public int clickerConnected(String eid, InetAddress clickerAddress)
   {
      return 0;
   }
   
      // Any time an answer is submitted, this method should be called
      // by the gClicker server
   public void answerSubmitted(String eid, int choice)
   {
      
   }
   
      // This method will be called by GClicker to send out questions to
      // all GClickers. It contains a method to be implemented in Server.
      // It will traverse create a Question object and send submit it individually
      // to each gClicker.
   public void broadcastQuestion(String question, String[] answers)
   {
      //server.sendQuery(InetAddress inetAddress, Question question);
   }
}
