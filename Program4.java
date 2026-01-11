/*
 * Memory Allocation Simulator
 *
 * Author: Avoy Tejada
 *
 * Description:
 * Implements a command-line memory allocation simulator supporting
 * First Fit, Best Fit, and Worst Fit algorithms. The system processes
 * memory requests, releases, compaction, and status reporting to model
 * operating system memory management behavior.
 */

import java.util.Scanner;

public class Program4
{
   private Scanner inputScanner;
   private MemoryManager memoryManager;

   //***********************************************************
   //
   // Method: main
   //
   // Description: The main method of the program. It creates an
   // object of this class and delegates all work to non-static
   // methods.
   //
   // Parameters: String[] args - command line arguments
   //
   // Returns: N/A
   //
   //***********************************************************
   public static void main(String[] args)
   {
      Program4 program = new Program4();
      program.developerInfo();
      program.runProgram();
   }

   //***********************************************************
   //
   // Method: Program4 (Constructor)
   //
   // Description: Default constructor. It initializes the
   // scanner used for user input.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   public Program4()
   {
      inputScanner = new Scanner(System.in);
   }

   //***********************************************************
   //
   // Method: developerInfo (Non Static)
   //
   // Description: Prints the developer information for this
   // program.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   public void developerInfo()
   {
      System.out.println("Name: Avoy Tejada");
      System.out.println("Course: COSC 4302 Operating System Concepts");
      System.out.println("Program #: Four\n");
   } // End of the developerInfo method

   //***********************************************************
   //
   // Method: runProgram
   //
   // Description: Coordinates the overall flow of the program.
   // It obtains the initial amount of memory from the user,
   // creates the MemoryManager object, and starts the command
   // processing loop.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   public void runProgram()
   {
      long totalBytes = readInitialMemoryAmount();
      memoryManager = new MemoryManager(totalBytes);
      processAllocatorCommands();
   }

   //***********************************************************
   //
   // Method: readInitialMemoryAmount
   //
   // Description: Prompts the user for the initial amount of
   // memory in megabytes, validates the input, and returns the
   // corresponding number of bytes.
   //
   // Parameters: None
   //
   // Returns: long - the total number of bytes of memory
   //
   //***********************************************************
   private long readInitialMemoryAmount()
   {
      boolean validInput;
      long totalBytes;
      totalBytes = 0L;
      validInput = false;

      while (!validInput)
      {
         System.out.print("Enter the initial amount of memory: ");
         String inputLine = inputScanner.nextLine();
         String trimmedLine = inputLine.trim();

         if (trimmedLine.length() > 0)
         {
            String[] tokens = trimmedLine.split("\\s+");
            String firstToken = tokens[0];

            try
            {
               int megabytes = Integer.parseInt(firstToken);

               if (megabytes > 0)
               {
                  totalBytes = (long) megabytes * 1024L * 1024L;
                  validInput = true;
               }
               else
               {
                  System.out.println("Invalid amount. Please enter a positive integer value.");
               }
            }
            catch (NumberFormatException exception)
            {
               System.out.println("Invalid input. Please enter a positive integer value.");
            }
         }
         else
         {
            System.out.println("Invalid input. Please enter a positive integer value.");
         }
      }

      return totalBytes;
   }

   //***********************************************************
   //
   // Method: processAllocatorCommands
   //
   // Description: Repeatedly prompts the user for commands and
   // delegates each valid command to the MemoryManager. The loop
   // ends when the user enters the X command.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   private void processAllocatorCommands()
   {
      boolean done;
      done = false;

      while (!done)
      {
         System.out.print("allocator>");
         String commandLine = inputScanner.nextLine();
         String trimmedCommand = commandLine.trim();

         if (trimmedCommand.length() > 0)
         {
            done = handleCommand(trimmedCommand);
         }
      }
   }

   //***********************************************************
   //
   // Method: handleCommand
   //
   // Description: Parses a single command line and calls the
   // appropriate method in the MemoryManager class. Returns
   // true when the user chooses to exit the program.
   //
   // Parameters: String commandLine - the raw command entered
   // by the user
   //
   // Returns: boolean - true if the program should terminate;
   // false otherwise
   //
   //***********************************************************
   private boolean handleCommand(String commandLine)
   {
      boolean shouldExit;
      shouldExit = false;

      String[] tokens = commandLine.split("\\s+");
      String commandWord = tokens[0].toUpperCase();

      if (commandWord.equals("RQ"))
      {
         handleRequestCommand(tokens);
      }
      else if (commandWord.equals("RL"))
      {
         handleReleaseCommand(tokens);
      }
      else if (commandWord.equals("C"))
      {
         memoryManager.compactMemory();
      }
      else if (commandWord.equals("STAT"))
      {
         memoryManager.printMemoryStatus();
      }
      else if (commandWord.equals("X"))
      {
         shouldExit = true;
      }
      else
      {
         System.out.println("Invalid command. Please enter RQ, RL, C, STAT, or X.");
      }

      return shouldExit;
   }

   //***********************************************************
   //
   // Method: handleRequestCommand
   //
   // Description: Validates and processes an RQ command. It
   // extracts the process id, requested number of bytes, and
   // allocation strategy, then calls the MemoryManager to
   // perform the allocation.
   //
   // Parameters: String[] tokens - the tokens that make up the
   // command line
   //
   // Returns: N/A
   //
   //***********************************************************
   private void handleRequestCommand(String[] tokens)
   {
      if (tokens.length != 4)
      {
         System.out.println("Invalid RQ command. Usage: RQ <ProcessId> <Bytes> <F|B|W>");
      }
      else
      {
         String processId = tokens[1];
         String sizeToken = tokens[2];
         String strategyToken = tokens[3];

         try
         {
            long requestedBytes = Long.parseLong(sizeToken);
            char strategy = 'F';

            if (strategyToken.length() > 0)
            {
               strategy = Character.toUpperCase(strategyToken.charAt(0));
            }

            if (requestedBytes <= 0L)
            {
               System.out.println("Requested size must be greater than zero.");
            }
            else if (strategy != 'F' && strategy != 'B' && strategy != 'W')
            {
               System.out.println("Invalid strategy. Use F, B, or W.");
            }
            else
            {
               memoryManager.requestMemory(processId, requestedBytes, strategy);
            }
         }
         catch (NumberFormatException exception)
         {
            System.out.println("Invalid size value. Please enter an integer number of bytes.");
         }
      }
   }

   //***********************************************************
   //
   // Method: handleReleaseCommand
   //
   // Description: Validates and processes an RL command. It
   // extracts the process id and calls the MemoryManager to
   // release all memory allocated to that process.
   //
   // Parameters: String[] tokens - the tokens that make up the
   // command line
   //
   // Returns: N/A
   //
   //***********************************************************
   private void handleReleaseCommand(String[] tokens)
   {
      if (tokens.length != 2)
      {
         System.out.println("Invalid RL command. Usage: RL <ProcessId>");
      }
      else
      {
         String processId = tokens[1];
         memoryManager.releaseMemory(processId);
      }
   }
}
