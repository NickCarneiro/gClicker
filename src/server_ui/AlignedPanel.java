package server_ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/** This is a specific implementation of a JPanel. particularly, it's a JPanel
*   with a BoxLayout that will align everything relative to the X-Axis based on 
*   a justification specified in the constructor. This alignment can be "LEFT"
*   "CENTER" or "RIGHT". Also, there is functionality provided through another
*   constructor that allows space for indentation. For instance, if left 
*   justification is selected, there will be an indentation of the amount of
*   pixels specified in the constructor from the left. The opposite goes for
*   right justification. indentation is ignored if justification is center.
*
*   NOTE: This panel only aligns all the COMPONENTS given to it. Therefore, if,
*         for instance, a JPanel is added to this panel that is not left/right
*         justified, it will not appear justifed when displayed in this panel.
*         Only the farthest sides of that panel would be justified and aligned
*         with the other components in this panel.
**/

public class AlignedPanel extends JPanel
{
   public static String LEFT = BorderLayout.WEST;
   public static String CENTER = BorderLayout.CENTER;
   public static String RIGHT = BorderLayout.EAST;

   private String alignment;
   private int indentation;

   public AlignedPanel(String side)
   {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

      if(side.equals("LEFT"))
         alignment = LEFT;
      else if(side.equals("CENTER"))
         alignment = CENTER;
      else if(side.equals("RIGHT"))
         alignment = RIGHT;
      else
         alignment = side;

      indentation = 0;
   }

   public AlignedPanel(String side, int space)
   {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      alignment = side;
      indentation = space;
   }

   public Component add(Component aComp)
   {
      JPanel newPanel = new JPanel(new BorderLayout());
      JPanel innerPanel = new JPanel(new BorderLayout());

      JPanel boxPanel = new JPanel();
      boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));

         //if alignment is "CENTER", indention is ignored
      if((indentation <= 0) || (alignment.equals(CENTER)))
      {
         boxPanel.add(Box.createHorizontalGlue());
         boxPanel.add(aComp);
         boxPanel.add(Box.createHorizontalGlue());
      }
      else if(alignment.equals(RIGHT))
      {
         boxPanel.add(aComp);
         boxPanel.add(Box.createHorizontalStrut(indentation));
      }
      else
      {
         boxPanel.add(Box.createHorizontalStrut(indentation));
         boxPanel.add(aComp);
      }

      innerPanel.add(boxPanel, alignment);
      newPanel.add(innerPanel, BorderLayout.NORTH);
      super.add(newPanel);

      return this;
   }

   /**This method enables the ability to use a one-time temporary indention
    * different from the default indentation
    */
   public Component add(Component aComp, int tempIndent)
   {
      int temp;
      Component returned;
      
      temp = indentation;
      indentation = tempIndent;
      returned = add(aComp);
      indentation = temp;

      return returned;
   }      

   public Component add(Component aComp, String tempAlign)
   {
      String temp;
      Component returnMe;

      temp = alignment;
      if(tempAlign.equals("LEFT"))
         alignment = LEFT;
      else if(tempAlign.equals("CENTER"))
         alignment = CENTER;
      else if(tempAlign.equals("RIGHT"))
         alignment = RIGHT;
      else
         alignment = tempAlign;

      returnMe = add(aComp);
      alignment = temp;

      return returnMe;
   }

   public Component add(Component aComp, String tempAlign, int tempIndent)
   {
      int temp;
      Component returned;
      
      temp = indentation;
      indentation = tempIndent;
      returned = add(aComp, tempAlign);
      indentation = temp;

      return returned;
   }

   public void complete()
   {
      super.add(Box.createVerticalGlue());
   }
   
   public void setAlignment(String side)
   {
      if(side.equals("LEFT"))
         alignment = LEFT;
      else if(side.equals("CENTER"))
         alignment = CENTER;
      else if(side.equals("RIGHT"))
         alignment = RIGHT;
      else
         alignment = side;
   }

   public void setIndentation(int amount)
   {
      indentation = amount;
   }
}