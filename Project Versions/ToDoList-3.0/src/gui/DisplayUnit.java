package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import sorting.*;
import javax.swing.*;
import app.*;

/**
 * class that produces the general display
 * @author amber
 *
 */
public class DisplayUnit {
	
	//fields
	private JFrame frame;
	private JPanel mainPanel = new JPanel(new GridBagLayout());
	private GridBagConstraints c;
	private JMenuBar menu = new JMenuBar();
	private JMenu editMenu = new JMenu("Edit");
	private JMenu addMenu = new JMenu("New");
	private JMenu helpMenu = new JMenu("Help");
	private ListItem currentItem;
	private JTabbedPane tabbedPane;
	private JScrollPane mainScrollpane;
	
	private static ItemList currentList = Driver.lists.get(0);
	
	/**
	 * Constructor
	 */
	public DisplayUnit() {
		frame = new JFrame("To Do List");
		frame.setSize(900, 600);
		frame.setLayout(new BorderLayout());

		mainScrollpane = new JScrollPane(mainPanel);
		
		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.add("Home", mainScrollpane);
		
		buildMenu();
		buildMain();
		tabPopulator(tabbedPane);
		
		JScrollPane genScroller = new JScrollPane(tabbedPane);
		frame.add(genScroller);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JPanel getPanel(int n) {
		return (JPanel) tabbedPane.getComponent(n);
	}
	
	/**
	 * method that builds the main home page and its lists
	 */
	private void buildMain() {
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 20;
		c.insets = new Insets(10,5,3,5);
		c.ipadx = 5;
		c.ipady = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		
		JPanel overdue = new JPanel();
		overdue.setPreferredSize(new Dimension(350, 180));
		overdue.setLayout(new BoxLayout(overdue, BoxLayout.Y_AXIS));
		JScrollPane overdueScroll = new JScrollPane(overdue);
		overdue.add(new JLabel("\n"));
		overdue.add(new JLabel("                                                    Overdue"));
		//populates overdue panel with overdue tasks
		homePanelPopulator(overdue, "Overdue");
		
		JPanel today = new JPanel();
		today.setPreferredSize(new Dimension(350, 180));
		today.setLayout(new BoxLayout(today, BoxLayout.Y_AXIS));
		JScrollPane todayScroll = new JScrollPane(today);
		today.add(new JLabel("\n"));
		today.add(new JLabel("                                                    Today"));
		//populates "today" panel with tasks for the day
		homePanelPopulator(today, "Today");
		
		JPanel upcoming = new JPanel();
		upcoming.setPreferredSize(new Dimension(350, 180));
		upcoming.setLayout(new BoxLayout(upcoming, BoxLayout.Y_AXIS));
		JScrollPane upcomingScroll = new JScrollPane(upcoming);
		upcoming.add(new JLabel("\n"));
		upcoming.add(new JLabel("                                                    Upcoming"));
		//populates upcoming panel with upcoming tasks
		homePanelPopulator(upcoming, "Upcoming");
		
		JPanel undated = new JPanel();
		undated.setPreferredSize(new Dimension(350, 180));
		undated.setLayout(new BoxLayout(undated, BoxLayout.Y_AXIS));
		JScrollPane undatedScroll = new JScrollPane(undated);
		undated.add(new JLabel("\n"));
		undated.add(new JLabel("                                                    Undated"));
		//populates undated panel with undated tasks
		homePanelPopulator(undated, "Undated");
		
		JPanel completed = new JPanel();
		completed.setPreferredSize(new Dimension(350, 180));
		completed.setLayout(new BoxLayout(completed, BoxLayout.Y_AXIS));
		JScrollPane completedScroll = new JScrollPane(completed);
		completed.add(new JLabel("\n"));
		completed.add(new JLabel("                                                    Completed"));
		//populates completed panel with completed, non-deleted tasks
		homePanelPopulator(completed, "Completed");
		
		//adds all of the above panels to the main panel and positions them
		mainPanel.add(overdueScroll, c); c.gridy += 5;
		mainPanel.add(todayScroll, c); c.gridy += 5;
		mainPanel.add(undatedScroll, c); c.gridy = 0; c.gridx += 5; c.gridheight = 2;
		mainPanel.add(upcomingScroll, c); c.gridy += 5; c.gridheight = 1;
		mainPanel.add(completedScroll, c);
	}
	
	/**
	 * method that populates the given panel with appropriate tasks
	 * @param panel
	 * @param name
	 */
	public static void homePanelPopulator(JPanel panel, String name) {
		ItemList temp = new ItemList("");
		for(int i = 0; i < Driver.hiddenlists.size(); i++) {
			if(Driver.hiddenlists.get(i).getName().equals(name)) {
				temp = Driver.hiddenlists.get(i);
				break;
			}
		}
		
		String data;
		ListItem task;
		
		for(int i = 0; i < temp.getList().size(); i++) {
			data = "";
			//defines the current task item and fills its data for the tooltip
			task = (ListItem) temp.get(i);
			data += "<html>Due Date: " + task.getDue() + "<br>Do Date: " + 
				task.getDo() + "<br>Importance: " + 
				task.getImportance() + "<br>Workload: " + task.getSize() +"</html";
			
			JCheckBox box = new JCheckBox(temp.get(i).getName());
			box.setToolTipText(data);
			box.addActionListener((ActionListener) new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String task = e.getActionCommand();
					ListItem temp;
					Collections.sort(Driver.usedTaskNames); //sorts list of task names alphabetically
					Collections.sort(Driver.lists.get(0).getList(), new Sortbyname()); //sorts list of all tasks by name alphabetically (should line up with names list)
					
					int i = Driver.usedTaskNames.indexOf(task);
					temp = Driver.lists.get(0).get(i);
					if(temp.crossed)
						temp.crossed = false;
					else
						temp.crossed = true;
					
					Driver.display.buildMain();
				}
			});
			if(task.crossed)
				box.setSelected(true);
			panel.add(box);
		}
	}
	
	/**
	 * method that populates the given tabbedpane with appropriate panels
	 * @param tabs
	 */
	private void tabPopulator(JTabbedPane tabs) {
		tabs.removeAll();
		tabs.add("Home", mainScrollpane);
		tabs.setToolTipTextAt(0, "Home");
		String tempname;
		for(int i = 0; i < Driver.lists.size(); i++) {
			JPanel temp = new JPanel();
			tempname = Driver.lists.get(i).getName();
			tabs.add("<html><body><table width='50'>" + tempname + "</table></body></html>", temp);
			int index = tabs.getComponentCount() - 1;
			tabs.setToolTipTextAt(index, tempname);
		}
	}
	
	/**
	 * private method that puts together the frame's menubar, items, and their action listeners
	 */
	private void buildMenu() {
		//creates necessary menu items
		JMenuItem createList = new JMenuItem("New List");
		JMenuItem createTask = new JMenuItem("New Task");
		
		JMenuItem renameList = new JMenuItem("Rename List");
		JMenuItem deleteList = new JMenuItem("Delete List");
		JMenuItem editDue = new JMenuItem("Change Due Date");
		JMenuItem editDo = new JMenuItem("Change Do Date");
		JMenuItem editImp = new JMenuItem("Change Importance Level");
		JMenuItem editSize = new JMenuItem("Change Size/Workload");
		
		JMenuItem namingHelp = new JMenuItem("Naming Tips"); //this one just needs a tooltip i think
		
		//adds action listeners to each menu item
		createList.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String response = JOptionPane.showInputDialog("Please name this list: ");
				if(Utilities.validateName(response)) {
					Driver.lists.add(new ItemList(response));
					Utilities.save();
					tabPopulator(tabbedPane);
					
					int temp = tabbedPane.getComponentCount() - 1;
					JPanel tempPanel = (JPanel) tabbedPane.getComponent(temp);
					temp = Driver.lists.size() - 1;
					
					//panelPopulator(tempPanel, String.valueOf(temp));
				}
				else {
					JOptionPane.showMessageDialog(null, "Invalid List Name");
				}
			}
		});
		
		createTask.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new taskCreator();
			}
		});
		
		renameList.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		deleteList.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		editDue.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		editDo.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		editImp.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		editSize.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//adds menu items to appropriate menus
		addMenu.add(createList);
		addMenu.add(createTask);
		
		editMenu.add(renameList);
		editMenu.add(deleteList);
		editMenu.add(editDue);
		editMenu.add(editDo);
		editMenu.add(editImp);
		editMenu.add(editSize);
		
		helpMenu.add(namingHelp);
		
		//adds menus to the menu bar
		menu.add(addMenu);
		menu.add(editMenu);
		menu.add(helpMenu);
		
		//adds the menu bar to the frame
		frame.getContentPane().add(BorderLayout.NORTH, menu);
	}
	
	/**
	 * private method for updating the display after any action that alters it
	 * @param panel
	 */
	private void updateDisplay(JPanel panel) {
		//not yet implemented
	}
	
	/**
	 * private method for creating a new list and adding it to the display
	 */
	private void addList() {
		//not yet implemented
	}
	
	/**
	 * private method that deletes the currently selected task
	 */
	private void deleteTask() {
		//not yet implemented
	}
	
	/**
	 * private method that edits the currently selected task
	 * takes an int as a parameter to determine which type of edit is occuring
	 * 0 is change due date
	 * 1 is change "do" date
	 * 2 is change importance rank
	 * 3 is change size/workload rank
	 * @param action
	 */
	private void editTask(int action) {
		//not yet implemented
	}
}

