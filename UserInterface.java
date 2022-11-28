import java.util.*;
import java.io.*;

public class UserInterface 
{
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int SHOW_CLIENTS = 3;
  private static final int SHOW_PRODUCTS = 4;
  private static final int ADD_TO_CART = 5;
  private static final int DISPLAY_CART = 6;
  private static final int EMPTY_CART = 7;
  private static final int PLACE_ORDER = 8;
  private static final int SHOW_ORDERS = 9;
  private static final int SHOW_INVOICES = 10;
  private static final int WAITLIST_ITEM = 11;
  private static final int WISHLIST_ITEM = 12;
  private static final int SHOW_WAITLIST = 13;
  private static final int SHOW_WISHLIST = 14;
  private static final int SHOW_BALANCE = 15;
  private static final int PRODUCTS_WAITLIST = 16;
  private static final int MAKE_PAYMENT = 17;
  private static final int SHOW_TRANSACTIONS = 18;
  private static final int SHOW_INVENTORY = 19;
  private static final int RECEIVE_SHIPMENT = 20;
  private static final int SAVE = 21;
  private static final int RETRIEVE = 22;
  private static final int HELP = 23;
  

  private UserInterface() 
  {
    if (yesOrNo("Look for saved data and use it?")) 
	{
      retrieve();
    } else 
	{
      warehouse = Warehouse.instance();
    }
  }

  public static UserInterface instance() 
  {
    if (userInterface == null) 
	{
      return userInterface = new UserInterface();
    } 
	else 
	{
      return userInterface;
    }
  }

  public void help() 
  {
    System.out.println("Enter a number between  0 and 23 as explained below:");
    System.out.println(EXIT + " to Exit");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to add products");
    System.out.println(SHOW_CLIENTS + " to print clients");
	System.out.println(SHOW_PRODUCTS + " to print products");
	System.out.println(ADD_TO_CART + " to add product to the shopping cart");
	System.out.println(DISPLAY_CART + " to display client's shopping cart");
	System.out.println(EMPTY_CART + " to empty client's shopping cart");
	System.out.println(PLACE_ORDER + " to place an order");
    System.out.println(SHOW_ORDERS + " to display all orders");
    System.out.println(SHOW_INVOICES + " to display all invoices");
	System.out.println(WAITLIST_ITEM + " to add to the waitlist");
	System.out.println(WISHLIST_ITEM + " to add to the wishlist");	
	System.out.println(SHOW_WAITLIST + " to display the waitlist");
	System.out.println(SHOW_WISHLIST + " to display the wishlist");
	System.out.println(SHOW_BALANCE + " to display a client's balance");
    System.out.println(PRODUCTS_WAITLIST + " to display products, stock, and waitlist amount");
	System.out.println(MAKE_PAYMENT + " to add to a client's balance");
	System.out.println(SHOW_TRANSACTIONS + " to display a list of a client's transactions");
	System.out.println(SHOW_INVENTORY + " to view the warehouse's inventory");
    System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
	System.out.println(SAVE + " to save");
	System.out.println(RETRIEVE+ " to retrieve");
    System.out.println(HELP + " for help");
  }

  private void save() 
  {
    if (warehouse.save()) 
	{
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    } else 
	{
      System.out.println(" There has been an error in saving \n" );
    }
  }

  private void retrieve() 
  {
    try 
	{
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) 
	  {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } 
	  else
		{
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } 
	catch(Exception cnfe) 
	{
      cnfe.printStackTrace();
    }
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

  public int getInt(String prompt) 
  {
    do 
	{
      try 
	  {
        String item = getToken(prompt);
        Integer num = Integer.parseInt(item);
        return num.intValue();
      } catch (NumberFormatException nfe) 
	  {
        System.out.println("Please input a number ");
      }
    } 
	while (true);
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
      } 
	  catch (NumberFormatException nfe) 
	  {
        System.out.println("Enter a number");
      }
    } 
	while (true);
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

  public void waitlistItem() 
  {
      String clientId = getToken("Enter existing client id");
      String productId = getToken("Enter exising product id");
      int quantity = getInt("Enter quantity to waitlist");
      boolean result = warehouse.waitlistItem(clientId, productId, quantity);
      if ( result ) 
	  {
          System.out.println("Successfully waitlisted items");
      } 
	  else 
	  {
          System.out.println("Could not waitlist item");

      }
  }
  
