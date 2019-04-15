daysofweek
=========

Grails Days Of week plugin using ICU4J libraries to work out internationl calendar week days 


Dependency Grails 2:

```groovy
	compile ":daysofweek:0.1"
```

[Grails 2 source](https://github.com/vahidhedayati/grails-daysofweek-plugin/tree/grails2)

Dependency Grails 3 (build.gradle):

```groovy
	compile "org.grails.plugins:daysofweek:0.1"
```
[Grails 3 source](https://github.com/vahidhedayati/grails-daysofweek-plugin)
	

Documentation
---

Typical gsp uage :

You will need boostrap for _displayWeek.gsp the template inside this plugin to work, if you wish not to use
bootstrap then override the template call shown further down.

```gsp
<!-- do you need bootstrap on your grails 2 application  ? -->
	<dow:bootstrap/>
	
 <dow:week />
  
 <dow:week dow="${64 as Byte}" context="section4" />
  
 <dow:week daysOfweek="${['SUN','MON']}"  context="section5"/>
 <dow:week daysOfweek="${['SUN','MON','SAT']}"  context="section6" />
  
   
 <dow:week locale="${new Locale("ENGLISH", "US")}"  context="section7" />
 <dow:week locale="${new Locale("", countryCode)}" context="section8"  />
 <dow:week locale="${Locale.EN}" context="section9" />
   
 <dow:week template="/some/path/to/gsp/template/_override_plugin.gsp"  context="section11" />
 
 
  <!-- If you are using it many times on the page introduce and change context value. -->
 
 
	<!-- Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc -->  
   <dow:week />
  
   <!-- Testing dow as 64 and locale as Iraq -->
   <dow:week dow="${64 as Byte}" context="section2"  fieldName="myDow" locale="${new Locale("ar","IQ")}" />
   
   <!-- Testing FA_IR -->
   <dow:week dow="${100 as Byte}" fieldName="myDow2"   locale="${new Locale("fa","IR")}" context="section3" />
 
 ```
 
 
 
 Depending on end user's locale the calendar starting from
 
 ```
Around the world
Nation	Typical hours worked per week	Working week
Indonesia	40	Monday–Friday
Iran	45	Saturday-Thursday/Saturday-Wednesday
Iraq	40	Sunday–Thursday
Ireland	40	Monday–Friday
```


![sample image](https://raw.githubusercontent.com/vahidhedayati/grails-daysofwqeek-plugin/master/docs/sample.png)

http://stackoverflow.com/questions/313417/whats-the-best-way-to-store-the-days-of-the-week-an-event-takes-place-on-in-a-r

/sun=1, mon=2, tue=4, wed=8, thu=16, fri=32, sat=64.

The plugin without being wired back into validation bean will return a list of selected days of the week by end user

To get a final value which this plugin converts to / from you need to run

Byte dow = DaysOfWeek.fromListToBit(params.list('daysOfWeek'))

Will return a byte value of dow which contains which week days the end user has selected. When this is stored and reloaded in the context of above bean it will show the days as per selection, sample controller to show / save form which reshows it as follows:

```groovy
TestController {

	def index() {
		render view: 'index'
	}
	def test2() {
		Byte dow = DaysOfWeek.fromListToBit(params.list('daysOfWeek'))
		
		//not used examples of how to reuse on a single page as per index
		Byte myDow = DaysOfWeek.fromListToBit(params.list('myDow'))
		
		Byte myDow2 = DaysOfWeek.fromListToBit(params.list('myDow2'))
		
		render view:'test',model:[dow:dow]
	}

}
```


`/views/index.gsp` :

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
   		<dow:week />


   <!-- Testing dow as 64 and locale as Iraq -->
   <dow:week dow="${64 as Byte}" context="section2"  fieldName="myDow" locale="${new Locale("ar","IQ")}" />
   
   <!-- Testing FA_IR -->
   <dow:week dow="${100 as Byte}" fieldName="myDow2"   locale="${new Locale("fa","IR")}" context="section3" />
   
   
		 <g:submitButton name='doit'/>
 	</g:form>
 
 ```
 
 
	</body>
</html>



`/views/test.gsp` :


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
<b>
	This is return page user selected ${dow} ${new Date()}</b>
	<g:form action="test2">
		Testing default dow as per user locale,try whatever your url is and add ?lang=en_GB ?lang=fa etc    
   		<dow:week dow="${dow}"/>
 		<g:submitButton name='doit'/>
 	</g:form>
	</body>
</html>
```