/**
* class that defines and launches the task creation menu
*/
class taskCreator {
	
	private String taskname = null;
	private LocalDate duedate = null;
	private LocalDate dodate = null;
	private int importance = 0;
	private int size = 0;
	private JFrame frame;

	/**
	 * Constructor
	 */
	public taskCreator() {
		frame = new JFrame("Task Creation Menu");
		frame.setSize(500, 400);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollpane = new JScrollPane(mainPanel);
		
		JPanel namePanel = new JPanel();
		JPanel duePanel = new JPanel();
		JPanel doPanel = new JPanel();
		JPanel impPanel = new JPanel();
		JPanel sizePanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		duePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		doPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		impPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sizePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		
		//puts together name-setting panel
		JTextField nameField = new JTextField("Enter Task Name", 20);
		//nameField.addActionListener(new nameListener());
		namePanel.add(new JLabel("*Task Name: "));
		namePanel.add(nameField);
		JButton setname = new JButton("Set Name");
		JLabel badInputLabel = new JLabel("");
		
		//"Set Name" button functionality
		setname.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = nameField.getText();
				boolean badInput = false;
				for(int i = 0; i <  Driver.invalidCharacters.size(); i++) {
					if(input.contains(Driver.invalidCharacters.get(i))) {
						badInput = true;
						break;
					}
				}
				
				if(badInput || Driver.invalidNames.contains(input)) {
					badInputLabel.setText("Invalid Task Name\n"
							+ "This task may already exist, or you may have"
							+ " included illegal characters\n"
							+ "(Task names may not include '|' and similar characters)");
				}
				else {
					taskname = input;
					badInputLabel.setText("");
				}
			}
		});
		
		namePanel.add(setname);
		namePanel.add(badInputLabel);
		
		//prepares data for SpinnerDateModel
		LocalDate today = LocalDate.now();
		int month = today.getMonthValue();
		int year = today.getYear();
		LocalDate temp;
		
		Date start = Date.valueOf(today);
		temp = LocalDate.of(year - 2, month, 1);
		Date initDate = Date.valueOf(temp);
		temp = LocalDate.of(year + 100, month, 1);
		Date finDate = Date.valueOf(temp);
		
		//creates spinner models and spinners for due date and "do" date selection
		//due date selector:
		SpinnerModel due = new SpinnerDateModel(start, initDate, finDate, Calendar.DAY_OF_MONTH);
		JSpinner dueDatePicker = new JSpinner(due);
		
		JSpinner.DateEditor dueEditor = new JSpinner.DateEditor(dueDatePicker, "MM/dd/yyyy");
		dueEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		dueEditor.getTextField().setColumns(10);
		dueDatePicker.setEditor(dueEditor);
		
		dueDatePicker.addFocusListener((FocusListener) new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				//nothing needs to happen when the focus is gained
			}

			@Override
			public void focusLost(FocusEvent e) {
				//when the focus is lost, set the due date to whatever date the spinner has set
				LocalDate date = (LocalDate) dueDatePicker.getValue();
				duedate = date;
			}
		});
		
		duePanel.add(new JLabel("Due Date (MM/DD/YYYY): "));
		duePanel.add(dueDatePicker);
		
		//"do" date selector:
		SpinnerModel doModel = new SpinnerDateModel(start, initDate, finDate, Calendar.DAY_OF_MONTH);
		JSpinner doDatePicker = new JSpinner(doModel);
		
		JSpinner.DateEditor doEditor = new JSpinner.DateEditor(doDatePicker, "MM/dd/yyyy");
		doEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
		doEditor.getTextField().setColumns(10);
		doDatePicker.setEditor(doEditor);
		
		doDatePicker.addFocusListener((FocusListener) new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				//nothing needs to happen when the focus is gained
			}

			@Override
			public void focusLost(FocusEvent e) {
				//when the focus is lost, set the due date to whatever date the spinner has set
				LocalDate date = (LocalDate) doDatePicker.getValue();
				dodate = date;
			}
		});
		
		doPanel.add(new JLabel("'Do' Date (MM/DD/YYYY): "));
		doPanel.add(doDatePicker);
		
		//preps options for importance level and size/workload selection via combobox
		String[] options = {"1", "2", "3", "4", "5"};
		//creates combobox for selecting importance level
		JComboBox<String> impChooser = new JComboBox<String>(options);
		impChooser.setEditable(false);
		impChooser.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox importo = (JComboBox) e.getSource();
				importance = Integer.parseInt((String)(importo.getSelectedItem()));
			}
		});
		
		JLabel impLabel = new JLabel("*Importance Level: ");
		//tool tip informing the user of what this input is
		impLabel.setToolTipText("<html>\"Importance\" is how critical it is that a "
				+ "task gets done at all.<br>This is ranked from 1-5<br>"
				+ "Where 1 means it is totally unimportant, and 5 means it is the most important.</html>");
		impPanel.add(impLabel);
		impPanel.add(impChooser);
		
		//creates combobox for selecting size/workload
		JComboBox<String> sizeChooser = new JComboBox<String>(options);
		sizeChooser.setEditable(false);
		sizeChooser.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox sizeSelecto = (JComboBox) e.getSource();
				size = Integer.parseInt((String)(sizeSelecto.getSelectedItem()));
			}
		});
		
		JLabel sizeLabel = new JLabel("*Size/Workload: ");
		//tool tip informing the user of what this input is
		sizeLabel.setToolTipText("<html>\"Size/Workload\" is how much time a task might take. "
				+ "<br>This is ranked from 1-5.<br>"
				+ "1: task takes <2 hours to complete<br>"
				+ "2: task takes 2-4 hours<br>"
				+ "3: task takes 4-8 hours<br>"
				+ "4: task takes 8-12 hours<br>"
				+ "5: task takes 12+ hours</html>");
		sizePanel.add(sizeLabel);
		sizePanel.add(sizeChooser);
		
		//adds "submit" button and "cancel" button
		JButton submit = new JButton("Submit");
		//submit button functionality
		submit.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean makeTask = false;
				ListItem item = new ListItem("temp");
				if(importance == 0 || size == 0 || taskname == null) {
					JOptionPane.showMessageDialog(null, "Cannot Submit. Please fill all required fields");
				}
				else if(duedate == null || dodate == null) {
					int response = JOptionPane.showConfirmDialog(null, "This task may be missing one or both dates. Are you sure you want to proceed?");
					if(response == JOptionPane.YES_OPTION) {
						item = new ListItem(taskname, importance, size);
						Driver.lists.get(0).add(item);
						makeTask = true;
						JOptionPane.showMessageDialog(frame, "Task Created.");
					}
					//else do nothing
				}
				else {
					Driver.lists.get(0).add(new ListItem(taskname, duedate, dodate, importance, size));
					makeTask = true;
				}
				
				if(makeTask) {
					int index = 0;
					String[] choices = new String[Driver.lists.size()];
					for(int i = 0; i < Driver.lists.size(); i++) {
						choices[i] = Driver.lists.get(i).getName();
					}
					String answer = (String) JOptionPane.showInputDialog(null, "Please select a list to add this task to:", "Task Creation", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					for(int i = 0; i < Driver.lists.size(); i++) {
						if(Driver.lists.get(i).getName().equals(answer)) {
							Driver.lists.get(i).add(item);
							index = i;
							break;
						}
					}
					if(!answer.equals("General")) {
						Driver.lists.get(0).add(item);
					}
					DisplayUnit display = Driver.display;
					JPanel temp = display.getPanel(index);
					//DisplayUnit.panelPopulator(temp, String.valueOf(index + 1));
					Utilities.save();
					frame.dispose();
				}
			}
		});
		
		JButton cancel = new JButton("Cancel");
		//cancel button functionality (closes out of window)
		cancel.addActionListener((ActionListener) new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
				
		buttonsPanel.add(submit); buttonsPanel.add(cancel);
		
		//adds all the panels to the main panel
		mainPanel.add(namePanel); mainPanel.add(duePanel); mainPanel.add(doPanel); 
		mainPanel.add(impPanel); mainPanel.add(sizePanel); mainPanel.add(buttonsPanel);
		
		//adds everything to the frame
		frame.add(scrollpane);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
