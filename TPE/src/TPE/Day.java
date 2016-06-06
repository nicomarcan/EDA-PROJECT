package TPE;

import java.util.LinkedList;
import java.util.List;

public class Day {
	 	public static final Day MONDAY = new Day(0);
	    public static final Day TUESDAY = new Day(1);
	    public static final Day WEDNESDAY = new Day(2);
	    public static final Day THURSDAY = new Day(3);
	    public static final Day FRIDAY = new Day(4);
	    public static final Day SATURDAY = new Day(5);
	    public static final Day SUNDAY = new Day(6);
	    public static final int size = 7;
	    
	    private final int dayIndex;
	  
	    private static final Day days[] = {MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY};
	    
	    private Day (int index){
	    	dayIndex = index;
	    }
	    
	    public static Day getDay(int index){
	    	Day day = null;
	    	switch(index){
	    		case 0: day = MONDAY;
	    				break;
	    		case 1: day = TUESDAY;
	    				break;
	    		case 2: day =  WEDNESDAY;
	    				break;	
	    		case 3: day = THURSDAY;
	    				break;	
	    		case 4: day = FRIDAY;
	    				break;	
	    		case 5: day = SATURDAY;
	    				break;	
	    		case 6: day = SUNDAY;
	    				break;	
	    	}
	    	return day;
	    }
	    
	    public static int getIndex(Day d){
	     	if(d == Day.MONDAY)
	     		return 0;
	     	if(d == Day.TUESDAY)
	     		return 1;
	     	if( d == Day.WEDNESDAY)
	     		return 2;
	     	if(d == Day.THURSDAY)
	     		return 3;
	     	if(d == Day.FRIDAY)
	     		return 4;
	     	if(d == SATURDAY)
	     		return 5;
	     	if(d == SUNDAY)
	     		return 6;
	     	return 0;
	     	
	    }
	    public static void main(String[] args) {
			System.out.println(Day.getIndex(Day.TUESDAY));
		}
	    public String toString(){
	    	String a = "";
	    	return a+this.dayIndex;
	    }
	    
	    public static List<Day> getAllDays(){
	    	List<Day> days = new LinkedList<Day>();
	    	for(int i = 0 ; i< Day.size;i++){
	    		days.add(getDay(i));
	    	}
	    	return days;
	    }
	    
	    public static List<Day> getDays(String[] days) {
			List<Day> ans = new LinkedList<Day>();
			for(int i = 0; i <days.length;i++){
				switch(days[i]){
				case "Lu": ans.add(Day.MONDAY);
							break;
				case "Ma": ans.add(Day.TUESDAY);
							break;
				case "Mi": ans.add(Day.WEDNESDAY);
							break;
				case "Ju": ans.add(Day.THURSDAY);
							break;
				case "Vi": ans.add(Day.FRIDAY);
							break;
				case "Sa": ans.add(Day.SATURDAY);
							break;
				case "Do": ans.add(Day.SUNDAY);
							break;		
				}
			}
			return ans;
		}
	    
		public static boolean checkDays(String[] days) {
			for(int i = 0; i < days.length;i++){
				for(int j = i+1 ; j < days.length;j++){
					if(days[i].equals(days[j])){
						return false;
					}
				}
			}
			return true;
			
		}
	    
	    
}


