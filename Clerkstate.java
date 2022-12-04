import java.util.*;
import java.text.*;
import java.io.*;
public class Clerkstate extends WareState 
{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private UserInterface context;
  private static Clerkstate instance;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int PRODUCTS_WAITLIST  = 16;
  private static final int SHOW_CLIENTS  = 3;
  private static final int SHOW_BALANCE = 15;
  private static final int MAKE_PAYMENT  = 17;
  private static final int SHOW_WAITLIST = 13;
  private static final int CLIENTMENU = 23;
  private static final int HELP = 25;
  
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

  public void help() 
  {
	System.out.println("Successfully loaded into the CLERK STATE");
    System.out.println("Enter a number between 0 and 25 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(PRODUCTS_WAITLIST + " to display products, stock, and waitlist amount");
    System.out.println(SHOW_CLIENTS + " to print clients");
    System.out.println(SHOW_BALANCE + " to display a client's balance");
    System.out.println(MAKE_PAYMENT + " to add to a client's balance");
	System.out.println(SHOW_WAITLIST + " to display the waitlist");
	System.out.println(CLIENTMENU + " to change to the client state");
    System.out.println(HELP + " for help");
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


  public void clientmenu()
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
 
  public void process() 
  {
    int command;
    help();
    while ((command = getCommand()) != EXIT) 
	{
      switch (command) {
        case ADD_CLIENT:          		addClient();
										break;
        case SHOW_CLIENTS:				showClients();        
										break;
        case PRODUCTS_WAITLIST:			showProductsWaitlist(); 
										break;					
        case SHOW_WAITLIST:				showWaitlist();        
										break;								
        case SHOW_BALANCE:				showBalance();         
										break;
        case MAKE_PAYMENT:				processPayment();	
										break;   
		case CLIENTMENU:          		clientmenu();
										break;								
        case HELP:						help();
										break;
      }
    }
    logout();
  }
  public void run() 
  {
    process();
  }
}
