import java.util.*;
import java.text.*;
import java.io.*;
public class Managerstate extends WareState 
{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private UserInterface context;
  private static Managerstate instance;
  private static final int EXIT = 0;
  private static final int ADD_PRODUCTS = 2;
  private static final int CLERKMENU = 24;
  private static final int RECEIVE_SHIPMENT = 20;
  private static final int HELP = 25;
  
  private Managerstate() 
  {
      super();
      warehouse = Warehouse.instance();
  }

  public static Managerstate instance() 
  {
    if (instance == null) 
	{
      instance = new Managerstate();
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

  
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() 
  {
	System.out.println("Successfully loaded into the MANAGER STATE");  
    System.out.println("Enter a number between 0 and 12 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_PRODUCTS + " to add products");
    System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
	System.out.println(CLERKMENU + " to become a salesclerk");
    System.out.println(HELP + " for help");
  }

  public void addProducts() 
  {
    Product result;
    do 
	{
      String name = getToken("Enter name");
      int quantity = getNumber("Enter quantity");
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


  public void recieveShipment() 
  {
    Product p;
    do 
	{
      String productId = getToken("Enter productId");
      p = warehouse.getProductById(productId);
      if(p != null) 
	  {
        int quantity = getNumber("Enter quantity");
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
	      int choice = getNumber("Enter a number:");
		  switch (choice) 
		  {
			case 1:    		System.out.println("Your waitlist will remain the same");
									break;
			case 2:   	    quantity -= waitItem.getQuantity();
							waitItem.setOrderFilled(true);
							System.out.println("Order filled.");
									break;
			case 3:  		quantity -= getNumber("Enter new quantity to the waitlist");
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


  public void logout()
  {
    (UserInterface.instance()).changeState(0); // exit with a code 0
  }
 
	
   public void clerkmenu()
  {     
    (UserInterface.instance()).changeState(1);
  }
	
 
  public void process() 
  {
    int command;
    help();
    while ((command = getCommand()) != EXIT) 
	{
      switch (command) 
	  {
        case ADD_PRODUCTS:          	addProducts();
										break;
        case RECEIVE_SHIPMENT:			recieveShipment();        
										break;
		case CLERKMENU:					clerkmenu();
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
