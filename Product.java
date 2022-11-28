import java.io.*;

public class Product implements Serializable 
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private int quantity;
  private double price;

  public Product(String name, int quantity, double price)
  {
    id = (ProductIdServer.instance()).generateId();
    this.name = name;
    this.quantity = quantity;
	this.price = price;
  }
  public String getId() 
  {
    return id;
  }
  public String getName() 
  {
    return name;
  }
  public int getQuantity() 
  {
    return quantity;
  }
  public double getPrice() 
  {
    return price;
  }
  public void setId(String newId) 
  {
    id = newId;
  }
  public void setName(String newName) 
  {
    name = newName;
  }
  public void setQuantity(int newQuantity) 
  {
    quantity = newQuantity;
  }
  public void setPrice(double newPrice) 
  {
    price = newPrice;
  } 
  public Boolean equals(String id) 
  {
    return this.id.equals(id);
  }
  public String toString() 
  {
  return "[Product's Id: " + id + " Product's name: " + name + " Product's quantity: " + quantity + " Product's price: " + price + "]";
  }
}
