# Reversi

This project demonstrates the integration of [PlayN], a cross-platform game toolkit, and [Nexus], a
cross-platform networking toolkit. It implements a very simple multiplayer lobby and Reversi game.
Neither are especially sophisticated, but they serve their primary purpose, which is to illustrate
the various bits of glue that are needed to make PlayN and Nexus play nicely together.

## Building and Running

The easiest way to compile and run the code is via Maven. You can build and run the server like so:

    mvn test -Pserver

You can then build and run a Java client thusly:

    mvn test

You can build the HTML5 client like so:

    mvn package -Phtml

The server will automatically serve up the HTML client on port 8080, so point your browser at:

    http://localhost:8080/

after building the HTML5 client and you're good to go. Naturally you can run multiple Java clients
(in separate terminal windows) or multiple HTML5 clients (in separate browser windows) or any
combination of both. Go crazy!

You can in theory also build the iOS and Android clients, though to get them to connect to the
server, you'd need to have it listen on a proper IP address, rather than localhost.

## Discuss

Questions, comments and other communications can be expressed on the [PlayN Google Group].

[PlayN]: https://code.google.com/p/playn/
[Nexus]: https://github.com/threerings/nexus
[PlayN Google Group]: http://groups.google.com/group/playn
