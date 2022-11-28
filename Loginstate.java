import java.util.*;
import java.text.*;
import java.io.*;
public class Loginstate extends WareState{
  private static final int CLERK_LOGIN = 0;
  private static final int MANAGER_LOGIN = 1;
  private static final int CLIENT_LOGIN = 2;
  private static final int EXIT = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private UserInterface context;
  private static Loginstate instance;
  private Loginstate() {
      super();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new Loginstate();
    }
    return instance;
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" ));
        if (value <= EXIT && value >= CLERK_LOGIN) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
 
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  private void clerk(){
    (UserInterface.instance()).setLogin(UserInterface.IsClerk);
    (UserInterface.instance()).changeState(0);
  }
  
  private void manager(){
    (UserInterface.instance()).setLogin(UserInterface.IsManager);
    (UserInterface.instance()).changeState(1);
  }

  private void client(){
    String clientID = getToken("Please input the client id: ");
    if (Warehouse.instance().searchClientship(clientID) != null){
      (UserInterface.instance()).setLogin(UserInterface.IsClient);
      (UserInterface.instance()).setClient(clientID);      
      (UserInterface.instance()).changeState(2);
    }
    else 
      System.out.println("Invalid client id.");
  } 

  public void process() {
    int command;
    System.out.println("Successfully loaded into the LOGIN STATE\n"+
						"Please input 0 to login as Clerk\n"+ 
                        "input 1 to login as Manager\n" +
						"input 2 to login as Client\n" +
                        "input 3 to exit the System\n");     
    while ((command = getCommand()) != EXIT) {

      switch (command) {
        case CLERK_LOGIN:       clerk();
                                break;
        case MANAGER_LOGIN:     manager();
                                break;
		case CLIENT_LOGIN:       client();
                                break;
        default:                System.out.println("Invalid choice");
                                
      }
      System.out.println("Please input 0 to login as Clerk\n"+ 
                        "input 1 to login as Manager\n" +
						"input 2 to login as Client\n" +
                        "input 3 to exit the system\n"); 
    }
    (UserInterface.instance()).changeState(3);
  }

  public void run() {
    process();
  }
}
