public abstract class WareState {
  protected static UserInterface context;
  protected WareState() {
    //context = UserInterface.instance();
  }
  public abstract void run();
}
