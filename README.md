easyScrum
=========

Simple Scrum Tool to manage teams and primarily create burn down charts.

Artefacts: 
* https://sourceforge.net/projects/easy-scrum/

Dependencies
* https://github.com/puel/jsf-burndown-chart
* https://github.com/adangel/simple-burndown-chart

Deployment:
* Java 7
* Glassfish 3.x -- Glassfish 4 will not work due to Hibernate!
* mySQL - other DBs not tested till now
* jdbc/easyScrum data source in container
* DB updates will be done automatically - please ensure DDL permissions on the schema used