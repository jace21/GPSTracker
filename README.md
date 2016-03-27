# GPSTracker
Threaded Client Side GPS Tracker for Android OS using TCP sockets to connect to a multithreaded server.

#Features
GPS Tracker is a multithreaded client side application that records the current time and location at 5 minute intervals and pings the server of its location and time. In order to connect to the server, the client must enter a matching login and password of an existing client within the server's database of users. The client sends an encrypted packet that the server will decrypt and pattern match before sending a confirmation to the client before allowing them to connect. 

#Design
Designed as a background process that will continue to record GPS data and time at 5 minute intervals and send packets to the server regardless if phone is in sleep mode.  It was built to run in the background and ping at intervals to lower CPU and battery usage so that it can run at a minimum of 8+ hours. It has a basic UI that shows the current location and updates the UI according based on the current location

#Upcoming Features
A current improvement in progress are substantial movements over to Google’s new function calls that can keep the location from going “stale”. This helps prevent issues such as changing locations and not having updated GPS location. A potential contender if power consumption was a big issue is to keep it from pinging if the location remains the same for a certain amount of intervals. The most important feature in mind at the current time though is a safety net for the client side where when a client is currently connected to the server but then the server goes down. What the client will do in this scenario is go into safe mode where it will continue to log its data because we want consistency and no gaps in our recorded data. What this safe mode means is that our client will save subsequent data packets into a data file and when it is able to reconnect to the server it will send the data file to the server to fill in the missing gaps of the pings. This will help prevent data loss in the event of a downed server.
