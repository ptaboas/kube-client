Feature: Secret operations

Scenario: Create secrets
	Given a namespace "acceptance"
	When I create a secret in namespace "acceptance" with name "mysecret" and properties
		| user		|	pepe		|
		| password	| supersecret	|
	Then I check that exist the secret "#secret" with name "mysecret" in namespace "acceptance"
	And I check that secret list contains secret "#secret"