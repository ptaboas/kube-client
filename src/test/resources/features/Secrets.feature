Feature: Secret operations

Scenario: Create secrets
	Given a namespace "acceptance"
	When I create a secret in namespace "acceptance" with name "mysecret" and properties
		| user		|	pepe		|
		| password	| supersecret	|
	Then I check that exist the secret "#secret" with name "mysecret" in namespace "acceptance"
	
Scenario: Update secret adding data
	Given a namespace "acceptance"
	When I create a secret in namespace "acceptance" with name "mysecret" and properties
		| user		| pepe			|
		| password	| supersecret	|
	Then I check that exist the secret "#secret" with name "mysecret" in namespace "acceptance"
	When I update the secret "#secret" adding new data:
		| key		| thekey	|
	Then I check that secret "mysecret" in namespace "acceptance" contains data "key" equals to "thekey"
