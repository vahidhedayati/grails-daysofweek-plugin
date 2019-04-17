
`TestController.groovy`:

```groovy

import grails.utils.DaysOfWeek
import grails.utils.DaysOfWeekBean
class TestController {

	def index() {
		render view: 'index'
	}
	
	/**
	 * Since our primary call in index binded to Bean we have declared the bean in this instance
	 * Please refer to the bean itself to see how you would extend and reuse in your own custom validation etc.
	 * 
	 * The rest is bean-less - the myDow stuff does not need the DaysOfWeekBean bean defined in test2 method.
	 * 
	 * @param bean - only needed if binded to bean
	 * <dow:week activateAll="${false}" bindToBean="${true}" />
	 * 
	 * @return 
	 */
	def test2(DaysOfWeekBean bean) {
		bean.formatBean()
		Byte myDow,myDow2,myDow100
		if (params.myDow) {	
			myDow = DaysOfWeek.fromListToBit(params.list('myDow'))
		}
		
		if (params.myDow2) {
		 myDow2 = DaysOfWeek.fromListToBit(params.list('myDow2'))
		}
		if (params.myDow100) {
			myDow100 = DaysOfWeek.fromListToBit(params.list('myDow100'))
		}
		render view:'test',model:[bean:bean,myDow:myDow,myDow2:myDow2,myDow100:myDow100]
	}
	
	def testjquery() {
		render view: 'jquery'
	}
	
	def testjquery2(DaysOfWeekBean bean) {
		bean.formatBean()
		Byte myDow,myDow2,myDow100
		if (params.myDow) {
			myDow = DaysOfWeek.fromListToBit(params.list('myDow'))
		}
		
		if (params.myDow2) {
		 myDow2 = DaysOfWeek.fromListToBit(params.list('myDow2'))
		}
		if (params.myDow100) {
			myDow100 = DaysOfWeek.fromListToBit(params.list('myDow100'))
		}
		render view:'testjquery2',model:[bean:bean,myDow:myDow,myDow2:myDow2,myDow100:myDow100]
	}
	
}
```


Above are 4 controller declarations `index()`   `test2(DaysOfWeekBean bean)` this only binds to the bean due to very first callon index.gsp that attempts to bind to bean, it carries out `formatBean()` which configures everything for days dow byte value as per last screen selection.


It also provides `testjquery()` and  `testjquery2(DaysOfWeekBean bean)` the same write up as above applies around the bean binding etc.

These are now each wired in to either call up as per first 2 the bootstrap way or as per last 2 jquery-ui method of loading weekdays.

 
#### Bootstrap views:

`index.gsp`:

```gsp
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'demo.label', default: 'demo runCommand')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<dow:bootstrap/>
	</head>
	<body>
	<g:form action="test2" >
		Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc
	   <dow:week activateAll="${false}" bindToBean="${true}"  
	  	 showLabel="${true}" showLabel="${true}" showLocale="${true}"/>
	   
	   <!-- Testing dow as 64 and locale as Iraq -->
	   <dow:week dow="${64 as Byte}" context="section2" 
			showLabel="${true}" fieldName="myDow"  showLocale="${true}" locale="${new Locale("ar","IQ")}" />
	   
	   <!-- Testing FA_IR -->
	   <dow:week dow="${100 as Byte}" fieldName="myDow2" 
	   		showLabel="${true}" locale="${new Locale("fa","IR")}" showLocale="${true}" context="section3" />
	   
	   <!-- Testing th_TH -->
	   <dow:week activateAll="${true}" context="section100" 
	   		showLabel="${true}"  fieldName="myDow100" locale="${new Locale("th","TH")}" showLocale="${true}" />
	   
	 <g:submitButton name='doit'/>
	 </g:form>
	</body>
</html>
```

`test2.gsp` page that picks up the form action and reposts back to itself:
 
