package  grails.utils



import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.Calendar
import com.ibm.icu.util.ULocale

/**
 *  DaysOfWeek is an ENUM which declares 
 *  physical week days i.e. SUN MON,
 *  The ENUM values contains :
 *  a bitwise operation known locally as variable value  -- is fixed
 *  the week day representation known locally as dow   -- is fixed 
 *  a boolean true/false  known locally as isWeekend  --  is modifiable.
 *   -> configures a default week or weekend day is overridden in DayComparator
 *   
 *  This class takes java locale and converts to com.ibm.icu.util.ULocale
 *  and returns a final result
 * 
 * @author Vahid Hedayati April 2019
 *
 */
public enum DaysOfWeek {

	// CODE(bitwise code in byte, actual dow integer , *shortName, *longName, *weekend day? true/false),
	// *= overridden according to locale
    SUN((byte)1,1, 'Sun', 'Sunday', true),
    MON((byte)2,2, 'Mon', 'Monday', false),
    TUE((byte)4,3, 'Tue', 'Tuesday',false),
    WED((byte)8,4, 'Wed', 'Wednesday',false),
    THU((byte)16,5,'Thu', 'Thursday',false),
    FRI((byte)32,6,'Fri', 'Friday',false),
    SAT((byte)64,7,'Sat', 'Saturday',true)

	/*
	 *  http://stackoverflow.com/questions/313417/whats-the-best-way-to-store-the-days-of-the-week-an-event-takes-place-on-in-a-r
	 *  sun=1, mon=2, tue=4, wed=8, thu=16, fri=32, sat=64.
	 *
	 *  Now, say the course is held on mon, wed and fri. the value to save in database would be 42 (2+8+32). 
	 *  Then you can select courses on wednesday like this:
	 *  select * from courses where (days & 8) > 0
	 *  if you want courses on thu and fri you would write:
	 *  select * from courses where (days & 48) > 0
	 */
    byte value
	//Actual day of week SUN = 1 MON = 2 ... SAT = 7	
    int dow
	
	//set by formatting of week days
	String shortName
	
	//set by formatting of week days
	String longName
	
