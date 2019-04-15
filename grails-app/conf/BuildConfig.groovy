grails.project.work.dir = 'target'
grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
		//mavenRepo "https://repo.grails.org/grails/plugins"
	}

	dependencies {
		compile group: 'com.ibm.icu', name: 'icu4j', version: '59.1'
	}

	plugins {
		build ':release:3.0.1', ':rest-client-builder:2.0.3', {
			export = false
		}
	}
}