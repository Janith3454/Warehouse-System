import java.util.*;
import java.io.*;

public class Warehouse implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Warehouse warehouse;
    private ClientList clientList;
	private Inventory inventory;
    private InvoiceList invoiceList;
    private OrderList orderList;
    private ProductList productList;
    private Waitlist waitlist;
	private Wishlist wishlist;
    private ClientIdServer clientIdServer;
    private InvoiceIdServer invoiceIdServer;
    private OrderIdServer orderIdServer;

    private Warehouse() 
	{
        clientList = ClientList.instance();
        invoiceList = InvoiceList.instance();
        orderList = OrderList.instance();
        productList = ProductList.instance();
        waitlist = Waitlist.instance();
		wishlist = Wishlist.instance();
        clientIdServer = ClientIdServer.instance();
        invoiceIdServer = InvoiceIdServer.instance();
        orderIdServer = OrderIdServer.instance();
    }

    public static Warehouse instance() 
	{
        if (warehouse == null) 
		{
            return warehouse = new Warehouse();
        } else 
		{
            return warehouse;
        }
    }

    public static Warehouse retrieve() 
	{
        try {
          FileInputStream file = new FileInputStream("WarehouseData");
          ObjectInputStream input = new ObjectInputStream(file);
          input.readObject();
          input.close();
          return warehouse;
        } catch(IOException ioe) 
		{
          ioe.printStackTrace();
          return null;
        } catch(ClassNotFoundException cnfe) 
		{
          cnfe.printStackTrace();
          return null;
        }
      }
	  
    public static  boolean save() 
	{
      try 
	  {
        FileOutputStream file = new FileOutputStream("WarehouseData");
        ObjectOutputStream output = new ObjectOutputStream(file);
        output.writeObject(Warehouse.instance());
        output.writeObject(ClientIdServer.instance());
        output.writeObject(InvoiceIdServer.instance());
        output.writeObject(InvoiceList.instance());
        output.writeObject(OrderIdServer.instance());
        output.writeObject(OrderList.instance());
        output.writeObject(ProductList.instance());
        output.writeObject(Waitlist.instance());
		output.writeObject(Wishlist.instance());
        output.close();
        return true;
      } catch(IOException ioe) 
		{
          ioe.printStackTrace();
          return false;
        }
      }
	  
    private void writeObject(java.io.ObjectOutputStream output) 
	{
     try 
	 {
        output.defaultWriteObject();
        output.writeObject(warehouse);
     } 
	 catch(IOException ioe) 
     {
        System.out.println(ioe);
     }
    }
	  
    private void readObject(java.io.ObjectInputStream input) 
	{
        try 
		{
          input.defaultReadObject();
          if (warehouse == null) 
		  {
            warehouse = (Warehouse) input.readObject();
          } else 
		  {
            input.readObject();
          }
        } 
		catch(IOException ioe) 
		{
          ioe.printStackTrace();
        } 
		catch(Exception e) 
		{
          e.printStackTrace();
        }
    }

    public Client addClient(String name, String address, String phone) 
	{
        Client client = new Client(name, address, phone);
        if (ClientList.instance().insertClient(client)) 
		{
            return client;
        } 
		else 
		{
            return null;
        }
    }

    public Product addProduct(String name, int quantity, double price) 
	{
        Product product = new Product(name, quantity, price);

        if (ProductList.instance().insertProduct(product))
		{
            return product;
        } 
		else 
		{
            return null;
        }
    }

    public Product getProductById(String id) 
	{
        Iterator<Product> products = ProductList.instance().getProducts();
        Product p = null;
        while (products.hasNext() && p == null) 
		{
            Product tmp = products.next();
            if ( tmp.equals(id)) 
			{
              p = tmp;
            }
        }

        return p;
    }

    public InventoryItem getInventoryItemById(String id) 
	{
        Iterator<InventoryItem> inventory = Inventory.instance().getInventory();
        InventoryItem item = null;
        while (inventory.hasNext() && item == null) 
		{
            InventoryItem tmp = inventory.next();
            if ( tmp.equals(id)) 
			{
              item = tmp;
            }
        }
        return item;
    }

    public Iterator<Product> getProducts() 
	{
      return ProductList.instance().getProducts();
    }

    public Boolean setProductInfo(String id, String name, int quantity, double price) 
	{
        Product p = this.getProductById(id);
        if ( p == null ) 
		{
          return false;
        }
        p.setName(name);
        p.setQuantity(quantity);
        p.setPrice(price);
        return true;
    }

    public Client getClientById(String id)
	{
        Iterator<Client> clients = ClientList.instance().getClients();
        Client p = null;
        while (clients.hasNext() && p == null) 
		{
            Client tmp = clients.next();
            if ( tmp.equals(id) )
			{
                p = tmp;
            }
        }

        return p;
    }

    public Iterator<Client> getClients() 
	{
      return ClientList.instance().getClients();
    }

    public Boolean setClientInfo(String id, String name, String address, String phone) 
	{
        Client p = this.getClientById(id);
        if ( p == null ) 
		{
            return false;
        }
        p.setName(name);
        p.setAddress(address);
		p.setPhone(phone);
        return true;
    }

    public Boolean displayCart(String clientId) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
            return false;
        }
        Iterator<ShoppingCartItem> cartIterator = client.getShoppingCart().getShoppingCartProducts();
        while (cartIterator.hasNext())
		{
            System.out.println(cartIterator.next());
         }
        return true;
    }

    public Boolean addToCart(String clientId, Product product, int quantity) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
          return false;
        }
        client.getShoppingCart().insertProductToCart(product, quantity);
        return true;
    }

    public Boolean emptyCart(String clientId) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
          return false;
        }
        client.setShoppingCart(new ShoppingCart());
        return true;
    }

    public Boolean placeOrder(String clientId) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
            return false;
        }
        Iterator<ShoppingCartItem> cartIterator = client.getShoppingCart().getShoppingCartProducts();
        while(cartIterator.hasNext())
			{
            ShoppingCartItem cartItem = cartIterator.next();
            String productId = cartItem.getProduct().getId();
            InventoryItem inventoryItem = getInventoryItemById(productId); 
            if(inventoryItem != null) 
			{
                int quantityInStock = inventoryItem.getQuantity();
                int cartQuantity = cartItem.getQuantity();
                int newQuantityInStock = 0;
                newQuantityInStock = quantityInStock - cartQuantity;
                if(newQuantityInStock < 0) 
				{
                    int waitItemQuantity = newQuantityInStock * -1;
                    waitlistItem(clientId, productId, waitItemQuantity);
                    inventoryItem.setQuantity(0);
                } 
				else 
				{
                    inventoryItem.setQuantity(newQuantityInStock);
                }
            }
        }
        Transaction transaction = new Transaction("Order Placed", client.getShoppingCart().getTotalPrice());
        client.getTransactionList().insertTransaction(transaction);
        Order order = new Order(client);
        Invoice invoice = new Invoice(order);
        OrderList.instance().insertOrder(order);
        InvoiceList.instance().insertInvoice(invoice);
        client.subtractBalance(client.getShoppingCart().getTotalPrice());
        emptyCart(client.getClientId());
        return true;
    }

    public Iterator<Order> getOrders() 
	{
        return OrderList.instance().getOrders();
    }

    public Boolean waitlistItem(String clientId, String productId, int quantity) 
	{
        Client c = getClientById(clientId);
        Product p = getProductById(productId);
        if ( c == null || p == null || quantity <= 0 ) 
		{
            return false;
        }
        Waitlist waitlist = Waitlist.instance();
        waitlist.insertWaitItem(c, p, quantity);
        return true;
    }
	
	public WishItem getWishlistById(String id)
	{
		Iterator<WishItem> wishlists = Wishlist.instance().getWishlists();
		WishItem w = null;
		while (wishlists.hasNext() && w == null) 
		{
            WishItem tmp = wishlists.next();
			Client c = tmp.getClient();
			String Cid = c.getClientId();
            if ( Cid.equals(id) )
			{
                w = tmp;
            }
        }
		return w;      
	}

	public WishItem wishlistItem(String clientId, String productId, int quantity) 
	{
        Client c = getClientById(clientId);
        Product p = getProductById(productId);
        WishItem wishItem = new WishItem(c, p, quantity);
        if (Wishlist.instance().insertWishItem(wishItem)) 
		{
            return wishItem;
        } 
		else 
		{
            return null;
        }    
    }
	
    public Iterator<WaitItem> getWaitlist() 
	{
        return Waitlist.instance().waitlist();
    }
	
	 public Iterator<WishItem> getWishlist() 
	{
        return Wishlist.instance().getWishlists();
    }

    public Iterator<Invoice> getInvoices() 
	{
        return InvoiceList.instance().getInvoices();
    }

    public Order getOrderById(String id) 
	{
        Iterator<Order> orders = OrderList.instance().getOrders();

        Order o = null;
        while (orders.hasNext() && o == null) 
		{
            Order tmp = orders.next();
            if ( tmp.equals(id)) 
			{
                o = tmp;
            }
        }
        return o;
    }

    public Invoice getInvoiceById(String id) 
	{
        Iterator<Invoice> invoices = InvoiceList.instance().getInvoices();

        Invoice i = null;
        while (invoices.hasNext() && i == null) 
		{
            Invoice tmp = invoices.next();
            if ( tmp.equals(id)) 
			{
                i = tmp;
            }
        }

        return i;
    }

    public int getWaitlistProductQuantity(String productId) 
	{
        int quantity = 0;
        Product p = getProductById(productId);
        Iterator<WaitItem> waitlistIterator = getWaitlist();

        while (waitlistIterator.hasNext() && p != null) 
		{
            WaitItem item = waitlistIterator.next();
            if(p.equals(item.getProduct().getId()) && !item.getOrderFilled()) 
			{
                quantity += item.getQuantity();
            }
        }

        return quantity;
    }

    public List<WaitItem> getWaitItemsByProductId(String id) 
	{
        List<WaitItem> foundItemsList = new LinkedList<WaitItem>();
        Iterator<WaitItem> waitlist = Waitlist.instance().waitlist();

        Product p = getProductById(id);
        while (waitlist.hasNext() && p != null) 
		{
            WaitItem item = waitlist.next();
            if(p.equals(item.getProduct().getId()) && !item.getOrderFilled()) 
			{
                foundItemsList.add(item);
            }
        }

        return foundItemsList;
    }

    public Boolean addToInventory(String productId, int quantity) 
	{
        Product product = this.getProductById(productId);
        if ( product == null ) 
		{
            return false;
        }
        InventoryItem item = getInventoryItemById(productId);
        if(item == null) 
		{
            Inventory.instance().addToInventory(product, quantity);
        } else {
            int currentQuantity = item.getQuantity();
            int newQuantity = currentQuantity += quantity;
            item.setQuantity(newQuantity);
        }
        return true;
    }

    public Boolean makePayment(String clientId, double amount) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
            return false;
        }
        client.addBalance(amount);
        Transaction transaction = new Transaction("Payment Made", amount);
        client.getTransactionList().insertTransaction(transaction);
        return true;
    }

    public Iterator<Transaction> getTransactions(String clientId) 
	{
        Client client = this.getClientById(clientId);
        if ( client == null ) 
		{
           return null;
        }
        return client.getTransactionList().getTransactions();
    }

    public Iterator<InventoryItem> getInventory() 
	{
        return Inventory.instance().getInventory();
    }

    public Boolean addProductToInventory(String id, int quantity) 
	{
        Product p = getProductById(id);
        if(p == null) 
		{
            return false;
        }
        Inventory.instance().addToInventory(p, quantity);
        return true;
    }
    
}
