import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Clerkstate extends WareState implements ActionListener 
{	
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private UserInterface context;
  private static Clerkstate instance;
  private static final int EXIT = 0;
  private static final int HELP = 7;
  
  private JFrame frame;
  private AbstractButton addClientBtn, prodsWishlistBtn, clientInfoStateBtn, makePaymentBtn, showWaitlistBtn,
          becomeClientStateBtn, logoutBtn;

 
  private Clerkstate()
  {
      super();
      warehouse = Warehouse.instance();
  }

  public static Clerkstate instance() 
  {
    if (instance == null) 
	{
      instance = new Clerkstate();
    }
    return instance;
  }
  
   public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.addClientBtn))
        this.addClient();
      else if (event.getSource().equals(this.prodsWishlistBtn))
        this.showProductsWaitlist();
      else if (event.getSource().equals(this.clientInfoStateBtn))
        this.clientMenu();
      else if (event.getSource().equals(this.makePaymentBtn))
        this.processPayment();
      else if (event.getSource().equals(this.showWaitlistBtn))
        this.showWaitlist();
	  else if (event.getSource().equals(this.becomeClientStateBtn))
        this.clientState();
      else if (event.getSource().equals(this.logoutBtn))
        this.logout();
    }

  public void clear() 
   { // clean up stuff
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
      } catch (NumberFormatException nfe) 
	  {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  
   public double getDouble(String prompt) 
  {
    do {
      try 
	  {
        String item = getToken(prompt);
        Double num = Double.parseDouble(item);
        return num.doubleValue();
      } 
	  catch (NumberFormatException nfe) 
	  {
        System.out.println("Please input a number ");
      }
    } 
	while (true);
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
      } catch (NumberFormatException nfe) 
	  {
        System.out.println("Enter a number");
      }
    } while (true);
  }
  
    public void clientMenu() {
        (UserInterface.instance()).changeState(5);
    }

 
  public void addClient() 
  {
    String name = getToken("Enter client's name");
    String address = getToken("Enter address");
	String phone = getToken("Enter client's phone number"); 
    Client result = warehouse.addClient(name, address, phone);
    if (result == null) 
	{
      System.out.println("Could not add member");
    }
    System.out.println(result);
  }

	public void showProductsWaitlist() 
  {
    int amt = 0;
    Iterator<Product> allProducts = warehouse.getProducts();
    while(allProducts.hasNext()) 
	{
      Product tempProduct = allProducts.next();
      Iterator<WaitItem> waitList = warehouse.getWaitlist();
      while(waitList.hasNext())
		  {
        WaitItem tempWaitItem = waitList.next();
        if(tempProduct == tempWaitItem.getProduct()) 
		{
          amt += tempWaitItem.getQuantity();
        }
      }
      System.out.println(tempProduct.toString() + " " + amt);
      amt = 0;
    }
  }


  public void processPayment() 
  {
    Client client;

    String clientId = getToken("Enter client id to make a payment");
    client = warehouse.getClientById(clientId);
    if (client != null)
		{      
      Double paymentAmount = getDouble("Enter payment amount");
      if(warehouse.makePayment(clientId, paymentAmount))
		{
        System.out.println("Payment Successful, new balance: " + client.getBalance());
      }
    } 
	else 
	{
      System.out.println("Could not find that client id");
    }
  }
  
  
    public void showWaitlist() 
  {
    Iterator<WaitItem> waitlist = warehouse.getWaitlist();
    while (waitlist.hasNext())
	{
      WaitItem item = waitlist.next();
      System.out.println(item.toString());
    }
  }


  public void clientState()
  {
    String clientID = getToken("Please input the client id: ");
    if (Warehouse.instance().searchClientship(clientID) != null){
      (UserInterface.instance()).setClient(clientID);      
      (UserInterface.instance()).changeState(2);
    }
    else 
      System.out.println("Invalid client id."); 
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
      addClientBtn = new JButton("Add a Client");
      prodsWishlistBtn = new JButton("Display products, stock, and waitlist amount");
      clientInfoStateBtn = new JButton("Change to |CLIENT INFO STATE|");
      makePaymentBtn = new JButton("Add to a client's balance");
      showWaitlistBtn = new JButton("Display the waitlist");
      becomeClientStateBtn = new JButton("Change to |CLIENT STATE|");
      logoutBtn = new JButton("Log out");
      addClientBtn.addActionListener(this);
      prodsWishlistBtn.addActionListener(this);
      clientInfoStateBtn.addActionListener(this);
      makePaymentBtn.addActionListener(this);
      showWaitlistBtn.addActionListener(this);
      becomeClientStateBtn.addActionListener(this);
      logoutBtn.addActionListener(this);
      frame.getContentPane().add(this.addClientBtn);
      frame.getContentPane().add(this.prodsWishlistBtn);
      frame.getContentPane().add(this.clientInfoStateBtn);
      frame.getContentPane().add(this.makePaymentBtn);
      frame.getContentPane().add(this.showWaitlistBtn);
      frame.getContentPane().add(this.becomeClientStateBtn);
      frame.getContentPane().add(this.logoutBtn);
      frame.setVisible(true);
      frame.paint(frame.getGraphics());
	  // frame.repaint();
      frame.toFront();
      frame.requestFocus();
   }

}
