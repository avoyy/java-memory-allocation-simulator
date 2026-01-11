# Java Memory Allocation Simulator

## Overview
This project is a Java-based simulation of memory allocation strategies commonly taught in operating systems. The program models how processes are assigned memory using different allocation algorithms and displays the resulting memory state.

## Features
- Implements multiple memory allocation strategies:
  - First Fit
  - Best Fit
  - Worst Fit
- Processes allocation requests and tracks memory usage
- Outputs current memory blocks and assigned processes
- Modular and readable Java design

## Technologies Used
- Java
- Object-Oriented Programming principles

## How It Works
The simulator accepts a series of memory allocation requests and assigns memory based on the selected strategy. It maintains internal data structures to track free and allocated memory blocks and updates system state after each request.

## How to Run
1. Clone the repository
2. Compile the Java files: javac *.java
3. Run the program: java Program4
   
## What I Learned
- How operating systems manage memory allocation
- Applying algorithms to real system constraints
- Designing maintainable Java code using OOP principles
- Debugging and testing logic-heavy programs

## Future Improvements
- Add memory deallocation support
- Improve visualization of memory blocks
- Extend simulator to support additional algorithms
