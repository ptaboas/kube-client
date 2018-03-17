Feature: Ingress operations

Scenario: Create ingress
	Given a namespace "acceptance"
	When I create an ingress in namespace "acceptance" with name "test", host "example.org" and backend "test:80"
	Then I check that exist an ingress "#ingress" with name "test" in namespace "acceptance"

Scenario: Observe ingresses
	Given a namespace "acceptance"
	When I observe ingresses in namespace "acceptance" storing events in "#ingresses"
	And I create an ingress "#ingress1" in namespace "acceptance" with name "test", host "example.org" and backend "test:80"
	Then I check that observed events "#ingresses" has the "ADDED" event of "#ingress1"
	When I clear event list "#ingresses"
	And I delete ingress "#ingress1"
	Then I check that observed events "#ingresses" has the "DELETED" event of "#ingress1"