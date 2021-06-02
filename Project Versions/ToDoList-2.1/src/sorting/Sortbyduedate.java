package sorting;

import java.util.Comparator;
import java.time.LocalDate;
import app.ListItem;

public class Sortbyduedate  implements Comparator<ListItem>{

	@Override
	public int compare(ListItem a, ListItem b) {
		
		int[] aDate = a.getDueDate();
		int[] bDate = a.getDueDate();
		
		LocalDate first = LocalDate.of(aDate[2], aDate[0], aDate[1]);
		LocalDate second = LocalDate.of(bDate[2], bDate[0], bDate[1]);
		
		return first.compareTo(second);
	}

}
