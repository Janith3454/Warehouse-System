import java.io.Serializable;

public class WishItem implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private Client client;
    private Product product;
    private int quantity;

    public WishItem(Client c, Product p, int q) 
	{
		this.client = c;
        this.product = p;
        this.quantity = q;
    }

    public Client getClient() 
	{
        return client;
    }

    public Product getProduct()
	{
        return product;
    }

    public int getQuantity() 
	{
        return quantity;
    }

	public void setClient(Client newC) 
	{
		client = newC;
	}
	
	public void setProduct(Product newP) 
	{
		product = newP;
	}
	
    public void setQuantity(int newQuantity) 
	{
        quantity = newQuantity;
    }

    public String toString() 
	{
        return "Client: " + client.toString() + " Wishlist:" + " [Product info: " + product.toString() + " Quantity in Wishlist: " + quantity + "]" ;
    }
}
