Feature: Service account operations

Scenario: Get service account
	When I create a namespace "acceptance"
	Then I check that exist a service account in namespace "acceptance" with name "default"
	
Scenario: Create service account
	Given a namespace "acceptance"
	When I create a service account in namespace "acceptance" with name "test"
	Then I check that exist a service account "#serviceaccount" with name "test" in namespace "acceptance"
	
Scenario: Update service account
	Given a namespace "acceptance"
	When I create a service account in namespace "acceptance" with name "test"
	Then I check that exist a service account "#serviceaccount" with name "test" in namespace "acceptance"
	When I create a secret in namespace "acceptance" with name "test" and properties
		| user		|	pepe		|
		| password	| supersecret	|
	And I update the service acount "#serviceaccount" adding new secret "test"
	Then I chech that service account "test" in namespace "acceptance" contains secret "test"
