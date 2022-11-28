import java.util.*;
import java.io.*;

public class Wishlist implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private List<WishItem> wishItems = new LinkedList<WishItem>();
    private static Wishlist wishlist;

    public static Wishlist instance() 
	{
        if (wishlist == null) 
		{
            return (wishlist = new Wishlist());
        } 
		else 
		{
            return wishlist;
        }
    }

    public boolean insertWishItem(WishItem wishItem)
	{
        wishItems.add(wishItem);
        return true;
    }

    public Iterator<WishItem> getWishlists() {
        return wishItems.iterator();
    }

    private void writeObject(java.io.ObjectOutputStream output) 
	{
        try {
          output.defaultWriteObject();
          output.writeObject(wishlist);
        } catch(IOException ioe) {
          ioe.printStackTrace();
        }
      }
      private void readObject(java.io.ObjectInputStream input) 
	  {
        try 
		{
          if (wishlist != null) 
		  {
            return;
          } 
		  else 
		  {
            input.defaultReadObject();
            if (wishlist == null) 
			{
                wishlist = (Wishlist) input.readObject();
            } 
			else 
			{
                input.readObject();
            }
          }
        } 
		catch(IOException ioe) 
		{
          ioe.printStackTrace();
        } 
		catch(ClassNotFoundException cnfe) 
		{
          cnfe.printStackTrace();
        }
      }

    public String toString() 
	{
        return wishItems.toString();
    }
}

