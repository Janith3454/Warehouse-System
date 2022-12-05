import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Loginstate extends WareState implements ActionListener
{
  private static Loginstate instance;
 
  private JFrame frame;
  private AbstractButton clientButton, exitButton, clerkButton, managerButton;

  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private UserInterface context;
  
  private Loginstate() 
  {
      super();
  }

  public static Loginstate instance() 
  {
    if (instance == null) 
	{
      instance = new Loginstate();
    }
    return instance;
  }
  
   public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.clientButton))
        this.client();
      else if (event.getSource().equals(this.exitButton))
        (UserInterface.instance()).changeState(3);
      else if (event.getSource().equals(this.clerkButton))
        this.clerk();
      else if (event.getSource().equals(this.managerButton))
        this.manager();
   }
	
  public void clear() 
  { 
    // clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());
  }

  public String getToken(String prompt) 
  {
    do 
	{
      try 
	  {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) 
		{
          return tokenizer.nextToken();
        }
      } 
	  catch (IOException ioe) 
	  {
        System.exit(0);
      }
    } 
	while (true);
  }
 
  private boolean yesOrNo(String prompt) 
  {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') 
	{
      return false;
    }
    return true;
  }

  private void clerk()
  {
    (UserInterface.instance()).setLogin(UserInterface.IsClerk);
    (UserInterface.instance()).changeState(0);
  }
  
  private void manager()
  {
    (UserInterface.instance()).setLogin(UserInterface.IsManager);
    (UserInterface.instance()).changeState(1);
  }

  private void client()
  {
    String clientID = JOptionPane.showInputDialog(frame, "Please enter the client ID: ");
        if (Warehouse.instance().searchClientship(clientID) != null) 
		{
           (UserInterface.instance()).setLogin(UserInterface.IsClient);
           (UserInterface.instance()).setClient(clientID);
           clear();
           (UserInterface.instance()).changeState(2);
        } 
		  else 
		  {
            JOptionPane.showMessageDialog(frame, "Invalid client id.");
          }
  }
  
  public void run() 
  {
    frame = UserInterface.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    clientButton = new JButton("Client");
    clerkButton = new JButton("Clerk");
    managerButton = new JButton("Manager");
    exitButton = new JButton("Exit");
    clientButton.addActionListener(this);
    clerkButton.addActionListener(this);
    managerButton.addActionListener(this);
    exitButton.addActionListener(this);
    frame.getContentPane().add(this.clientButton);
    frame.getContentPane().add(this.clerkButton);
    frame.getContentPane().add(this.managerButton);
    frame.getContentPane().add(this.exitButton);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    // frame.repaint();
    frame.toFront();
    frame.requestFocus();
  }

}
