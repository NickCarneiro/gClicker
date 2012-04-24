package server_ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class VisibleTimer extends JPanel implements ActionListener
{
   public static final String START = "Start";
   public static final String PAUSE = "Pause";
   public static final String STOP = "Stop";
   public static final String EXPIRED = "Expired";
   public static final String CANCEL = "Cancel";
   public static final String SUBMIT = "Submit";
   
   private AlignedPanel minSelect, middle, secSelect;
   private boolean active;
   private int initTime, timeRem;
   private JButton startStop, cancelSubmit, upMin, downMin, upSec, downSec;
   private JLabel minutes, seconds, colon;
   private JPanel control;
   private Timer redrawTimer;
   private Vector<TimerListener> listeners;
   
   public VisibleTimer(int iTime)
   {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      setBorder(BorderFactory.createTitledBorder("Timer:"));
      active = false;
      timeRem = 0;
      initTime = iTime;
      redrawTimer = new Timer(1000, this);
      listeners = new Vector<TimerListener>();
      
      startStop = new JButton("Start");
      startStop.setPreferredSize(new Dimension(80, 40));
      cancelSubmit = new JButton("Cancel");
      cancelSubmit.setPreferredSize(new Dimension(80, 40));
      upMin = new JButton("^");
      downMin = new JButton("v");
      upSec = new JButton("^");
      downSec = new JButton("v");
      
      minutes = new JLabel();
      minutes.setFont(GClickerModel.bigFont);
      seconds = new JLabel();
      seconds.setFont(GClickerModel.bigFont);
      colon = new JLabel(" : ");
      colon.setFont(GClickerModel.bigFont);
      redrawText(true);
      
      control = new AlignedPanel("CENTER");
      minSelect = new AlignedPanel("CENTER");
      middle = new AlignedPanel("CENTER");
      secSelect = new AlignedPanel("CENTER");
      
      control.add(startStop);
      control.add(Box.createVerticalGlue());
      control.add(cancelSubmit);
      
      minSelect.add(upMin);
      minSelect.add(minutes);
      minSelect.add(downMin);
      
      middle.add(Box.createVerticalStrut(upMin.getHeight()));
      middle.add(Box.createVerticalStrut(1));
      middle.add(colon);
      middle.add(Box.createVerticalStrut(downMin.getHeight()));
      
      secSelect.add(upSec);
      secSelect.add(seconds);
      secSelect.add(downSec);
      
      add(control);
      add(Box.createHorizontalStrut(10));
      add(minSelect);
      add(middle);
      add(secSelect);
      
      startStop.addActionListener(this);
      cancelSubmit.addActionListener(this);
      upMin.addActionListener(this);
      downMin.addActionListener(this);
      upSec.addActionListener(this);
      downSec.addActionListener(this);
   }
   
   private void redrawText(boolean init)
   {
      int time;
      if (init)
      {
         timeRem = initTime;
         time = initTime;
      }
      else
         time = timeRem;
         
      if (time < 600)
         minutes.setText("0" + time / 60);
      else
         minutes.setText("" + time / 60);
      
      if ((time % 60) < 10)
         seconds.setText("0" + time % 60);
      else
         seconds.setText("" + time % 60);
   }
   
   private void enableButtons(boolean enable)
   {
      upMin.setEnabled(enable);
      downMin.setEnabled(enable);
      upSec.setEnabled(enable);
      downSec.setEnabled(enable);
      active = !enable;
   }
   
   private void checkTime()
   {
      if (timeRem <= 0)
      {
         redrawTimer.stop();
         notifyChange(EXPIRED);
         enableButtons(true);
         redrawText(true);
         startStop.setText(START);
      }  
   }
   
   public void notifyChange(String s)
   {
      for (int i = 0; i < listeners.size(); i++)
         listeners.get(i).stateChanged(s);
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == redrawTimer)
      {
         timeRem -= 1;
         redrawText(false);
         checkTime();
      }
      else if (e.getSource() == startStop)
      {
         if (startStop.getText().equals(START))
         {
            if (timeRem == 0)
               timeRem = initTime;
            
            enableButtons(false);
            startStop.setText(PAUSE);
            cancelSubmit.setText(SUBMIT);
            notifyChange(START);
            redrawTimer.restart();
         }
         else
         {
            startStop.setText(START);
            cancelSubmit.setText(CANCEL);
            notifyChange(PAUSE);
            redrawTimer.stop();
         }
      }
      else if (e.getSource() == cancelSubmit)
      {
         redrawTimer.stop();
         
         if (cancelSubmit.getText().equals(CANCEL))
            notifyChange(CANCEL);
         else
            notifyChange(SUBMIT);
         
         redrawText(true);
         enableButtons(true);
         startStop.setText(START);
         cancelSubmit.setText(CANCEL);
      }
      else if (e.getSource() == upMin)
      {
         if (initTime < 5940)
         {
            initTime += 60;
            redrawText(true);
         }
      }
      else if (e.getSource() == downMin)
      {
         if (initTime >= 60)
         {
            initTime -= 60;
            redrawText(true);
         }
      }
      else if (e.getSource() == upSec)
      {
         if (initTime < 5999)
         {
            initTime += 1;
            redrawText(true);
         }
      }
      else if (e.getSource() == downSec)
      {
         if (initTime > 0)
         {
            initTime -= 1;
            redrawText(true);
         }
      }
   }
   
   public void addListener(TimerListener ti)
   {
      listeners.add(ti);
   }
   
   public boolean isActive() { return active; }
   
   public static void main(String args[])
   {
      try
      {
         VisibleTimer content = new VisibleTimer(60);
         
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