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
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
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
      } 
	  catch (IOException ioe) 
	  {
        System.exit(0);
      }
    } 
	while (true);
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
  {currentClient = code;}

  public void setClient(String mID)
  { clientID = mID;}

  public int getLogin()
  { return currentClient;}

  public String getClient()
  { return clientID;}

  private UserInterface() 
  { //constructor
    System.out.println("In UserInterface constructor");
    if (yesOrNo("Look for saved data and use it?")) 
	{
      retrieve();
    } else 
	{
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WareState[4];
    states[0] = Clerkstate.instance();
	states[1] = Managerstate.instance();
    states[2] = Clientstate.instance(); 
    states[3]=  Loginstate.instance();
    nextState = new int[4][4];
    nextState[0][0] = 3;nextState[0][1] = 1;nextState[0][2] = 2;nextState[0][3] = -2; //clerk
    nextState[1][0] = 3;nextState[1][1] = 0;nextState[1][2] = -2;nextState[1][3] = 3; //manager
    nextState[2][0] = 3;nextState[2][1] = 0;nextState[2][2] = 1;nextState[2][3] = -2; //client
	nextState[3][0] = 0;nextState[3][1] = 1;nextState[3][2] = 2;nextState[3][3] = -1; //login
    currentState = 3;
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
   System.out.println(" GOODBYE! \n "); System.exit(0);
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
