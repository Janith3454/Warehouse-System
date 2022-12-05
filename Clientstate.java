import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Clientstate extends WareState implements ActionListener
{
  private static Clientstate clientstate;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int HELP = 7;
  
  private Clientstate() 
  {
    warehouse = Warehouse.instance();
  }
  
  private JFrame frame;
  private AbstractButton showclientsBtn, showproductsBtn, showtransBtn, modifyShoppingBtn,
  placeorderBtn, showWaitlistBtn, logoutBtn;

  public static Clientstate instance() 
  {
    if (clientstate == null) 
	{
      return clientstate = new Clientstate();
    } 
	else 
	{
      return clientstate;
    }
  }
  
   public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.showclientsBtn))
        this.showClients();
      else if (event.getSource().equals(this.showproductsBtn))
        this.showProducts(); 
      else if (event.getSource().equals(this.showtransBtn))
        this.showTransactions();
      else if (event.getSource().equals(this.modifyShoppingBtn))
        this.modifyShoppingCart();
	  else if (event.getSource().equals(this.placeorderBtn))
        this.placeOrder(); 
      else if (event.getSource().equals(this.showWaitlistBtn))
        this.showWaitlist();
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
  
   public void modifyShoppingCart() {
        (UserInterface.instance()).changeState(4);
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

  public void showProducts() 
  {
      Iterator<Product> allProducts = warehouse.getProducts();
      while (allProducts.hasNext())
	  {
        Product product = allProducts.next();
        System.out.println(product.toString());
      }
  }

  public void showTransactions() 
  {
    Client client;
    String clientId = getToken("Enter client id to see transactions");
    client = warehouse.getClientById(clientId);
    if (client != null) 
	{
      System.out.println("Transaction List: ");
      Iterator<Transaction> transactions = warehouse.getTransactions(clientId);
      while (transactions.hasNext())
	  {
        System.out.println(transactions.next().toString());
    }
    } 
	else 
	{
      System.out.println("Could not find that client id");
    }
  }
  
   public void CartWishlist(WishItem wishlist)
  {  
	Client c = wishlist.getClient();
	String Cid = c.getClientId();
    Product p = wishlist.getProduct();
	int Q = wishlist.getQuantity();
    warehouse.addToCart(Cid, p, Q);
    System.out.println("Added to the shopping cart successfully");
  }
  
  public void CartWishlistNewQuantity(WishItem wishlist)
  {  
	Client c = wishlist.getClient();
	String Cid = c.getClientId();
    Product p = wishlist.getProduct();
	int Q = getNumber("Enter the new quantity to the wishlist");
    warehouse.addToCart(Cid, p, Q);
    System.out.println("Added to the shopping cart successfully");
  }
  

  public void placeOrder() 
  {
    Client client;
	WishItem wishlist;
    String clientId = getToken("Enter client id to place an order");
    client = warehouse.getClientById(clientId);
    if (client != null) 
	{
      System.out.println("Client found:");	  
	  wishlist = warehouse.getWishlistById(clientId);
	  
	  if(wishlist != null)
	  {
	  System.out.println("Wishlist found: \n");	  
	  System.out.println(wishlist); 
	  System.out.println("Please select an option");
	  System.out.println("Type 1 to leave on wishlist");
	  System.out.println("Type 2 to order the product with existing quantity");
	  System.out.println("Type 3 to order the product with a different quantity");
	  int choice = getNumber("Enter a number:");
		switch (choice) 
		{
			case 1:    		System.out.println("Your wishlist will remain the same");
									break;
			case 2:   	    CartWishlist(wishlist);
									break;
			case 3:  		CartWishlistNewQuantity(wishlist);
									break;
		}
	  }	
      Iterator<ShoppingCartItem> cartIterator = client.getShoppingCart().getShoppingCartProducts();
      if (cartIterator.hasNext()) 
	  {
        System.out.println("Shopping Cart Total: $" + client.getShoppingCart().getTotalPrice());
        if(yesOrNo("Are you sure you wish to place an order?")) 
		{
          if(warehouse.placeOrder(clientId)) 
		  {
            System.out.println("Order placed, total price charged to client's balance,");
            System.out.println("invoice generated, and shopping cart has been emptied.");
          } 
		  else 
		  {
            System.out.println("Unable to place order");
          }
          }
		  else 
		  {
            System.out.println("Canceled, order was not placed");
          }
        } 
		else 
		{
          System.out.println("Shopping cart is empty, unable to place order");
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
  

  public void logout()
  {
    if ((UserInterface.instance()).getLogin() == UserInterface.IsClerk)
        { 
         (UserInterface.instance()).changeState(1); 
        }
    else if (UserInterface.instance().getLogin() == UserInterface.IsClient)
       {  
        (UserInterface.instance()).changeState(0); 
       }
	else if (UserInterface.instance().getLogin() == UserInterface.IsManager)
       {  
        (UserInterface.instance()).changeState(2); 
       }
    else 
       (UserInterface.instance()).changeState(3); // exit code 2, indicates error
  }
  
  public void run() 
  {
    frame = UserInterface.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    showclientsBtn = new JButton("Show Clients");
    showproductsBtn = new JButton("Show Products");
    showtransBtn = new JButton("Display a list of a client's transactions");
    modifyShoppingBtn = new JButton("Change to |MODIFY SHOPPINGCART STATE|");
	placeorderBtn = new JButton("Place an order");
	showWaitlistBtn = new JButton("Display the waitlist");
	logoutBtn = new JButton("Log out");
    showclientsBtn.addActionListener(this);
    showproductsBtn.addActionListener(this);
    showtransBtn.addActionListener(this);
    modifyShoppingBtn.addActionListener(this);
	placeorderBtn.addActionListener(this);
	showWaitlistBtn.addActionListener(this);
	logoutBtn.addActionListener(this);
    frame.getContentPane().add(this.showclientsBtn);
    frame.getContentPane().add(this.showproductsBtn);
    frame.getContentPane().add(this.showtransBtn);
    frame.getContentPane().add(this.modifyShoppingBtn);
	frame.getContentPane().add(this.placeorderBtn);
	frame.getContentPane().add(this.showWaitlistBtn);
	frame.getContentPane().add(this.logoutBtn);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    // frame.repaint();
    frame.toFront();
    frame.requestFocus();
  }
 
}
