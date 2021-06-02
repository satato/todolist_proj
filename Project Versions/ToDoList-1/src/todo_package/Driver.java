package todo_package;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Driver {

	public static ArrayList<ItemList> lists = new ArrayList<>();
	private static boolean quit = false;
	private static int listCount = 0;
	
	public static void main(String[] args) throws IOException {
		
		Scanner scan = new Scanner(System.in);
		
		//generates initial, general list object, daily list, and weekly list
		ItemList general = new ItemList("General");
		ItemList daily = new ItemList("daily");
		ArrayList<List> weekly = new ArrayList<>();
		
		lists.add(general);
		Utilities.load();
		
		//welcomes user and begins action sequence
		System.out.println("Welcome to the auto-organizing To-Do List!");
		
		while(!quit) {
			quit = start(scan);
		}
		scan.close();
		System.err.println("Quitting program... Please wait while we finish saving");
		Utilities.save();
	}

	/**
	 * presents the main menu options and calls the appropriate action based on the user's decision
	 * @param scan
	 * @return boolean (true if the user chooses to quit, false if any other choice was made)
	 */
	private static boolean start(Scanner scan) {
		String userInput;
		int choice = 0;
		boolean good = true;
		System.out.println("Would you like to:\n1)Edit your Lists\n2)Edit your Tasks\n3)Quit");
		
		userInput = scan.next();
		try {
			choice = Integer.parseInt(userInput);
			if(choice > 6 || choice < 1) {
				good = false;
			}
			else {
				switch (choice) {
				case 1:
					boolean result = editLists(scan);
					if(result) {
						System.err.println("Returning to Menu...");
					}
					break;
				case 2:
					editTasks(scan);
					break;
				case 3:
					return true;
				}
			}
		}
		catch(Exception e) {
			good = false;
		}
		if(!good) {
			System.err.println("Invalid Response. Restarting...");
		}
		
		return false;
	}
	
	/**
	 * Presents a list-editing/creation & deletion menu
	 * @param scan
	 * @return true if action completes without issue, false if process fails for any reason
	 */
	private static boolean editLists(Scanner scan) {
		System.err.println("Entering list editor...");
		System.out.println("Would you like to:\n1)Create a new list\n2)Delete a list\n3)View your lists\n4)Exit");
		String response = scan.next();
		
		try {
			int answer = Integer.parseInt(response);
			if(answer < 1 || answer > 4) {
				System.err.println("Invalid Response. Restarting...");
				return false;
			}
			else {
				switch (answer) {
				case 1:
					newList(scan);
					break;
				case 2:
					deleteList(scan);
					break;
				case 3:
					viewLists();
					break;
				case 4:
					System.err.println("Exiting list editor...");
					return false;
				}
			}
		}
		catch(Exception e) {
			System.err.println("Invalid Response. Restarting...");
		}
		return true;
	}
	
	/**
	 * method for viewing all lists that the user has created/has access to
	 * @return ArrayList<String> list of the names of all user-created to-do lists
	 */
	private static ArrayList<String> viewLists() {
		ArrayList<String> listnames = new ArrayList<>();
		
		System.out.println("To-Do Lists:");
		for(int i = 0; i < lists.size(); i++) {
			listnames.add(lists.get(i).getName());
			System.out.println(lists.get(i).toString());
		}
		System.out.println();
		return listnames;
	}
	
	/**
	 * creates a new list named by the user
	 * @param scan
	 */
	private static void newList(Scanner scan) {
		System.out.println("Please give this list a name:\n");
		String name = scan.next();
		lists.add(new ItemList(name));
		viewLists();
	}
	
	/**
	 * prompts the user to choose a list to delete and deletes it
	 * @param scan
	 */
	private static void deleteList(Scanner scan) {
		
		for(int i = 0; i < lists.size(); i++) {
			System.out.println(lists.get(i).toString(i));
		}
		
		System.out.println("Which list would you like to delete? (Please type answer as a single number)\n");
		String response = scan.next();
		try {
			int answer = Integer.parseInt(response);
			if(answer == 0) {
				System.out.println("Sorry! You can't delete that - it's where we store ALL of your tasks!");
			}
			else if(answer < 1 || answer > lists.size() - 1) {
				System.err.println("Invalid Response. Returning to Menu...");
			}
			else {
				lists.remove(answer);
				viewLists();
			}
		}
		catch(Exception e) {
			System.err.println("Invalid Response. Returning to Menu...");
		}
		
	}
	
	/**
	 * Presents a task-editing/creation & deletion menu
	 * @param scan
	 */
	private static void editTasks(Scanner scan) {
		System.err.println("Entering task editor...");
		System.out.println("Would you like to:\n1)Create a new task\n2)Delete a task from a list\n3)View your tasks\n4)Add task to list\n5)Exit");
		String response = scan.next();
		
		try {
			int answer = Integer.parseInt(response);
			if(answer < 1 || answer > 5) {
				System.err.println("Invalid Response. Returning to Menu...");
			}
			else {
				switch (answer) {
				case 1:
					lists.get(0).add(newTask(scan));
					break;
				case 2:
					listSelector(scan, 1);
					break;
				case 3:
					viewTasks(lists.get(0));
					break;
				case 4:
					listSelector(scan, 0);
					break;
				case 5:
					return;
				}
			}
		}
		catch(Exception e) {
			System.err.println("Invalid Response. Returning to Menu...");
		}
		
	}
	
	/**
	 * presents all user-created tasks
	 * @param list
	 */
	private static void viewTasks(ItemList list) {
		System.out.print("Tasks in " + list.getName() + " [");
		for(int i = 1; i <= list.getList().size(); i++) {
			System.out.print(i + ": " + list.get(i - 1));
			if(i != list.getList().size()) {
				System.out.println();
			}
		}
		System.out.print("]\n");
	}
	
	/**
	 * takes user input to select a list to add a task to or remove a task from
	 * @param scan
	 * @param action
	 */
	private static void listSelector(Scanner scan, int action) {
		
		System.out.println("To-Do Lists:");
		for(int i = 0; i < lists.size(); i++) {
			System.out.println(lists.get(i).toString(i));
		}
		
		int response;
		
		//if 0, add task to list. if 1, remove task from list
		if(action == 0) {
			System.out.println("Please type the number of the list you want to add a task to as a single number: ");
			response = scan.nextInt();
			
			if(response < 0 || response > lists.size() - 1) {
				System.err.println("Invalid Response. Returning to Menu...");
			}
			else {
				ListItem item = newTask(scan);
				if(response != 0) {
					lists.get(0).add(item);
				}
				lists.get(response).add(item);
			}
		}
		else {
			System.out.println("Please type the number of the list you want to remove a task from as a single number: ");
			response = scan.nextInt();
			
			if(response < 0 || response > lists.size() - 1) {
				System.err.println("Invalid Response. Returning to Menu...");
			}
			else {
				ItemList temp = lists.get(response);
				viewTasks(temp);
				System.out.println("Please type the number of the task you want to remove as a single number: ");
				response = scan.nextInt();
				
				if(response < 1 || response > temp.getList().size()) {
					System.err.println("Invalid Response. Returning to Menu...");
				}
				else {
					temp.remove(response - 1);
				}
			}
		}
		
		viewLists();
		
	}
	
	/**
	 * creates a new task/item
	 * @param scan
	 * @return the created ListItem object
	 */
	private static ListItem newTask(Scanner scan) {
		System.out.println("Please name this task:\n");
		String name = scan.next();
		
		System.out.println("Please type the due date in the format: mm/dd/yyyy, or type 'none':\n");
		String duedate = scan.next();
		
		String dodate = null;
		if(!duedate.equals("none")) {
			System.out.println("Please type the 'do' date in the format: mm/dd/yyyy, or type 'none':\n");
			dodate = scan.next();
		}
		
		System.out.println("Next please rank the importance of this task from 1 to 5:\n");
		int i = scan.nextInt();
		
		System.out.println("Next please rank the workload/time it will take to complete this task from 1 to 5:\n");
		int w = scan.nextInt();
		
		ListItem task;
		if(duedate.equals("none")) {
			task = new ListItem(name, i, w);
		}
		else {
			task = new ListItem(name, duedate, dodate, i, w);
		}
		
		return task;
	}
	
	/**
	 * removes a task of the user's choice
	 * @param scan
	 */
	private static void removeItem(Scanner scan, ItemList list) {
		viewTasks(list);
		System.out.println("Please type the number of the task you would like to remove: ");
	}
	
	/**
	 * edits a task of the user's choice
	 * @param scan
	 */
	private static void editItem(Scanner scan) {
		throw new UnsupportedOperationException("not implemented yet");
	}
	
}
