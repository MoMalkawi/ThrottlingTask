# ThrottlingTask
Handles controllers that have specific throttling capabilities depending on request count

Functionality: Responds with the request url of the end-point.

Throttling restrictions:

Profile: starts throttling after 10 requests has been received from a certain IP address in a 1 minute interval.

History: starts throttling after 30 requests has been received from a certain IP address in a 1 minute interval.

Service restrictions:

History: user is denied a response and is sent a 503 Service Unavailable if he has 30 requests being throttled at any given time.

Testing:

Services: testing was done with JUnit in a Spring Boot environment.

Integration: using Mockito while creating my own request wrapper I sent requests to each end-point controller.
