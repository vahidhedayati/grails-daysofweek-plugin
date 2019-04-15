package grails.utils

import grails.validation.Validateable


@Validateable
class DaysOfWeekBean  {
    Byte dow
    List<String> daysOfWeekList
   //String[] daysOfweek= [] as String[] ///new ArrayList<String>(); //new String[0] 

	Locale locale

	//div container override per call
	String context='default'
	
	String fieldName='daysOfweek'
		
	def formatBean() {
		if (!dow) {
			//if (!daysOfweek) {
				dow=DaysOfWeek.allWeek()
			//} else {
			//	dow=DaysOfWeek.fromListToBit(daysOfweek)
			//}
		}
		daysOfWeekList=DaysOfWeek.fromBitValueToList(dow as int)
	}

	
    static constraints = {
		locale(nullable:true)
       // daysOfweek(nullable:true)
        dow(min:(byte)1,max:(byte)127)
    }

}
