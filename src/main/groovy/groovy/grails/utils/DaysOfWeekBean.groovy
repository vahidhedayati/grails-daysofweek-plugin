package grails.utils

import grails.validation.Validateable
import groovy.grails.utils.DaysOfWeek


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


	/**
	 * This is the from Language / from locale of given date
	 * currently only supports Julian "Western Dates" which can be converted to
	 * other calendars by defining a lang or locale below
	 * These 2 optional if not set will default from/to locale to locale value below
	 */
	Locale fromLocale
	String fromLang

	//This is the language / or locale to be of provided date
	//lang optional if provided and no locale given locale loaded for language code given
	String lang
	Locale locale

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
			dow=DaysOfWeek.allWeek()
		}
		if (!dow && selectedDays) {
			dow=DaysOfWeek.fromListToBit(selectedDays)
		}
		if (dow) {
			daysOfWeekList=DaysOfWeek.fromBitValueToList(dow as int)
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


	Locale getLocale() {
		if (lang && !locale) {
			return convertLocale(lang)
		}
		return locale?:Locale.UK
	}


	public static Locale convertLocale(String language) {
		if (language.contains('_')) {
			List<String> lang=language.split('_')
			return Locale.getInstance(lang[0].toLowerCase(),lang[1].toUpperCase(),'')
		}
		return Locale.getInstance(language,'','')
	}
}