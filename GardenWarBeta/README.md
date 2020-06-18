# Grazy Garden War Beta

A multi-player networking, turn-based game with the theme of climate change

## Getting Started
This project is built with Intellij, it is therefore recommended to 
use Intellij IDE to open the project to facilitate configurations
The project consists of two projects. First one is CrazyGardenWarBetaServer,
the other is CrazyGardenWarBetaClient. One needs to run the server program first,
enter a port number and then open the client program and connect to the server
with server's ip address and the port number that one just entered.

### Prerequisites

JDK 8+, Javafx 8, intellij or similar IDEs

### Project Configuration notes: 

Since Javafx9, Javafx has detached itself from jdk and became a dependency. 
hence, in order to run this program with javafx 9+, one needs to inject 
Javafx as a dependency.

## Deployment
Deploy as jar or a batch file. 
## Built With

* [JDK 8](https://www.oracle.com/java/technologies/javase-downloads.html)
  
## Author

* **Jianqiu Chen**

## License
This project is free of use by anyone

## Improvement Ideas
1. Better styled UI
2. Rematching without having to restart the program.
3. More variations in the disasters
4. Square Hover functionalities
