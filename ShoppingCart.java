import java.util.*;
import java.io.*;

public class ShoppingCart implements Serializable 
{
  private static final long serialVersionUID = 1L;
  private List<ShoppingCartItem> cart;

  public ShoppingCart()
  {
    cart = new LinkedList<ShoppingCartItem>();
  }

  public boolean insertProductToCart(Product product, int quantity)
  {
    ShoppingCartItem item = new ShoppingCartItem(product, quantity);
    cart.add(item);
    return true;
  }
  
  public Iterator<ShoppingCartItem> getShoppingCartProducts() 
  {
    return cart.iterator();
  }

  public double getTotalPrice() 
  {
    double totalPrice = 0;
    Iterator<ShoppingCartItem> cartIterator = cart.iterator();

    while (cartIterator.hasNext())
	{
      ShoppingCartItem item = cartIterator.next();
      totalPrice += (item.getProduct().getPrice() * item.getQuantity());
    }
    
    return totalPrice;
  }
  public String toString() 
  {
    return cart.toString();
  }
}
