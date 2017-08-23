Feature: Endpoint operations

Scenario: Create simple endpoint
	Given a namespace "acceptance"
	When I create an endpoint in namespace "acceptance" with name "test" and address "10.0.0.1:80"
	Then I check that exist an endpoint "#endpoint" with name "test" in namespace "acceptance"
	And I check that endpoint "#endpoint" contains a subset with addresses "10.0.0.1" and ports "80"
	
Scenario: Update endpoint
	Given a namespace "acceptance"
	When I create an endpoint in namespace "acceptance" with name "test" and address "10.0.0.1:80"
	Then I check that exist an endpoint "#endpoint" with name "test" in namespace "acceptance"
	When I update service "#endpoint" with new address "10.0.0.2"
	Then I check that exist an endpoint "#endpoint" with name "test" in namespace "acceptance"
	And I check that endpoint "#endpoint" contains a subset with addresses "10.0.0.1,10.0.0.2" and ports "80"
	
Scenario: Observe endpoints
	Given a namespace "acceptance"
	When I observe endpoints storing events in "#endpoints"
	And I create an endpoint "#endpoint1" in namespace "acceptance" with name "test" and address "10.0.0.1:80"
	Then I check that observed events "#endpoints" contains the "ADDED" event of "#endpoint1"
	When I clear event list "#endpoints"
	And I delete endpoint "#endpoint1"
	Then I check that observed events "#endpoints" contains the "DELETED" event of "#endpoint1"