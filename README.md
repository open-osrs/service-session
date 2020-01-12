# service-session
Handles UUID generation and keeps track of active user count
  
# Setup  
  
Import the project using gradle.  
After gradle finishes init, simply launch service.SpringBootWebApplication  
  
The following endpoints exist:  
http://localhost:8080/session/new : Generates a new session and returns it to the requester  
http://localhost:8080/session/ping?uuid=uuid-here : Refreshes the session countdown for a given uuid  
http://localhost:8080/session/count : Displays the number of active users
