package sorting;

import java.util.Comparator;
import java.time.LocalDate;
import app.ListItem;

public class Sortbyduedate  implements Comparator<ListItem>{

	@Override
	public int compare(ListItem a, ListItem b) {
		
		LocalDate aDate = a.getDue();
		LocalDate bDate = a.getDue();
		
		return aDate.compareTo(bDate);
	}

}
