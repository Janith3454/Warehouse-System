import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class ModifyShoppingCartstate extends WareState implements ActionListener 
{
  private static ModifyShoppingCartstate modifyshoppingcart;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int HELP = 4;
  
  private ModifyShoppingCartstate() 
  {
    warehouse = Warehouse.instance();
  }
  
  private JFrame frame;
  private AbstractButton addtocartBtn, displaycartBtn, emptycartBtn, logoutBtn;

  public static ModifyShoppingCartstate instance() 
  {
    if (modifyshoppingcart == null) 
	{
      return modifyshoppingcart = new ModifyShoppingCartstate();
    } 
	else 
	{
      return modifyshoppingcart;
    }
  }
  
   public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.addtocartBtn))
        this.addToCart();
      else if (event.getSource().equals(this.displaycartBtn))
        this.displayCart();         
      else if (event.getSource().equals(this.emptycartBtn))
        this.emptyCart();         
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
  
  public void addToCart() 
  {
    Client client;
    Product product;
    String clientId = getToken("Enter client id to add to their shopping cart");
    client = warehouse.getClientById(clientId);
    if (client != null) 
	{
      System.out.println("Client found:");
      System.out.println(client);
      do 
	  {
        String productId = getToken("Enter product id");
        product = warehouse.getProductById(productId);
        if(product != null) 
		{
          System.out.println("Product found:");
          System.out.println(product);
          int productQuantity = getNumber("Enter enter quantity");
          warehouse.addToCart(clientId, product, productQuantity);
        } else 
		{
          System.out.println("Could not find that product id");
        }
        if (!yesOrNo("Add another product to the shopping cart?")) 
		{
          break;
        }
      } 
	  while (true);
    } 
	else 
	{
      System.out.println("Could not find that client id");
    }
  }

  public void displayCart() 
  {
    Client client;
    String clientId = getToken("Enter client id to view to their shopping cart");
    client = warehouse.getClientById(clientId);
    if (client != null) 
	{
      System.out.println("Client found:");
      System.out.println(client);
      System.out.println("Shopping Cart:");
      warehouse.displayCart(clientId);
    } 
	else 
	{
      System.out.println("Could not find that client id");
    }
  }

  public void emptyCart() 
  {
    Client client;
    String clientId = getToken("Enter client id to empty to their shopping cart");
    client = warehouse.getClientById(clientId);
    if (client != null) 
	{
      System.out.println("Client found:");
      System.out.println(client);
      if(yesOrNo("Are you sure you wish to empty the shopping cart?")) 
	  {
        warehouse.emptyCart(clientId);
        System.out.println("Shopping Cart has been emptied");
      } 
	  else 
	  {
        System.out.println("Cancelled, shopping cart was not emptied");
      }
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
    addtocartBtn = new JButton("Add product to the shopping cart");
    displaycartBtn = new JButton("Print the shopping cart");
    emptycartBtn = new JButton("Empty the shopping cart");
	logoutBtn = new JButton("Log out");
    addtocartBtn.addActionListener(this);
    displaycartBtn.addActionListener(this);
    emptycartBtn.addActionListener(this);
	logoutBtn.addActionListener(this);
    frame.getContentPane().add(this.addtocartBtn);
    frame.getContentPane().add(this.displaycartBtn);
    frame.getContentPane().add(this.emptycartBtn);
	frame.getContentPane().add(this.logoutBtn);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    // frame.repaint();
    frame.toFront();
    frame.requestFocus();
  }
}
