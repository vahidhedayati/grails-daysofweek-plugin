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

#### [Class usages](https://vahidhedayati.github.io/grails-daysofweek-plugin/gapi/index.html)

![sample image](https://raw.githubusercontent.com/vahidhedayati/grails-daysofweek-plugin/master/docs/sample.png)


#### daysofweek provides a bootstrap and jquery-ui method of showing the actual days output, depending on which you prefer or already have installed


The plugin without being wired back into validation bean will return a list of selected days of the week by end user
 by default `bindToBean` is disabled is won't bind to the bean, this needs to be set to true to get it internal validation bean to pickup calculate. Only 1 element on a given form within gsp can bind to the bean. The rest will need to follow this method. Please refer to example below for a better insight.
 
To get a final value which this plugin converts to / from you need to run:
`Byte dow = DaysOfWeek.fromListToBit(params.list('daysOfWeek'))`

This  return a byte value of dow which contains which week days the end user has selected. When this is stored and reloaded in the context of above bean it will show the days as per selection.

#### Using plugin:

Simply call   `<dow:week  ....{below options}  />` :

This will return as above your own `fieldName` as the params back or by default as above `daysOfWeek` which contains a 'Byte' value which represents the bitwise value of the week days the end user has selected.

Simply wire in that same byte value back into same tag lib and set `dow` to be that value for the plugin to draw out the selected days of the week user selected previously.

```
dow="${myDow}"       = The byte value if already provided from db or something
dow="${64 as Byte}"  = you setting byte value manually this then populates relevant days.

daysOfweek="${['SUN','MON','TUE','WED','THU','FRI','SAT']}"  = either provide the dow above 
						or physical a list of a selection of  day names as shown


context="section2"   = Not needed if called only once on page, this is so that js stuff works per call 
 
showLocale="${true}" = To show the locale in showLabel only works if showLabel is on

showLabel="${true}"  = Show week days label and if showLocale true also shows locale

fieldName="myDow"    = This is the variable name of your checkbox by default daysOfWeek 

locale="${new Locale("ar","IQ")}"  = Override system locale to show which ever order of days of week
					Please note this only overrides the ordering sequence of days and not actual screen
					 text which is done via  ?lang=th lang parameter on url line.

overrideMessageLocale="${false}"  = By default true and if you provide locale then 
									messages of the end gsp will be also in that locale 

 template="/some/path/to/gsp/template/_override_plugin.gsp"   =This is to override the template to your
 						 own custom template if you so wish not to use plugin method of display days etc.
 
 bindToBean="${true}"  = This by default is false if set to true one one <dow:week on a given page 
 						can auto bind to backend bean as per controllers above.
 
 activateAll="${true}" =this is by defaut false, if there is currently no value provided and there 
 						are no active selected days, it attempts to pre-select all days for form loading up. 
 						You may wish for it to show everything as active as page starts up

```    



#### [Example controller/views: How to use daysofweek](https://github.com/vahidhedayati/grails-daysofweek-plugin/blob/master/example.md)


#### [Youtube video demonstrating plugin](https://www.youtube.com/watch?v=Jq2fXYep3QU)


### Points of reference

[Info behind weekdays byte value stackoverflow link](http://stackoverflow.com/questions/313417/whats-the-best-way-to-store-the-days-of-the-week-an-event-takes-place-on-in-a-r)
