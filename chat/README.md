# Nexus Chat Demo

This demo shows the use of Nexus with simple Java Swing client, and a simple GWT HTML5 client.

## Building and Running

The easiest way to compile and run the code is via Maven. You can build and run the server like so:

    mvn test -Pserver

You can then build and run a Java client thusly:

    mvn test -Pclient

You can build the HTML5 client like so:

    mvn package

The server will automatically serve up the HTML client on port 6502, so after building the HTML5
client and starting the server, point your browser at:

    http://localhost:6502/

Naturally you can run multiple Java clients (in separate terminal windows) or multiple HTML5
clients (in separate browser windows) or any combination of both. Go crazy!

## Discuss

Questions, comments and other communications can be expressed on the [OOO Google Group].

[OOO Google Group]: http://groups.google.com/group/ooo-libs
