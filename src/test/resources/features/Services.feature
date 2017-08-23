Feature: Service operations

Scenario: Create simple service
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and port 80
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port |
	  | 80   |
	And I check that service "#service" does not have selector
	When I retrieve all services "#services" in namespace "acceptance"
	Then I check that services "#services" contains the service "#service"
	
Scenario: Create simple service with selectors
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test", port 80 and selector "app=test"
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port |
	  | 80   |
	And I check that service "#service" contains selectors "app=test"
	
Scenario: Create service with multiple ports
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and next ports:
		| port	| protocol	| name	|
		| 80  	| TCP	 	| http	|
		| 443	| TCP		| https	|
		| 53	| UDP		| dns	|
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port	| protocol	|
	  | 80   	| TCP	 	|
	  | 443		| TCP	 	|
	  | 53		| UDP	 	|
	  
Scenario: Update service replacing all ports
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and port 80
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port |
	  | 80   |
	When I update service "#service" with new port 8080
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port |
	  | 8080 |
	  
Scenario: Update service adding new port
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and port "http" 80
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port |
	  | 80   |
	When I update service "#service" adding new port "https" 443
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	And I check that service "#service" has next ports:
	  | port	|
	  | 80		|
	  | 443		|

Scenario: Delete service
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and port 80
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	When I delete service "#service"
	Then I check that do not exist the service "#service"
	
Scenario: Observe services
	Given a namespace "acceptance"
	When I observe services storing events in "#services"
	And I create a service "#service1" in namespace "acceptance" with name "test" and port 80
	Then I check that observed events "#services" contains the "ADDED" event of "#service1"
	When I clear event list "#services"
	And I delete service "#service1"
	Then I check that observed events "#services" contains the "DELETED" event of "#service1"
	
Scenario: Observe list of service in namespace from given index
	Given a namespace "acceptance"
	When I create a service in namespace "acceptance" with name "test" and port 80
	Then I check that exist a service "#service" with name "test" in namespace "acceptance"
	When I get services "#services" in namespace "acceptance"
	And I observe services in namespace "acceptance" from index in resource list "#services" storing events in "#events"
	And I create a service "#service1" in namespace "acceptance" with name "other" and port 80
	Then I check that observed events "#events" contains the "ADDED" event of "#service1"
	