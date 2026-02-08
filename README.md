# Predetermined Balls - Physics Simulation

A high-performance ball collision simulation using JavaFX with spatial partitioning optimization. This project demonstrates efficient collision detection using a chunk-based system and predetermined ball positions that can be saved and loaded.

![Java](https://img.shields.io/badge/Java-11+-blue.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-Required-green.svg)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Technical Details](#technical-details)
- [How It Works](#how-it-works)
- [Customization](#customization)

## 🎯 Overview

Predetermined Balls is a physics simulation application that simulates realistic ball collisions in a 2D space. The project uses advanced spatial partitioning techniques to efficiently handle thousands of balls simultaneously. The simulation includes gravity, collision detection, energy loss, and the ability to color balls based on an input image.

## ✨ Features

- **High-Performance Collision Detection**: Uses a chunk-based spatial partitioning system to optimize collision detection for large numbers of balls
- **Realistic Physics**: Implements Verlet integration for stable and realistic physics simulation
- **Gravity Simulation**: Balls are affected by gravity and bounce realistically
- **Energy Loss**: Configurable energy loss during collisions for realistic damping
- **Image-Based Coloring**: Balls can be colored based on pixel colors from an input image
- **Save/Load System**: Save and load predetermined ball positions for reproducible simulations
- **Configurable Parameters**: Easy-to-modify physics parameters (gravity, ball size, precision, etc.)
- **Real-time Rendering**: Smooth animations using JavaFX AnimationTimer
- **Scalable**: Handles up to 12,000+ balls efficiently

## 📦 Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher
- **JavaFX SDK**: Download from [OpenJFX](https://openjfx.io/)
- An image file named `img.png` in the project root directory (for ball coloring)

## 🚀 Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/omselkara/Predetermined-Balls.git
   cd Predetermined-Balls
   ```

2. **Download JavaFX SDK**:
   - Download the JavaFX SDK from [https://openjfx.io/](https://openjfx.io/)
   - Extract it to a location on your system
   - Note the path to the `lib` folder (e.g., `C:\javafx-sdk-21\lib` on Windows or `/usr/local/javafx-sdk-21/lib` on Linux/Mac)

3. **Add an image file**:
   - Place an image file named `img.png` in the same directory where you will run the application (typically the compiled output directory)
   - This image will be used to color the balls based on their final positions

## 💻 Usage

### Compiling the Project

Navigate to the `src` directory and compile all Java files:

**Windows**:
```bash
cd src
javac *.java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls
```

**Linux/Mac**:
```bash
cd src
javac *.java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls
```

**Note**: The compiled `.class` files will be generated in the `src` directory. For better organization, you can compile to a separate output directory using the `-d` flag.

### Running the Application

After compilation, run the application from the directory containing the compiled classes. Make sure `img.png` is in the same directory:

**Windows**:
```bash
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls Main
```

**Linux/Mac**:
```bash
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls Main
```

**Important**: The application looks for `img.png` and `out.txt` in the current working directory. Ensure these files are present where you run the java command.

## ⚙️ Configuration

The simulation behavior can be customized by modifying constants in the source files:

### Main.java
- `width` and `height` (line 32-33): Window dimensions (default: 600x600)
- `col` and `row` (line 38): Number of chunks for spatial partitioning (default: 150x150)
- `count` (line 45): Number of balls to simulate (default: 12,000)
- `load` (line 54): Set to `true` to load predetermined positions from `out.txt` (in current working directory)
- `save` (line 55): Set to `true` to save ball positions to `out.txt` (in current working directory)
- `render` (line 57): Set to `true` to enable rendering, `false` for faster computation

### Ball.java
- `dx` (line 12): Ball diameter (default: 6.0)
- `g` (line 13): Gravity strength (default: 0.3)
- `precision` (line 14): Collision detection precision/iterations (default: 10)
- `energyLoss` (line 15): Energy loss coefficient during collisions (default: 0.7)

## 📁 Project Structure

```
Predetermined-Balls/
├── src/
│   ├── Main.java          # Main application class with JavaFX setup and game loop
│   ├── Ball.java          # Ball physics and collision logic
│   ├── Chunk.java         # Spatial partitioning chunk implementation
│   ├── FileInput.java     # File reading utility
│   ├── FileOutput.java    # File writing utility
│   └── FastRGB.java       # Fast pixel color extraction from images
├── out/                   # Output directory (example, may vary)
│   ├── *.class            # Compiled Java classes
│   ├── img.png            # Input image (must be in working directory)
│   └── out.txt            # Saved ball positions (generated when save=true)
└── README.md              # This file
```

## 🔧 Technical Details

### Physics Engine
- **Integration Method**: Verlet integration for stable physics simulation
- **Time Step**: Configurable through `Ball.precision` parameter
- **Collision Response**: Impulse-based collision resolution with energy loss

### Spatial Partitioning
The simulation uses a grid-based spatial partitioning system (chunks) to optimize collision detection:
- The simulation space is divided into a grid of chunks
- Each ball is assigned to one or more chunks based on its position
- Collision detection only checks balls in nearby chunks
- Reduces collision detection complexity from O(n²) to approximately O(n)

### Performance Optimizations
1. **Chunk System**: Only checks collisions with balls in adjacent chunks
2. **Fast RGB Access**: Uses direct byte buffer access for image pixel reading
3. **Separation of Concerns**: Physics calculations separated from rendering
4. **Configurable Precision**: Adjustable collision detection iterations

## 🎮 How It Works

1. **Initialization**:
   - Creates a grid of chunks for spatial partitioning
   - Loads an image (`img.png` from current working directory) for ball coloring
   - Optionally loads predetermined positions from `out.txt` (if load=true)

2. **Ball Spawning**:
   - Balls are spawned gradually from the left side of the screen
   - Initial velocity is applied horizontally
   - Ball colors are determined from the image at saved positions (if loading)

3. **Physics Update Loop**:
   - Apply gravity to all balls
   - Update ball positions using Verlet integration
   - Check and resolve collisions with nearby balls
   - Keep balls within window boundaries
   - Update chunk assignments

4. **Rendering**:
   - Clear the canvas
   - Draw all balls with their colors
   - Update frame counter and FPS

5. **Save/Load System**:
   - When saving is enabled, ball positions are recorded after the simulation stabilizes
   - These positions can be loaded in future runs to reproduce the same visual pattern

## 🎨 Customization

### Creating Different Patterns

1. **Modify spawn parameters** in `Main.java` (lines 278-299):
   - Change spawn position (x, y coordinates)
   - Adjust initial velocity
   - Modify spawn rate and amount

2. **Change the input image**:
   - Replace `img.png` with your own image
   - The simulation will use colors from this image

3. **Adjust physics parameters**:
   - Increase `g` for stronger gravity
   - Decrease `energyLoss` for more elastic collisions
   - Increase `precision` for more accurate (but slower) collision detection

### Performance Tuning

- **For more balls**: Increase `count` (may require adjusting chunk grid size)
- **For faster simulation**: Set `render = false` and `save = true`
- **For smoother rendering**: Decrease `count` or increase chunk grid resolution
- **For more accurate physics**: Increase `Ball.precision` (increases computation time)

## 📝 Notes

- The first run should have `save = true` to generate the predetermined positions in `out.txt`
- Subsequent runs can use `load = true` to use the saved positions from `out.txt`
- The simulation is deterministic when using the same seed and loaded positions
- Ensure `img.png` exists in the current working directory (where you run the java command) before running
- Both `img.png` and `out.txt` should be in the same directory as the compiled `.class` files when running the application

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest new features
- Submit pull requests
- Improve documentation

## 📄 License

This project is open source and available for educational and personal use.

---

**Enjoy experimenting with the simulation!** 🎉