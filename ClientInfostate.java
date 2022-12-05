import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ClientInfostate extends WareState implements ActionListener 
{
  private static ClientInfostate clientinfo;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int HELP = 3;
  
  private ClientInfostate() 
  {
    warehouse = Warehouse.instance();
  }
  
  private JFrame frame;
  private AbstractButton showclientsBtn, showbalanceBtn, logoutBtn;

  public static ClientInfostate instance() 
  {
    if (clientinfo == null) 
	{
      return clientinfo = new ClientInfostate();
    } 
	else 
	{
      return clientinfo;
    }
  }
  
  public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.showclientsBtn))
        this.showClients();
      else if (event.getSource().equals(this.showbalanceBtn))
        this.showBalance();       
      else if (event.getSource().equals(this.logoutBtn))
        this.logout();
   }
	
  public void clear() 
  { 
    // clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());
  }
  
  public String getToken(String prompt) 
  {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) 
		{
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) 
	  {
        System.exit(0);
      }
    } while (true);
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
  
  public int getNumber(String prompt) 
  {
    do 
	{
      try 
	  {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } 
	  catch (NumberFormatException nfe) 
	  {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  
  public int getCommand() 
  {
    do 
	{
      try 
	  {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) 
		{
          return value;
        }
      } 
	  catch (NumberFormatException nfe) 
	  {
        System.out.println("Enter a number");
      }
    } while (true);
  }
   
   public void showClients() 
  {
      Iterator<Client> allClients = warehouse.getClients();
      while (allClients.hasNext())
	  {
        Client client = allClients.next();
        System.out.println(client.toString());
      }
  }

 public void showBalance() 
  {
    Client result;
    String id = getToken("Enter client id to see balance");
    result = warehouse.getClientById(id);
    if (result != null) 
	{
      System.out.println("Current Balance: $" + result.getBalance());
    } 
	else 
	{
      System.out.println("Could not find that client id");
    }
  }

    public void logout()
  {
	if ((UserInterface.instance()).getLogin() == UserInterface.IsClerk)
        { 
         (UserInterface.instance()).changeState(0); 
        }
    else if (UserInterface.instance().getLogin() == UserInterface.IsClient)
       {  
        (UserInterface.instance()).changeState(1); 
       }
	else if (UserInterface.instance().getLogin() == UserInterface.IsManager)
       {  
        (UserInterface.instance()).changeState(1); 
       }
    else 
       (UserInterface.instance()).changeState(3);
  }
 
   public void run() 
  {
    frame = UserInterface.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    showclientsBtn = new JButton("Show all the clients in the system");
    showbalanceBtn = new JButton("Display a client's balance");
	logoutBtn = new JButton("Log out");
    showclientsBtn.addActionListener(this);
    showbalanceBtn.addActionListener(this);
	logoutBtn.addActionListener(this);
    frame.getContentPane().add(this.showclientsBtn);
    frame.getContentPane().add(this.showbalanceBtn);
	frame.getContentPane().add(this.logoutBtn);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    // frame.repaint();
    frame.toFront();
    frame.requestFocus();
  }
}
