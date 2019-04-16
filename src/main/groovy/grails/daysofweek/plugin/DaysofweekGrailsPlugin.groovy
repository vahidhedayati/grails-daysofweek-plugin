package grails.daysofweek.plugin

import grails.plugins.Plugin

class DaysofweekGrailsPlugin extends Plugin {
    def version = "1.0"
    def grailsVersion = "2.4 > *"
    def title = "Days Of Week plugin"
    def description = """Grails plugin using ICU4J libraries to calculate international week day
         calendars returns dow for your application in byte format"""
    def documentation = "https://github.com/vahidhedayati/grails-daysofweek-plugin"
    def license = "APACHE"
    def developers = [name: 'Vahid Hedayati', email: 'badvad@gmail.com']
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/vahidhedayati/grails-daysofweek-plugin/issues']
    def scm = [url: 'https://github.com/vahidhedayati/grails-daysofweek-plugin']
}