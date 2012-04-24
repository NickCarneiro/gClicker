package server_ui;

import java.net.InetAddress;

public class GPerson 
{
   public static final int NO_ANSWER = -1;
   private static boolean availableClickers[] = null;
   
   private boolean newAnswer;
   private int answer, clickerID;
   private InetAddress address;
   private String eid;
   
   public GPerson(String id, InetAddress addr, int clicker_id)
   {
      if (availableClickers == null)
      {
         availableClickers = new boolean[GClickerModel.NUM_CLICK_POSS];
         for (int i = 0; i < availableClickers.length; i++)
            availableClickers[i] = true;
      }
      
      eid = id;
      address = addr;
      newAnswer = false;
      answer = -1;
      
      assignClickerID(clicker_id);
   }
   
   //	doesn't need to be synchronized because QuestionManager getId 
   // is synchronized and guarantees everything to be unique
   private void assignClickerID(int clicker_id)
   {
      this.clickerID = clicker_id;
      availableClickers[clicker_id] = false;
   }
   
   public void changeAnswer(int ans)
   {
      answer = ans;
      setNewAnswer(true);
   }
   
   public void reset() 
   { 
      setNewAnswer(false);
      changeAnswer(NO_ANSWER); 
   }
   
   
   public void setNewAnswer(boolean b) { newAnswer = b; }
   public void setClickerID(int id) { clickerID = id; }
   
   public boolean isNewAnswer() { return newAnswer; }
   public int getAnswer() { return answer; }
   public int getClickerID() { return clickerID; }
   public InetAddress getAddress() { return address; }
   public String getEID() { return eid; }
}