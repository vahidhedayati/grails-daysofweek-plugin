package grails.utils

import grails.utils.DaysOfWeek
import grails.validation.Validateable


/**
 * 
 * Validation bean to work with the displayWeek template
 * 
 * If you wish to reuse this functionality simply extend this in your own validation
 * 
 *  
 * class MyValidation extends  DaysOfWeekBean {
 *   //do other stuff and you get all of this too
 * }
 * 
 * @author Vahid Hedayati
 *
 */

class DaysOfWeekBean implements Validateable {
	// the byte value that represents bitwise value of week days selected
	Byte dow
	
	//physical list of days bound to above byte
	List<String> daysOfWeekList
	
	//This gets set as selectedDays to strip out all nulls sent via front end
	List daysOfweek=[] // new ArrayList<String>() 

	Locale locale=Locale.UK

	//div container override per call
	String context='default'

	//This defines the checkbox element collected
	String fieldName='daysOfweek'

	//Show default label provided in gsp i.e. weekDays.label
	boolean showLabel=false

	//Show locale next to above provided label - only works if above is enabled
	boolean showLocale=false

	//if set to true will auto select all days of the week
	boolean activateAll=false

	//Only 1 element on a gsp can have this as true, follow sample controller return call
	boolean bindToBean=false

	def formatBean() {
		if (activateAll && !dow && !selectedDays ) {
			dow= DaysOfWeek.allWeek()
		}
		if (!dow && selectedDays) {
			dow=DaysOfWeek.fromListToBit(selectedDays)
		}
		if (dow) {
			daysOfWeekList= DaysOfWeek.fromBitValueToList(dow as int, locale)
		}
	}

	List getSelectedDays() {
		return daysOfweek?.findAll{it}
	}
    static constraints = {
		locale(nullable:true)
        daysOfweek(nullable:true)
        dow(min:(byte)1,max:(byte)127)
    }
}
