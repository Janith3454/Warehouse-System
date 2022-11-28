import java.io.*;

public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clientId;
    private String name;
    private String address;
	private String phone;
    private double balance;
    private ShoppingCart shoppingCart;
    private TransactionList transactionList;
	//private List productsOnwishlist = new LinkedList();

    public Client (String name, String address, String phone) {
        this.name = name;
        this.address = address;
		this.phone = phone;
        clientId = (ClientIdServer.instance()).generateId();
        balance = 0;
        shoppingCart = new ShoppingCart();
        transactionList = new TransactionList();       
    }
	
	//public void placeWishlist(Wishlist wishlist) 
	//{
	//	productsOnwishlist.add(wishlist);
	//}

    public String getname() 
	{
        return name;
    }

    public String getAddress() 
	{
        return address;
    }
		
	public String getPhone() 
	{
    return phone;
	}
	
    public String getClientId() 
	{
        return clientId;
    }

    public double getBalance() 
	{
        return balance;
    }

    public ShoppingCart getShoppingCart() 
	{
        return shoppingCart;
    }

    public TransactionList getTransactionList() 
	{
        return transactionList;
    }
 
    public void setName(String newName) 
	{
		name = newName;
	}
	
	public void setPhone(String newPhone) 
	{
    phone = newPhone;
	}

    public void setAddress(String newAddressName) {
        address = newAddressName;
    }

    public void setClientId(String newClientId) {
        clientId = newClientId;
    }

    public void setShoppingCart(ShoppingCart newCart) {
        shoppingCart = newCart;
    }

    public void setBalance(double newBalance) {
        balance = newBalance;
    }

    public void addBalance(double bal) {
        balance += bal;
    }

    public void subtractBalance(double bal) {
        balance -= bal;
    }

    public boolean equals(String id) {
        return this.clientId.equals(id);
    }

    public String toString() {
        String string = "[Client's id: " + clientId + " Client's name: " + name + " Client's address: " + address + " Client's phone number: " + phone + " Client's balance: " + balance + "]";
        return string;
    }
}
