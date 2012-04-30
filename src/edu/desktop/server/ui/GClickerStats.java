package edu.desktop.server.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.desktop.server.ui.GClicker.AnswerField;


@SuppressWarnings("serial")
public class GClickerStats extends JDialog implements ActionListener
{
   private static final String SUBMIT = "Submit";
   private static final String CANCEL = "Cancel";
   private static final String CLOSE = "Close";
   private AlignedPanel content;
   private AnswerOption answers[];
   private ButtonGroup answerGroup;
   private GClicker gClicker;
   private GClickerModel model;
   private int answerCounts[];
   private JButton submitClose, cancel;
   private JPanel buttonPanel;
   
   public GClickerStats(GClicker gc, GClickerModel m){
      super(gc, "Answer Select", true);
      gClicker = gc;
      model = m;
      
      content = new AlignedPanel("LEFT");
      content.setBorder(BorderFactory.createTitledBorder("Select correct answer: "));
      answerGroup = new ButtonGroup();
      
      //converting answer fields from main input into answer options on stat selection view
      AnswerField[] answerFields = gClicker.getAnswers();
      int numAns = answerFields.length;
      answers = new AnswerOption[numAns];
      for (int i = 0; i < numAns; i++)
      {
         answers[i] = new AnswerOption(answerFields[i].getChoice(), 
        		 model.getBroadcastedQuestion().getAnswers()[i]);
         content.add(answers[i]);
         answerGroup.add(answers[i].getButton());
      }
      
      submitClose = new JButton(SUBMIT);
      submitClose.setPreferredSize(new Dimension(80, 40));
      cancel = new JButton(CANCEL);
      cancel.setPreferredSize(new Dimension(80, 40));
      submitClose.addActionListener(this);
      cancel.addActionListener(this);
      
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(cancel);
      buttonPanel.add(Box.createHorizontalStrut(5));
      buttonPanel.add(submitClose);
      content.add(buttonPanel, "RIGHT");
      
      add(content);
      pack();
      setLocationRelativeTo(gClicker.getRelativeComponent());
      setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == cancel)
      {
         setVisible(false);
         dispose();
      }
      else if (e.getSource() == submitClose)
      {
         if (submitClose.getText().equals(SUBMIT) && oneSelected())
         {
            enableButtons(false);
            cancel.setEnabled(false);
            submitClose.setText(CLOSE);
            updateStats();
         }
         else if (submitClose.getText().equals(CLOSE))
         {
            setVisible(false);
            dispose();
         }
      }
   }
   
   private void updateAnswerCounts()
   {
      TreeMap<String, GPerson> people = model.getPeople();

      answerCounts = new int[answers.length + 1];
      for (int i = 0; i < answerCounts.length; i++)
      {
         answerCounts[i] = 0;
      }
      
      GPerson gPerson;
      for (Map.Entry<String, GPerson> entry : people.entrySet())
      {
         gPerson = entry.getValue();
         
         if (gPerson.getAnswer() == GPerson.NO_ANSWER)
            answerCounts[answerCounts.length - 1]++;
         else
            answerCounts[gPerson.getAnswer()]++;
      }
   }
   
   private boolean oneSelected()
   {
      for (int i = 0; i < answers.length; i++)
      {
         if (answers[i].isSelected())
            return true;
      }
      
      return false;
   }
   
   private void enableButtons(boolean enable)
   {
      for (int i = 0; i < answers.length; i++)
      {
         answers[i].getButton().setEnabled(enable);
      }
   }
   
   private void updateStats()
   {
      updateAnswerCounts();
      
      for (int i = 0; i < answers.length; i++)
      {
         answers[i].updateStat(answerCounts[i], model.getPeople().size());
      }
   }
   
   private class AnswerOption extends JPanel
   {
      private static final int MAX_WIDTH = 24;
      private String data;
      private char choice;
      private JLabel stat, filler;
      private JRadioButton button;
      
      private AnswerOption(char c, String ans)
      {
         setLayout(new BorderLayout());
         choice = c;
         
         button = new JRadioButton(c + ": " + ans);
         button.setFont(GClickerModel.bigFont);
         
         data = "?%";
         stat = new JLabel(data);
         stat.setOpaque(true);
         stat.setFont(GClickerModel.monoSpaceBig);
         stat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
         stat.setBackground(Color.LIGHT_GRAY);
         
         filler = new JLabel();
         filler.setFont(GClickerModel.monoSpaceBig);
         setFiller();
         
         add(button, BorderLayout.NORTH);
         add(stat, BorderLayout.WEST);
         add(filler, BorderLayout.CENTER);
      }
      
      private void setFiller()
      {
         String fillerText = "";
         for (int i = stat.getText().length(); i < MAX_WIDTH; i++)
            fillerText = fillerText.concat(" ");
            
         filler.setText(fillerText);
      }
      
      public void updateStat(int numSelected, int total)
      {
         int width;
         String percent;
         
         if (total > 0)
         {
            width = (numSelected * MAX_WIDTH) / total;
            percent = (100 * numSelected) / total + "%";
         }
         else
         {
            width = 0;
            percent = "0%";
         }
            
         for (int i = percent.length(); i < width; i++)
            percent = percent.concat(" ");
         
         stat.setText(percent);
         setFiller();
         
         if (isSelected())
            stat.setBackground(Color.GREEN);
         else
            stat.setBackground(Color.LIGHT_GRAY);
      }
      
      public boolean isSelected() { return button.isSelected(); }
      public char getChoice() { return choice; }
      public JRadioButton getButton() { return button; }
   }
}