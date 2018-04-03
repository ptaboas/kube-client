Feature: Pod operations

Scenario: Create a Pod
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and image "nginx" and port 80
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	And I check that pod "#pod" contains a not null status host ip
	And I check that pod "#pod" contains a not null status pod ip
	
Scenario: Create a Pod with labels
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx", port 80 and labels "app=test,version=1,check=true"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	And I check that pod "#pod" contains a not null status host ip
	And I check that pod "#pod" contains a not null status pod ip
	And I check that kubernetes rsource "#pod" contains labels "app=test,version=1,check=true"
	
Scenario: Update pod
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and image "nginx"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	When I update pod "#pod" adding new label "app=test,version=1"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that kubernetes rsource "#pod" contains labels "app=test,version=1"
	When I update pod "#pod" adding new label "check=true" waiting a while after get the updatter
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that kubernetes rsource "#pod" contains labels "app=test,version=1,check=true"
	
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

Scenario: Execute container pod command
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and container images "nginx,redis"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	When I execute next command "cat /etc/hostname" in container "nginx" of pod "#pod" getting result "#result"
	Then I check command result "#result" contains value "test" 
	
Scenario: Execute multi container pod command failure when container is not specified
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test" and container images "nginx,redis"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	When I try to execute next command "cat /etc/hostname" in pod "#pod" getting result "#result"
	Then I check that result "#result" is failure
	And I check that failure result "#result" contains message "Must specify container name: [nginx, redis]"

Scenario: Observe pods
	Given a namespace "acceptance"
	When I observe pods in namespace "acceptance" storing events in "#pods"
	And I create a pod "#pod1" in namespace "acceptance" with name "test" and image "nginx"
	Then I check that observed events "#pods" has the "ADDED" event of "#pod1"
	When I clear event list "#pods"
	And I delete pod "#pod1"
	Then I check that observed events "#pods" has the "DELETED" event of "#pod1"
	
Scenario: Create pod with httpget readyness probe
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and http readiness probe "/health" in port 80
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has http readiness probe "/health"
	
Scenario: Create pod with tcp readyness probe
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and tcp readiness probe in port 80
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has tcp readiness probe in port 80
	
Scenario: Create pod with environment variables
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and environment variables "ENV=test"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	When I execute next command "printenv ENV" in pod "#pod" getting result "#result"
	Then I check command result "#result" contains value "test"
	
Scenario:  Create pod with volume mount
	Given a namespace "acceptance"
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and volume mount "/tmp"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	
Scenario:  Create pod with secret volume mount
	Given a namespace "acceptance"
	When I create a secret in namespace "acceptance" with name "mysecret" and properties
		| user		| pepe			|
		| password	| supersecret	|
	When I create a pod in namespace "acceptance" with name "test", image "nginx" and secret "mysecret" mounted in "/tmp"
	Then I check that exist a pod "#pod" with name "test" in namespace "acceptance"
	And I check that pod "#pod" has "RUNNING" state
	