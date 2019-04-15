package grails.utils

import org.codehaus.groovy.grails.web.binding.DataBindingUtils

/**
 *  <dow:week />
 * 
 *  <dow:week dow="${64 as Byte}" />
 *  
 *  <dow:week daysOfweek="${['SUN','MON']}" />
 *  <dow:week daysOfweek="${['SUN','MON','SAT']}" />
 * 
 *  
 *  <dow:week locale="${new Locale("ENGLISH", "US")}" />
 *  <dow:week locale="${new Locale("", countryCode)}" />
 *  <dow:week locale="${Locale.EN}" />
 *  
 *  <dow:week template="/some/path/to/gsp/template/_override_plugin.gsp" />
 *  
 *  
 * @author Vahid Hedayati
 *
 */
class DayOfWeekTagLib {
	
	static namespace='dow'
	
	def bootstrap={
		out << g.render(contextPath: pluginContextPath, template:'/dow/bootstrap')
	}
	
	def week = { attrs ->
		DaysOfWeekBean bean = new DaysOfWeekBean()
		DataBindingUtils.bindObjectToInstance(bean, attrs)
		bean.formatBean()
		if (attrs.template) {
			out << g.render(template:attrs.template, [instance:bean])
		} else {
			out << g.render(contextPath: pluginContextPath, template : attrs?.template?:"/dow/displayWeek", model: [instance:bean])
		}
		
	} 
}
