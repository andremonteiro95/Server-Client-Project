# Server-Client Project
Java project with RMI Registry, TCP and SSL sockets. Synchronized client accesses. 

# How modules connect
Clients connect with server through TCP (RMI Registry) and SSL sockets. Various clients can be connected simultaneously to the server.
Admin client connects with server through simple TCP sockets in a diferent thread. Only one administrator client can be connected at a time. 

# Changing the hosts
The host names can be changed, for that effect just edit the static String objects declared at the beginning of each Java class (Server, Client and AdminClient). 

# Compiling and Usage
Open project in IntelliJ Idea (recommended) or any other Java IDE and run it.

