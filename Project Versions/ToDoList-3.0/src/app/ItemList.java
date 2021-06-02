package app;

import java.util.ArrayList;

public class ItemList {
	private ArrayList<ListItem> itemlist = new ArrayList<>();
	private String name;
	private int id;
	
	public static int ID = 0;
	
	/**
	 * Constructor
	 * @param name
	 */
	public ItemList(String name) {
		if(name == null) {
			throw new IllegalArgumentException("invalid list name");
		}
		this.name = name;
		this.id = ID;
		ID++;
		Driver.usedListNames.add(name);
	}
	
	/**
	 * takes a String as a parameter and renames the list it
	 * @param name
	 * @return true if renaming succeeds, false if it fails for any reason
	 */
	public boolean rename(String name) {
		if(name != null) {
			int i = Driver.usedListNames.indexOf(this.name);
			Driver.usedListNames.remove(i);
			
			this.name = name;
			Driver.usedListNames.add(name);
			return true;
		}
		return false;
	}
	
	/**
	 * takes a listItem as a parameter and adds that item to the itemlist
	 * @param item
	 * @return true if process succeeds, false if it fails for any reason
	 */
	public boolean add(ListItem item) {
		try {
			this.itemlist.add(item);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * clears the list's itemlist of all listitems
	 */
	public void clear() {
		this.itemlist.clear();
	}
	
	/**
	 * takes an integer as a parameter and removes that index from the itemlist
	 * @param index
	 * @return true if process succeeds, false if it fails for any reason
	 */
	public boolean remove(int index) {
		try {
			this.itemlist.remove(index);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * takes an integer as a parameter and returns the item at that index of itemlist
	 * @param index
	 * @return listItem
	 */
	public ListItem get(int index) {
		return this.itemlist.get(index);//??
	}
	
	/**
	 * @return the itemlist - deep or shallow copy unnecessary, mutability is wanted
	 */
	public ArrayList<ListItem> getList() {
		return this.itemlist;
	}
	
	/**
	 * gets the list's name
	 * @return String
	 */
	public String getName() {
		return new String(this.name);
	}
	
	/**
	 * an Override of the .toString() method
	 * @return String
	 */
	@Override
	public String toString() {
		String result = this.name + ",";
		
		for(int i = 0; i < this.itemlist.size(); i++) {
			result += (this.itemlist.get(i).toString(0) + ";");
		}
		
		return result;
	}
	
	/**
	 * an Override of the .toString() method
	 * @return String
	 */
	public String toString(int index) {
		String result = "";
		
		result += "list " + index + ": " + this.name + " - ";
		for(int i = 0; i < this.itemlist.size(); i++) {
			result += this.itemlist.get(i) + " | ";
		}
		
		return result;
	}
}
