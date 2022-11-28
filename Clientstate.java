import java.util.*;
import java.text.*;
import java.io.*;
public class Clientstate extends WareState 
{
  private static Clientstate clientstate;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int SHOW_CLIENTS = 3;
  private static final int SHOW_PRODUCTS = 4;
  private static final int SHOW_TRANSACTIONS = 18;
  private static final int ADD_TO_CART = 5;
  private static final int DISPLAY_CART = 6;
  private static final int EMPTY_CART = 7;
  private static final int PLACE_ORDER = 8;
  private static final int SHOW_WAITLIST = 13;
  private static final int HELP = 25;
  
  private Clientstate() 
  {
    warehouse = Warehouse.instance();
  }

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

  public void help() 
  {
	System.out.println("Successfully loaded into the CLIENT STATE");  
    System.out.println("Enter a number between 0 and 24 as explained below:");	
    System.out.println(EXIT + " to Exit\n");
    System.out.println(SHOW_CLIENTS + " to print clients");
    System.out.println(SHOW_PRODUCTS + " to print products");
    System.out.println(SHOW_TRANSACTIONS + " to display a list of a client's transactions");
    System.out.println(ADD_TO_CART + " to add product to the shopping cart");
	System.out.println(DISPLAY_CART + " to display client's shopping cart");
	System.out.println(EMPTY_CART + " to empty client's shopping cart");
	System.out.println(SHOW_WAITLIST + " to display the waitlist");
	System.out.println(PLACE_ORDER + " to place an order");
    System.out.println(HELP + " for help");
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
  


  public void process() 
  {
    int command;
    help();
    while ((command = getCommand()) != EXIT) 
	{
      switch (command) { 
        case SHOW_CLIENTS:				showClients();        
										break;
        case SHOW_PRODUCTS:				showProducts(); 
										break;									
		case SHOW_TRANSACTIONS:			showTransactions();   
										break;								
		case ADD_TO_CART:				addToCart();
										break;   
		case DISPLAY_CART:				displayCart();
										break;
		case EMPTY_CART:				emptyCart();          
										break;
		case PLACE_ORDER:				placeOrder();         
										break;
		case SHOW_WAITLIST:				showWaitlist();        
										break;			
        case HELP:              		help();
										break;
      }
    }
    logout();
  }

  public void run() 
  {
    process();
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
 
}
