package  grails.utils

import com.ibm.icu.util.ULocale
import com.ibm.icu.util.Calendar

public enum DaysOfWeek {

    SUN((byte)1,1,true),
    MON((byte)2,2,false),
    TUE((byte)4,3,false),
    WED((byte)8,4,false),
    THU((byte)16,5,false),
    FRI((byte)32,6,false),
    SAT((byte)64,7,true)
	
	//http://stackoverflow.com/questions/313417/whats-the-best-way-to-store-the-days-of-the-week-an-event-takes-place-on-in-a-r
	//sun=1, mon=2, tue=4, wed=8, thu=16, fri=32, sat=64.
	//Now, say the course is held on mon, wed and fri. the value to save in database would be 42 (2+8+32). Then you can select courses on wednesday like this:
	//select * from courses where (days & 8) > 0
	//if you want courses on thu and fri you would write:
	//select * from courses where (days & 48) > 0
    byte value

	//Actual day of week SUN = 1 MON = 2 ... SAT = 7	
    int dow
	
	boolean isWeekend

    DaysOfWeek(byte val, int d, boolean a) {
        this.value = val
        this.dow = d
		this.isWeekend=a
    }

    public byte getValue(){
        return value
    }

    public int getDow(){
        return dow
    }
	public boolean getIsWeekend(){
		return isWeekend
	}
	
	public void setIsWeekend(boolean s){
		this.isWeekend=s
	}
	
	/**
	 * Internal comparator designed to return days of the week in correct order based on user's locale / end country
	 * ?lang=fa
	 * vs
	 * ?lang=en	
	 * @return
	 */
	DaysOfWeek[] getOrderedDaysOfWeek() {
		DaysOfWeek[] array = values();
		DayComparator dayComparator = new DayComparator()
		dayComparator.currentDay=this
		Arrays.sort(array, dayComparator);
		return array;
	}
	DaysOfWeek[] orderedDaysOfWeek(Locale locale) {
		DaysOfWeek[] array = values();
		DayComparator dayComparator = new DayComparator()
		dayComparator.currentDay=this
		dayComparator.locale=locale
		Arrays.sort(array, dayComparator);
		return array;
	}
	
	public static DaysOfWeek getFirstDayOfWeek(Locale locale) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		return DaysOfWeek.byDow(c.getFirstDayOfWeek())
	}
	
	public static int getWeekEndStart(Locale locale) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		return c.getWeekData().weekendOnset
	}
	/**
	 * 
	 * Comparator to return week days as per locale of end user
	 * @author Vahid Hedayati
	 *
	 */
	class DayComparator implements Comparator<DaysOfWeek> {
		DaysOfWeek currentDay=DaysOfWeek.SUN
		Locale locale = Locale.UK
		int weekendStart = DaysOfWeek.getWeekEndStart(locale)
		int weekendEnd = DaysOfWeek.getWeekEndEnd(locale)
		@Override
		public int compare(final DaysOfWeek o1, final DaysOfWeek o2) {
			//LHS/RHS Bumped by 100 so 1 = 101 = SUN 2 = 102 = MON
			int lhDow=o1.getDow()+100
			int rhDow=o2.getDow()+100
			//hack to reduce RHS by 100 if greater than current day
			if (o2.getDow()>=currentDay.getDow()) {
				rhDow-=100
			}
			//hack to reduce LHS by 100 if greater than current day
			//If met MON = 2 from 102 and so on
			if (o1.getDow()>=currentDay.getDow()) {
				lhDow-=100
			}
			
			//TODO - this appears to be working correctly may need to be revisited 
			//o1.setIsWeekend(o2.getDow()>=weekendStart && o1.getDow()<=weekendEnd)
			o2.setIsWeekend(o1.getDow()>=weekendStart && o2.getDow()<=weekendEnd)
			
			//Final comparison should be things in relevant oder either bumped or normal number 
			return lhDow.compareTo(rhDow)
		}
	}
	

    public static byte fromListToBit(List<String> dow) {
        byte sum=(byte)0
        dow?.each { String d->
            sum+=DaysOfWeek.byKey(d).value
        }
        return sum
    }


	
	public static int getWeekEndEnd(Locale locale) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		return c.getWeekData().weekendCease
	}
	
    public static List<String> daysByLocale (Locale locale) {
        /**
         * JDK provides this method to get first day of week
         * but for example Iran states DOW = 1 . Incorrect should be Saturday
         * 
         * Calendar.getInstance(locale1).getFirstDayOfWeek()
         * 
         * using icu4j libraries instead
         */
		return getFirstDayOfWeek(locale).orderedDaysOfWeek(locale)
    }

    public static List<String> fromBitValueToList (final int origBitMask) {
        final List<String> ret_val = []
        int bitMask = origBitMask
        for ( final DaysOfWeek val : DaysOfWeek.values( ) ) {
            if ( ( val.value & bitMask ) == val.value ) {
                bitMask &= ~val.value
                ret_val.add(val.toString())
            }
        }
        if (bitMask != 0) {
            throw new IllegalArgumentException(String.format("Bit mask value 0x%X(%d) has unsupported bits 0x%X.  Extracted values: %s",
                    origBitMask, origBitMask, bitMask, ret_val))
        }

        return ret_val
    }

    static byte allWeek() {
        return (values().collect{it.value}.sum())
    }

    static DaysOfWeek byValue(byte val) {
        values().find { it.value == val }
    }

    static DaysOfWeek byDow(int val) {
        values().find { it.dow == val }
    }

    static DaysOfWeek byKey(String val) {
        values().find { it.toString() == val }
    }

	
	
	/**
	 * Static comparator that returns results based on order of dow value in ascending order
	 * fixed result set will always return SUN MON TUE WED etc..
	 *
	 * To call
	 * DaysOfWeek.getDaysOfWeek() which returns a collection of days of week
	 *
	 */

	private static final DowComparator dowComparator = new DowComparator()
	
	public static DaysOfWeek[] getDaysOfWeek() {
		DaysOfWeek[] array = values();
		Arrays.sort(array, dowComparator);
		return array;
	}
	
	private static final class DowComparator implements Comparator<DaysOfWeek> {
		@Override
		public int compare(final DaysOfWeek o1, final DaysOfWeek o2) {
			o1.compareTo(o2)
		}
	}

}

