Feature: Service account operations

Scenario: Get service account
	When I create a namespace "acceptance"
	Then I check that exist a service account in namespace "acceptance" with name "default"