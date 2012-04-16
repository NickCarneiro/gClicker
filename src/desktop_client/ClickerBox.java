package src.desktop_client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ClickerBox extends JPanel implements ActionListener
{
   private static final Font FONT = new Font("sansserif", Font.BOLD, 20);
   
   private int number;
   private JLabel text;
   private Timer flashTimer;
   private String answer;
   
   public ClickerBox(int num)
   {
      setBackground(Color.LIGHT_GRAY);
      setBorder(BorderFactory.createLineBorder(Color.BLACK));
      setPreferredSize(new Dimension(40, 30));
      
      text = new JLabel("" + num);
      text.setFont(FONT);
      add(text);
      
      flashTimer = new Timer(800, this);
      flashTimer.setRepeats(false);
   }
   
   public void flashGreen()
   {
      setBackground(Color.GREEN);
      flashTimer.restart();
   }
   
   public void flashYellow()
   {
      setBackground(Color.YELLOW);
      flashTimer.restart();
   }
   
   public void flashRed()
   {
      setBackground(Color.RED);
      flashTimer.restart();
   }
   
   public void actionPerformed(ActionEvent e)
   {
      setBackground(Color.BLUE);
   }
   
   public void changeAnswer(String ans)
   {
      if (answer == null)
         flashGreen();
      else
         flashYellow();
      
      answer = ans;
   }
   
   public void reset() 
   { 
      answer = null;
      setBackground(Color.WHITE); 
   }
   
   public Dimension getPreferredSize() { return new Dimension(55, 40); }
   public Dimension getMinimumSize() { return getPreferredSize(); }
   public Dimension getSize() { return getPreferredSize(); }
   public Integer getNum() { return new Integer(number); }
   
   public static void main(String args[])
   {
      try
      {
         ClickerBox content = new ClickerBox(27);
         content.reset();
         
         JFrame f = new JFrame();
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         f.getContentPane().add(content);
         f.pack();
         f.setLocation(30, 20);
         f.setVisible(true);
         
      } catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