	//set by formatting of week days
	boolean isWeekend

	
    DaysOfWeek(byte val, int dow, String shortName,String longName, boolean isWeekend) {
        this.value = val
        this.dow = dow
		this.shortName=shortName
		this.longName=longName
		this.isWeekend=isWeekend
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
	public String getShortName(){
		return shortName
	}	
	public String getLongName(){
		return longName
	}
	
	//Setters
	public void setIsWeekend(boolean s){
		this.isWeekend=s
	}
	public void setLongName(String s){
		this.longName=s
	}
	public void setShortName(String s){
		this.shortName=s
	}
	
	
	/**
	 * Internal comparator designed to return days of the week in correct order based on user's locale / end country
	 * ?lang=fa
	 * vs
	 * ?lang=en	
	 * @return
	 */
	public static DaysOfWeek[] getOrderedDaysOfWeek() {
		DaysOfWeek[] array = values();
		DayComparator dayComparator = new DayComparator()
		Arrays.sort(array, dayComparator);
		return array;
	}
	
	public static DaysOfWeek[] orderedDaysOfWeek(Locale locale) {
		DaysOfWeek[] array = values();
		DayComparator dayComparator = new DayComparator(locale)
		Arrays.sort(array, dayComparator);
		return array;
	}
	
	
	/**
	 * 
	 * Comparator to return week days as per locale of end user
	 * It does a few things :
	 * 
	 * 1.  modifies current week days dow to be either 
	 * + MAX value of DOW field in this ENUM so +7 or it will retain the DOW
	 * as per ENUM - during compareTo on very last line all entries fall in correct order of 
	 * end user locale.
	 * 
	 *
	 * 
	 * The final output is the DaysOfWeek[] orderedDaysOfWeek above here.
	 * 
	 * @author Vahid Hedayati April 2019
	 *
	 */
	public static class DayComparator implements Comparator<DaysOfWeek> {
		Locale locale = Locale.UK
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		
		int weekendStart = c.getWeekData().weekendOnset
		int weekendEnd = c.getWeekData().weekendCease
		DaysOfWeek currentDay = DaysOfWeek.byDow(c.getFirstDayOfWeek())
		//get it once
		int max =maximum
		int getMaximum() {
			int m = Integer.MIN_VALUE
			for (DaysOfWeek c in DaysOfWeek.values()) {
				m = Math.max(c.getDow(), m)
			}
			return m
		}
		
		DayComparator() {
			
		}
		
		DayComparator(Locale locale) {
			this.locale=locale
		}
		
		@Override
		public int compare(final DaysOfWeek o1, final DaysOfWeek o2) {
			//LHS/RHS Bumped by 7 so 1 = 8 = SUN 2 = 9 = MON
			int lhDow=o1.getDow()+max
			int rhDow=o2.getDow()+max

			//hack to reduce RHS by 100 if greater than current day
			if (o2.getDow()>=currentDay.getDow()) {
				rhDow-=max
			}
			//hack to reduce LHS by 7 if greater than current day
			//If met MON = 2 from 9 and so on
			if (o1.getDow()>=currentDay.getDow()) {
				lhDow-=max
			}
			
			//moved to initialiseEnumByLocale
			//set weekends to true locale weekend days as per country definition
			//o1.setIsWeekend(o1.getDow()==weekendStart || o1.getDow()==weekendEnd)
			//o2.setIsWeekend(o2.getDow()==weekendStart || o2.getDow()==weekendEnd)
			
			//Final comparison should be things in relevant oder either bumped or normal number 
			return lhDow.compareTo(rhDow)
		}
	}
	

	/**
	 * Byte bit1 = DaysOfWeek.fromListToBit(['MON','TUE'])
	 * Byte bit2 = DaysOfWeek.fromListToBit(['MON','SAT','SUN'])
	 * @param daysOfWeek a list containing string names of days of week 
	 * @return a byte sum for those weekdays = what user sees as dow byte value.
	 */
    public static byte fromListToBit(List<String> daysOfWeek) {
        byte sum=(byte)0
        daysOfWeek?.each { String d->
            sum+=DaysOfWeek.byKey(d).value
        }
        return sum
    }

	/**
	 * Dynamically  set data of the enum on call 
	 * 
	 *  1. Attempts to override isWeekend ENUM value set in current ENUM to match the end locale
	 *  2. set the correct short/long name of the day of the week based on provided locale
	 *  
	 * @param locale
	 */
	public static void initialiseEnumByLocale(Locale locale) {
		ULocale ulocale = new ULocale(locale.language,locale.country,locale.variant)
		Calendar c = Calendar.getInstance(ulocale)
		//1st April 2018 starts on Sunday same as our enum starting point
		java.text.DateFormat format = new  java.text.SimpleDateFormat("dd/MM/yyyy")
		Date date = format.parse("1/4/2018")
		String shortDayFormat='EEE'
		String longDayFormat='EEEE'
		SimpleDateFormat sdf = new SimpleDateFormat(shortDayFormat, ulocale)
		SimpleDateFormat ldf = new SimpleDateFormat(longDayFormat, ulocale)
		int weekendStart = c.getWeekData().weekendOnset
		int weekendEnd = c.getWeekData().weekendCease
		DaysOfWeek.values().each{DaysOfWeek val ->
			val.setIsWeekend(false)
			if (val.dow==weekendStart||val.dow==weekendEnd) {
				val.setIsWeekend(true)
			}
			val.setShortName(sdf.format(date))
			val.setLongName(ldf.format(date))
			date++
		}
	}
	
	/**
	 * List<String> myDays = DaysOfWeek.daysOfWeek(Locale.UK)
	 * 
	 * myDays = ['Mon','TUE',...'SUN'] //in order of local first week day
	 * 
	 * JDK provides this method to get first day of week
	 * Calendar.getInstance(locale1).getFirstDayOfWeek()
	 * which in some cases is not true presentation using ICU4J
	 * 
	 */
    public static List<String> daysByLocale (Locale locale) {
		initialiseEnumByLocale(locale)
		return this.orderedDaysOfWeek(locale)
    }

    public static List<String> fromBitValueToList (final int origBitMask, Locale locale = Locale.UK) {
        final List<String> ret_val = []
        int bitMask = origBitMask
        for ( final DaysOfWeek val : this.orderedDaysOfWeek(locale) ) {
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
	 * DaysOfWeek.getAvailableLocales(java.util.Locale)
	 * or 
	 * DaysOfWeek.availableLocales
	 * @param locale optional
	 * @return  a list of java.util.Locale calendars that can be used with this class  
	 */
	public static List<java.util.Locale> getAvailableLocales(Locale locale=Locale.UK) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		return c.getAvailableLocales()
	}
	
	/**
	 * DaysOfWeek.getFirstDayOfWeek(java.util.Locale)
	 * or 
	 * DaysOfWeek.firstDayOfWeek  //UK Bound
	 * @param locale optional
	 * @return DaysofWeek ENUM object equalling first week day of given java locale
	 */
	public static DaysOfWeek getFirstDayOfWeek(Locale locale=Locale.UK) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		return DaysOfWeek.byDow(c.getFirstDayOfWeek())
	}
	
	/**
	 * DaysOfWeek.getWeekEndStart(java.util.Locale)
	 * or
	 * DaysOfWeek.weekEndStart  //UK Bound
	 * @param locale optional
	 * @return integer of which week day is starting day of weekend for that locale - questionable at moment for me
	 */
	public static int getWeekEndStart(Locale locale=Locale.UK) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		// return c.getWeekDataForRegion(locale.country).weekendOnset
		return c.getWeekData().weekendOnset
	}
	
	/**
	 * DaysOfWeek.getWeekEndEnd(java.util.Locale)
	 * or
	 * DaysOfWeek.weekEndEnd  //UK Bound
	 * @param locale optional
	 * @return integer of which week day is ending day  of weekend for that locale - questionable at moment for me
	 */
	public static int getWeekEndEnd(Locale locale=Locale.UK) {
		Calendar c = (Calendar)Calendar.getInstance((ULocale)new ULocale(locale.language,locale.country,locale.variant))
		// return c.getWeekDataForRegion(locale.country).weekendCease
		return c.getWeekData().weekendCease
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
	
	/**
	 * Basic minimal comparator comparing lowest to highest and returning a list of 
	 * DaysOfWeek array above based on dow setting within ENUM
	 * @author Vahid Hedayati
	 *
	 */
	private static final class DowComparator implements Comparator<DaysOfWeek> {
		@Override
		public int compare(final DaysOfWeek o1, final DaysOfWeek o2) {
			o1.compareTo(o2)
		}
	}

}

