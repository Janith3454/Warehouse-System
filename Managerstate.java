import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.List;

public class Managerstate extends WareState implements ActionListener 
{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private UserInterface context;
  private static Managerstate instance;
  private static final int EXIT = 0;
  private static final int HELP = 4;
  
  private Managerstate() 
  {
      super();
      warehouse = Warehouse.instance();
  }
  
  private JFrame frame;
  private AbstractButton addproductsBtn, clerkmenuBtn, recieveShipmentBtn, logoutBtn;

  public static Managerstate instance() 
  {
    if (instance == null) 
	{
      instance = new Managerstate();
    }
    return instance;
  }
  
  public void actionPerformed(ActionEvent event) 
   {
      if (event.getSource().equals(this.addproductsBtn))
        this.addProducts();
      else if (event.getSource().equals(this.clerkmenuBtn))
        this.clerkmenu(); 
      else if (event.getSource().equals(this.recieveShipmentBtn))
        this.recieveShipment();        
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
	
   public void run() 
  {
    frame = UserInterface.instance().getFrame();
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new FlowLayout());
    addproductsBtn = new JButton("Add products");
    clerkmenuBtn = new JButton("Change to |SALESCLERK STATE|");
    recieveShipmentBtn = new JButton("Receive a shipment");
	logoutBtn = new JButton("Log out");
    addproductsBtn.addActionListener(this);
    clerkmenuBtn.addActionListener(this);
    recieveShipmentBtn.addActionListener(this);
	logoutBtn.addActionListener(this);
    frame.getContentPane().add(this.addproductsBtn);
    frame.getContentPane().add(this.clerkmenuBtn);
    frame.getContentPane().add(this.recieveShipmentBtn);
	frame.getContentPane().add(this.logoutBtn);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    // frame.repaint();
    frame.toFront();
    frame.requestFocus();
  }
}
