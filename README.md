# Czech post notifier - microservices
This project was designed to create a Czech post notifier using microservice architecture. The solution is for illustration only and should not be used, only if it is not completed. This version originated when the project were missing an important parts: registration, status change notifications and a better web interface.

The main goal was to learn how to work with microservice architecture, to find out how costly to create such that architecture, deployment, benefits and compare with monolithic architecture. Another goal was to try Springboot, JMS and docker and also compare the communication between Rest and JMS.

## Architecture
The application is divided into individual modules that expose their interface out and only two or one module use them. Communication between modules is provided with Rest or JMS. That can be changed by turning on the "rest" or "jms" profile.

### Modules
* Common - Common things for all modules. -> entities and service interfaces
* Rest - Common things in rest interface. -> abstract rest client and rest service and endpoint names
* Jms - Common Things in JMS services. -> message entities, abstract JMS Service, JMS Service Interfaces and front names
* UserService - A database for application users and exposed out via the interface (Rest or JMS).
* ParcelService - Database for packages and exposed out with the interface (Rest or JMS).
* CpService - Services for obtaining data from the Czech Post web service.
* WebService - an application web interface -> logging in, working with tracking numbers and displaying package status. -> communicates with ParcelService, UserService and ApplicationService.
* ApplicationService - the main application logic -> contains the package update code and should include an automatic update of the packages and send the change notification
* NotificationService (missing) - The service that sends the notification and its interface is exposed out (Rest or JMS).

### Used technologies
* Springboot
* Springboot Rest client and controller
* Apache http client
* JMS - ActiveMQ
* Thymeleaf
* H2 database
