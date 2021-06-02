package todo_package;
import java.util.*;

public class ItemList implements List {
	
	private String name;
	private ArrayList<ListItem> itemlist = new ArrayList<>();
	
	/**
	 * Constructor - takes a list name
	 * @param name
	 */
	public ItemList(String name) {
		if(name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
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
	 * takes the name of a task as a parameter and removes that task item from the itemlist
	 * @param task
	 * @return true if process succeeds, false if it fails for any reason
	 */
	public boolean remove(String task) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	/**
	 * takes an integer as a parameter and returns the item at that index of itemlist
	 * @param index
	 * returns listItem
	 */
	public ListItem get(int index) {
		return this.itemlist.get(index);
	}
	
	/**
	 * @return the itemlist - deep or shallow copy unnecessary, mutability is wanted
	 */
	public ArrayList<ListItem> getList() {
		return this.itemlist;
	}
	
	public String getName() {
		return new String(this.name);
	}
	
	/**
	 * an Override of the .toString() method
	 * @return String
	 */
	@Override
	public String toString() {
		String result = "";
		
		result += "list: " + this.name + " - ";
		for(int i = 0; i < this.itemlist.size(); i++) {
			result += this.itemlist.get(i);
			if(i != this.itemlist.size() - 1) {
				result += " ; ";
			}
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
