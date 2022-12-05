import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class UserInterface 
{
  private int currentState;
  private static Warehouse warehouse;
  private static UserInterface context;
  private int currentClient;
  private String clientID;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  public static final int IsClerk = 0;
  public static final int IsManager = 1;
  public static final int IsClient = 2;
  private WareState[] states;
  private int[][] nextState;
  
  private static JFrame WarehouseFrame;

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
    } 
	while (true);
  }
  
   private boolean yesOrNo(String prompt) 
   {
      String more = JOptionPane.showInputDialog(WarehouseFrame, prompt + " \n(Y|y)[es] or anything else for no");
      if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') 
	  {
        return false;
      }
      return true;
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

  public void setLogin(int code)
  {
	currentClient = code;
  }

  public void setClient(String mID)
  { 
	clientID = mID;
  }

  public int getLogin()
  { 
	return currentClient;
  }

  public String getClient()
  { 
	return clientID;
  }
  
  public JFrame getFrame() 
  {
    return WarehouseFrame;
  }


  private UserInterface() 
  { //constructor
    System.out.println("In UserInterface constructor");
    if (yesOrNo("Look for saved data and use it?")) 
	{
      retrieve();
    } 
	else 
	{
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WareState[6];
    states[0] = Clerkstate.instance();
	states[1] = Managerstate.instance();
    states[2] = Clientstate.instance(); 
    states[3]=  Loginstate.instance();
	states[4]=  ModifyShoppingCartstate.instance();
	states[5]=  ClientInfostate.instance();
    nextState = new int[6][6];
	
	nextState[0][0] = 3; // CLERK STATE
    nextState[0][1] = 1; // CLERK STATE
    nextState[0][2] = 2; // CLERK STATE
    nextState[0][3] = -2; // CLERK STATE
    nextState[0][4] = 4; // CLERK STATE
	nextState[0][5] = 5; // CLERK STATE

    nextState[1][0] = 3; //MANAGER STATE
    nextState[1][1] = 0; //MANAGER STATE
    nextState[1][2] = -2; //MANAGER STATE
    nextState[1][3] = 3; //MANAGER STATE
    nextState[1][4] = -2; //MANAGER STATE
	nextState[1][5] = 0; //MANAGER STATE

    nextState[2][0] = 3; //CLIENT STATE
    nextState[2][1] = 0; //CLIENT STATE
    nextState[2][2] = 1; //CLIENT STATE
    nextState[2][3] = -2; //CLIENT STATE
    nextState[2][4] = 4; //CLIENT STATE
	nextState[2][5] = 5; //CLIENT STATE

    nextState[3][0] = 0; //LOGIN STATE
    nextState[3][1] = 1; //LOGIN STATE
    nextState[3][2] = 2; //LOGIN STATE
    nextState[3][3] = -1; //LOGIN STATE
    nextState[3][4] = 4; //LOGIN STATE
	nextState[3][5] = 5; //LOGIN STATE

    nextState[4][0] = 2; //MODIFYSHOPPINGCART STATE
    nextState[4][1] = -2; //MODIFYSHOPPINGCART STATE
    nextState[4][2] = -2; //MODIFYSHOPPINGCART STATE
    nextState[4][3] = -2; //MODIFYSHOPPINGCART STATE
    nextState[4][4] = -1; //MODIFYSHOPPINGCART STATE
	nextState[4][5] = 5; //MODIFYSHOPPINGCART STATE
	
	nextState[5][0] = 0; //CLIENTINFO STATE
    nextState[5][1] = -2; //CLIENTINFO STATE
    nextState[5][2] = -2; //CLIENTINFO STATE
    nextState[5][3] = -2; //CLIENTINFO STATE
    nextState[5][4] = -1; //CLIENTINFO STATE
	nextState[5][5] = -1; //CLIENTINFO STATE

    currentState = 3;
    WarehouseFrame = new JFrame("Warehouse GUI");
    WarehouseFrame.addWindowListener(new WindowAdapter() 
	{
      public void windowClosing(WindowEvent e) 
	  {
        System.exit(0);
      }
    });
	
    WarehouseFrame.setSize(500, 500);
    WarehouseFrame.setLocation(500, 500);	
  }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      terminate();
    //System.out.println("current state " + currentState + " \n \n ");
    states[currentState].run();
  }

  private void terminate()
  {
   if (yesOrNo("Save data?")) {
      if (warehouse.save()) {
         System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
       } else {
         System.out.println(" There has been an error in saving \n" );
       }
     }
    JOptionPane.showMessageDialog(WarehouseFrame, "Good Bye!");
  }

  public static UserInterface instance() {
    if (context == null) {
       System.out.println("calling constructor");
       context = new UserInterface();
    }
    return context;
  }

  public void process(){
    states[currentState].run();
  }
  
  public static void main (String[] args){
    UserInterface.instance().process(); 
  }
}