  public void wishlistItem() 
  {
      String clientId = getToken("Enter existing client id");
      String productId = getToken("Enter exising product id");
      int quantity = getInt("Enter quantity to wishlist");
      WishItem result = warehouse.wishlistItem(clientId, productId, quantity);
      if (result == null) 
	{
      System.out.println("Could not add to the wishlist");
    }
	System.out.println("Successfully added the wishlist. \n");
    System.out.println(result);
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
  
  public void showWishlist() 
  {
    Iterator<WishItem> wishlist = warehouse.getWishlist();
    while (wishlist.hasNext())
	{
      WishItem item = wishlist.next();
      System.out.println(item.toString());
    }
  }

  public void addProducts() 
  {
    Product result;
    do 
	{
      String name = getToken("Enter name");
      int quantity = getInt("Enter quantity");
      double price = getDouble("Enter price");
      result = warehouse.addProduct(name, quantity, price);
      if (result != null)
		  {
        System.out.println(result);
      } 
	  else 
	  {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more products?")) 
	  {
        break;
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

  public void showProducts() 
  {
      Iterator<Product> allProducts = warehouse.getProducts();
      while (allProducts.hasNext())
	  {
        Product product = allProducts.next();
        System.out.println(product.toString());
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
          int productQuantity = getInt("Enter enter quantity");
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
	int Q = getInt("Enter the new quantity to the wishlist");
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
	  int choice = getInt("Enter a number:");
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

  public void showOrders() 
  {
    Iterator<Order> allOrders = warehouse.getOrders();
    while (allOrders.hasNext())
	{
      Order order = allOrders.next();
      System.out.println(order.toString());
    }
  }

  public void showInvoices() 
  {
    Iterator<Invoice> allInvoices = warehouse.getInvoices();
    while (allInvoices.hasNext())
	{
      Invoice invoice = allInvoices.next();
      System.out.println(invoice.toString());
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
  
  public void showInventory() 
  {
    Iterator<InventoryItem> inventoryIterator = warehouse.getInventory();
    while (inventoryIterator.hasNext())
	{
      System.out.println(inventoryIterator.next().toString());
    }
  }

  public void recieveShipment() 
  {
    Product p;
    do 
	{
      String productId = getToken("Enter productId");
      p = warehouse.getProductById(productId);
      if(p != null) 
	  {
        int quantity = getInt("Enter quantity");
        List<WaitItem> waitlistedOrders = warehouse.getWaitItemsByProductId(productId);
        Iterator<WaitItem> waitlistedOrdersIterator = waitlistedOrders.iterator();
        while(waitlistedOrdersIterator.hasNext())
		{
          WaitItem waitItem = waitlistedOrdersIterator.next();
          System.out.println("Waitlist found:");
          System.out.println(waitItem.toString());
		  System.out.println("Please select an option");
		  System.out.println("Type 1 to leave on waitList");
		  System.out.println("Type 2 to Fill waitListed order");
	      System.out.println("Type 3 to Fill waitListed order with new quantity");
	      int choice = getInt("Enter a number:");
		  switch (choice) 
		  {
			case 1:    		System.out.println("Your waitlist will remain the same");
									break;
			case 2:   	    quantity -= waitItem.getQuantity();
							waitItem.setOrderFilled(true);
							System.out.println("Order filled.");
									break;
			case 3:  		quantity -= getInt("Enter new quantity to the waitlist");
							waitItem.setOrderFilled(true);
							System.out.println("Order filled.");
									break;
		  }
        }     
        warehouse.addToInventory(productId, quantity);
      } 
	  else 
	  {
        System.out.println("Product not found");
      }
      if (!yesOrNo("Receive another product?")) 
	  {
        break;
      }
    } 
	while(true);
  }

  public void process() 
  {
    int command;
    help();
    while ((command = getCommand()) != EXIT) 
	{
      switch (command) 
	  {
        case ADD_CLIENT:          		addClient();
										break;
        case ADD_PRODUCTS: 				addProducts();       
										break;
        case SHOW_CLIENTS:				showClients();        
										break;
        case SHOW_PRODUCTS:				showProducts(); 
										break;
        case DISPLAY_CART:				displayCart();
										break;
        case ADD_TO_CART:				addToCart();
										break;   
        case EMPTY_CART:				emptyCart();          
										break;
        case PLACE_ORDER:				placeOrder();         
										break;
        case SHOW_ORDERS:				showOrders();       
										break;
        case SHOW_INVOICES:				showInvoices();     
										break;
        case WAITLIST_ITEM:				waitlistItem();
										break;
		case WISHLIST_ITEM:				wishlistItem();
										break;								
        case SHOW_WAITLIST:				showWaitlist();        
										break;
		case SHOW_WISHLIST:				showWishlist();        
										break;								
        case SHOW_BALANCE:				showBalance();         
										break;
        case MAKE_PAYMENT:				processPayment();	
										break;     
        case SHOW_TRANSACTIONS:			showTransactions();   
										break;	
        case SHOW_INVENTORY:			showInventory();  
										break;
        case RECEIVE_SHIPMENT:			recieveShipment();
										break;							
        case SAVE:						save();    
										break;
		case RETRIEVE:					retrieve();
										break;
        case HELP:						help();
										break;
      }
    }
  }

  public static void main(String[] s) 
  {
     UserInterface.instance().process();
  }
}
