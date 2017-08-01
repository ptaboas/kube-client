Feature: Kubernetes client


Scenario Outline: Create a non secured simple client
	When I crete a kubernetes client "#client" with next options:
		| server | <server> |
	Then I check that I get service list successfully using client "#client"
	Examples:
		| server 				|
		| http://127.0.0.1:8080 |
		| 127.0.0.1:8080 |
		| 127.0.0.1 |

Scenario: Create a secured simple client
	Given exist a service account CA certificate in file "serviceaccount/ca.crt"
	And exist a service account token in file "serviceaccount/token"
	When I crete a kubernetes client "#client" with next options:
		| server 	| https://127.0.0.1:443 |
		| caFile 	| serviceaccount/ca.crt |
		| tokenFile	| serviceaccount/token 	|
	Then I check that I get service list successfully using client "#client"
	
Scenario: Unauthorized request
	Given exist a service account CA certificate in file "serviceaccount/ca.crt"
	And exist a service account token in file "serviceaccount/token" with content "badtoken"
	When I crete a kubernetes client "#client" with next options:
		| server 	| https://127.0.0.1:443 |
		| caFile 	| serviceaccount/ca.crt |
		| tokenFile	| serviceaccount/token 	|
	And I try to retrieve service list "#result" using client "#client"
	Then I check that result "#result" is failure
	And I check that failure result "#result" contains message "Received status 401: Unauthorized"