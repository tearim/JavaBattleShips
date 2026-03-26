# UltraVersenker

A command‑line implementation of the classic Battleship game in Java, featuring ANSI‑colored console output for a more vivid and engaging terminal experience. When we were assigned to build a Battleship game as part of learning Java, I decided to go a bit further and explore what could be done with Java in a pure CLI environment. That led me to create a small “graphic” engine (which I’ll publish as a separate repo) built around chaxels — character‑pixels that allow fine‑grained control over foreground color, background color, z‑index layering, and even faux opacity.
The result is that this project became not only a Battleship game, but also a demo of the chaxel engine in action — including effects like translucent clouds drifting over the sun.

The name **UltraVersenker** is a nod to the German word Schiffeversenken (Battleship) and reflects my attempt to create an “ultimate” command‑line version of the game. With the help of my chaxel‑based CLI engine, the goal was to push a simple terminal Battleship far beyond the usual text‑only experience.

<img width="1115" height="628" alt="image" src="https://github.com/user-attachments/assets/d0cda112-5ca6-4fd1-b85d-4ddce9fd0971" />
(*Here you can see a typical battle: your main ship is down, you fire at the enemy, the future is uncertain. Yet the semitransparent cloud is over the sun. Chaxel engine works, even when the world is not*)

## AI usage policy:
Because this was a learning project, all Java code was written by me without the assistance of AI/LLMs (aside from standard inline completion tools). The JavaDocs and this Markdown file were generated with the help of an LLM and then edited by me.



## Features

- Classic Battleship gameplay with human vs AI (computer)
- Computer Player implementation (AI) with four difficulty levels (easy, medium, hard, and hard+). 
- ANSI styling for colorful and formatted console output using the custom Fansi utility
- Keyboard input handling via jnativehook
- Custom rendering system with chaxels (character-based graphics)
- Modular architecture with utilities, game logic, and bridges

## Requirements

- Java 24 or higher
- Maven 3.6+
- Windows (currently, due to CLI launcher specifics)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/UltraVersenker.git
   cd UltraVersenker
   ```

2. Build with Maven:
   ```bash
   mvn clean compile
   ```

## Usage

Run the game directly with Maven:
```bash
mvn exec:java -Dexec.mainClass="JavaBattleShips.UltraVersenker"
```

Or package and run the JAR:
```bash
mvn package
java -jar target/UltraVersenker-1.0-SNAPSHOT.jar
```

The game will launch in a new command prompt window on Windows. Or you can run it in your favorite IDE (I have tested it in IntelliJ and VS Code).

## Project Structure

```
UltraVersenker/
├── src/
│   ├── main/java/
│   │   ├── commonutils/          # Utility classes
│   │   │   ├── Ansi.java
│   │   │   ├── Fansi.java        # ANSI styling utility
│   │   │   ├── ParamLine.java
│   │   │   ├── STools.java
│   │   │   └── menudispatcher/   # Menu system utilities
│   │   └── JavaBattleShips/      # Main game package
│   │       ├── bridges/          # Abstractions for input/output
│   │       ├── chaxels/          # Character-based graphics
│   │       ├── chaxelshapes/     # Predefined shapes
│   │       ├── clrender/         # Command-line rendering
│   │       ├── keymaster/        # Keyboard input handling
│   │       ├── logic/            # Game logic and AI
│   │       └── relauncher/       # CLI launcher
│   └── test/java/                # Unit tests
├── pom.xml                       # Maven configuration
├── README.md                     # This file
├── .gitignore                    # Git ignore rules
└── LICENSE                       # License file
```

To run the game from an IDE, you need to go to the **src/main/java/JavaBattleShips/Ultraversenker.java**

## Testing

Run tests with Maven:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Run `mvn test` to ensure everything passes
6. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Uses [jnativehook](https://github.com/kwhat/jnativehook) for global keyboard input
- Custom Fansi library for ANSI console styling (will be published as a separate repo :p)
- SmartFutureCampus for support and help
