Feature: Pod operations

Scenario: Create a Pod
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and image "nginx"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	
Scenario: Get pods
	Given a namespace "acceptance"
	When I create a pod "#pod" in namespace "acceptance" with name "test" and image "nginx"
	And I get pods "#pods" in namespace "acceptance"
	Then I check that pod list "#pods" contains pod "#pod"
	
Scenario: Delete pod
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and image "nginx"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I delete pod "#pod"
	Then I check that do not exist the pod "#pod"
	
Scenario: Execute pod command
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and readiness probe "cat /tmp/healthy" 
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	And I check that pod "#pod" is not ready
	When I execute next command "cat /etc/hostname" in pod "#pod" getting result "#result"
	Then I check command result "#result" contains value "test" 
	When I execute next command "touch /tmp/healthy" in pod "#pod"
	Then I check that pod "#pod" is ready
	
Scenario: Execute pod command failure
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and readiness probe "cat /tmp/healthy" 
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	And I check that pod "#pod" is not ready
	When I try to execute next command "cat /etc/notfound" in pod "#pod" getting result "#result"
	Then I check that result "#result" is failure
	And I check that failure result "#result" contains message "Error executing command: cat: /etc/notfound: No such file or directory"

Scenario: Observe pods
	Given a namespace "acceptance"
	When I observe pods storing events in "#pods"
	And I create a pod "#pod1" in namespace "acceptance" with name "test" and image "nginx"
	Then I check that observed events "#pods" has the "ADDED" event of "#pod1"
	When I clear event list "#pods"
	And I delete pod "#pod1"
	Then I check that observed events "#pods" has the "DELETED" event of "#pod1"