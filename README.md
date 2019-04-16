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

###[Class usages](https://vahidhedayati.github.io/grails-daysofweek-plugin/gapi/index.html)

![sample image](https://raw.githubusercontent.com/vahidhedayati/grails-daysofweek-plugin/master/docs/sample.png)


####daysofweek provides a bootstrap and jquery-ui method of showing the actual days output, depending on which you prefer or already have installed


The plugin without being wired back into validation bean will return a list of selected days of the week by end user
 by default `bindToBean` is disabled is won't bind to the bean, this needs to be set to true to get it internal validation bean to pickup calculate. Only 1 element on a given form within gsp can bind to the bean. The rest will need to follow this method. Please refer to example below for a better insight.
 
To get a final value which this plugin converts to / from you need to run:
`Byte dow = DaysOfWeek.fromListToBit(params.list('daysOfWeek'))`

This  return a byte value of dow which contains which week days the end user has selected. When this is stored and reloaded in the context of above bean it will show the days as per selection.

###[Example controller/views: How to use daysofweek](https://github.com/vahidhedayati/grails-daysofweek-plugin/example.md)


###Points of reference

[Info behind weekdays byte value stackoverflow link](http://stackoverflow.com/questions/313417/whats-the-best-way-to-store-the-days-of-the-week-an-event-takes-place-on-in-a-r)