```gsp
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'demo.label', default: 'demo runCommand')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
			<dow:bootstrap/>
	</head>
	<body>
	<g:form action="test2">
		Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc    
   		<br>
	<b>Validation bean translated DOW to : ${bean.dow}
	  picked up days: ${bean.selectedDays }</b>
	  
   <dow:week   dow="${bean.dow}" bindToBean="${true}" showLocale="${true}"  showLabel="${true}" />

   <!-- Testing dow as 64 and locale as Iraq -->
   <dow:week dow="${myDow}" context="section2"  
   showLabel="${true}" fieldName="myDow" locale="${new Locale("ar","IQ")}" showLocale="${true}" />
   
   <!-- Testing FA_IR -->
   <dow:week dow="${myDow2}" fieldName="myDow2" 
   	showLabel="${true}" locale="${new Locale("fa","IR")}" context="section3" showLocale="${true}" />
   
   
   <dow:week dow="${myDow100}" activateAll="${true}" context="section100" 
    showLabel="${true}" fieldName="myDow100" locale="${new Locale("th","TH")}" showLocale="${true}" />
    
 	<g:submitButton name='doit'/>
 	</g:form>
	</body>
</html>
```
You will notice `<dow:bootstrap/>` this is because in this grails 2 app there is no bootstrap by default, this loads in relevant bootstrap js ans css files and is only needed to be called on any give page using default bootstrap method


#### Jquery views:

`jquery.gsp`:

```gsp
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'demo.label', default: 'demo runCommand')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<dow:jqueryui/>
	</head>
	<body>
	<g:form action="testjquery2" >
		Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc
	   <dow:week jqueryui="${true}"  activateAll="${false}" bindToBean="${true}" showLocale="${true}"
	     showLabel="${true}" />
	   <!-- Testing dow as 64 and locale as Iraq -->
	   <dow:week jqueryui="${true}" dow="${64 as Byte}" context="section2"  
	   showLocale="${true}"  showLabel="${true}"  fieldName="myDow" locale="${new Locale("ar","IQ")}" />
	   <!-- Testing FA_IR -->
	   <dow:week jqueryui="${true}" dow="${100 as Byte}" fieldName="myDow2" showLocale="${true}" 
	   showLabel="${true}" locale="${new Locale("fa","IR")}" context="section3" />
	   <!-- Testing th_TH -->
	   <dow:week jqueryui="${true}" activateAll="${true}" context="section100" showLocale="${true}" 
	    showLabel="${true}" fieldName="myDow100" locale="${new Locale("th","TH")}" />
	 <g:submitButton name='doit'/>
	 </g:form>
	</body>
</html>
```

`testjquery2.gsp` page that picks up the form action and reposts back to itself:
 
```gsp
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'demo.label', default: 'demo runCommand')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		<dow:jqueryui/>
	</head>
	<body>
	<g:form action="testjquery2">
		Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc    
   		<br>
	<b>Validation bean translated DOW to : ${bean.dow}
	  picked up days: ${bean.selectedDays }</b>
	  
   <dow:week  jqueryui="${true}" dow="${bean.dow}" showLocale="${true}"  showLabel="${true}" bindToBean="${true}" />

   <!-- Testing dow as 64 and locale as Iraq -->
   <dow:week jqueryui="${true}" dow="${myDow}" context="section2" showLocale="${true}"
    showLabel="${true}" fieldName="myDow" locale="${new Locale("ar","IQ")}" />
   
   <!-- Testing FA_IR -->
   <dow:week jqueryui="${true}" dow="${myDow2}" fieldName="myDow2"
   showLabel="${true}" showLocale="${true}" locale="${new Locale("fa","IR")}" context="section3" />
   
   
   <dow:week jqueryui="${true}"  dow="${myDow100}" showLocale="${true}" 
   	showLabel="${true}" activateAll="${true}" context="section100"  fieldName="myDow100" locale="${new Locale("th","TH")}" />
    
 	<g:submitButton name='doit'/>
 	</g:form>
	</body>
</html>
```

You will notice `<dow:jqueryui/>` triggered once per page, by default grails 2.4 app does not have jquery-ui. No need to declare If you already load this up. You will also notice on each call `jqueryui="${true}"` this tells it to load the template that uses jquery-ui method.


