Feature: Namespaces operations

Scenario: Create namespace
	When I create a namespace "acceptance"
	Then I check that exist a namespace with name "acceptance"
	
Scenario: Create namespace with labels
	When I create a namespace "acceptance" with labes "version=1,app=test"
	Then I check that exist a namespace "#namespace" with name "acceptance"
	And I check that kubernetes rsource "#namespace" contains labels "version=1,app=test"
	
Scenario: Update namespace
	When I create a namespace "acceptance"
	Then I check that exist a namespace with name "acceptance"
	When I update namespace "acceptance" adding new labes "version=1,app=test"
	Then I check that exist a namespace "#namespace" with name "acceptance"
	And I check that kubernetes rsource "#namespace" contains labels "version=1,app=test"