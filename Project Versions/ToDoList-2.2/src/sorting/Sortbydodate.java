package sorting;

import java.time.LocalDate;
import java.util.Comparator;

import app.ListItem;

public class Sortbydodate  implements Comparator<ListItem>{

	@Override
	public int compare(ListItem a, ListItem b) {

		int[] aDate = a.getDoDate();
		int[] bDate = a.getDoDate();
		
		LocalDate first = LocalDate.of(aDate[2], aDate[0], aDate[1]);
		LocalDate second = LocalDate.of(bDate[2], bDate[0], bDate[1]);
		
		return first.compareTo(second);
	}

}
