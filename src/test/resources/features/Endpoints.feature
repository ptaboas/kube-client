Feature: Endpoint operations

Scenario: Observe endpoints
	Given a namespace "acceptance"
	When I observe endpoints storing events in "#endpoints"
	And I create an endpoint "#endpoint1" in namespace "acceptance" with name "test" and address "10.0.0.1:80"
	Then I check that observed events "#endpoints" contains the "ADDED" event of "#endpoint1"
	When I clear event list "#endpoints"
	And I delete endpoint "#endpoint1"
	Then I check that observed events "#endpoints" contains the "DELETED" event of "#endpoint1"
	
Scenario: Get endpoints
	Given a namespace "acceptance"
	When I create an endpoint "#endpoint1" in namespace "acceptance" with name "test" and address "10.0.0.1:80"
	Then I check that endpoint list contains the endpoint "#endpoint1"
	