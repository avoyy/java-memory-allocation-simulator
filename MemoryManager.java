/*
 * Memory Allocation Simulator
 *
 * Author: Avoy Tejada
 *
 * Description:
 * Core memory management logic handling allocation strategies,
 * memory release, and compaction.
 */

import java.util.ArrayList;

public class MemoryManager
{
   private long totalMemorySize;
   private ArrayList<MemoryBlock> memoryBlocks;

   //***********************************************************
   //
   // Method: MemoryManager (Constructor)
   //
   // Description: Constructs a MemoryManager for a contiguous
   // region of memory with the specified total size in bytes.
   // Initially, the entire region is one free block.
   //
   // Parameters: long totalBytes - the total number of bytes in
   // the managed memory region
   //
   // Returns: N/A
   //
   //***********************************************************
   public MemoryManager(long totalBytes)
   {
      totalMemorySize = totalBytes;
      memoryBlocks = new ArrayList<MemoryBlock>();

      long startAddress;
      long endAddress;

      startAddress = 0L;
      endAddress = totalMemorySize - 1L;

      MemoryBlock initialBlock = new MemoryBlock(startAddress, endAddress, true, "");
      memoryBlocks.add(initialBlock);
   }
   //***********************************************************
   //
   // Method: requestMemory
   //
   // Description: Attempts to allocate a contiguous block of
   // memory for the given process using the specified strategy.
   // This version allows multiple allocations for the same
   // process, matching the assignment examples.
   //
   // Parameters:
   //   String processId - the id of the requesting process
   //   long requestedBytes - the number of bytes requested
   //   char strategy - F, B, or W
   //
   // Returns: N/A
   //
   //***********************************************************
   public void requestMemory(String processId, long requestedBytes, char strategy)
   {
      int holeIndex;
      holeIndex = findHoleIndex(requestedBytes, strategy);

      if (holeIndex == -1)
      {
         System.out.println("Error: Not enough memory for process " + processId + ".");
      }
      else
      {
         allocateFromHole(processId, requestedBytes, holeIndex);
      }
   }

   //***********************************************************
   //
   // Method: releaseMemory
   //
   // Description: Releases all memory blocks that have been
   // allocated to the given process. If the process does not
   // exist in the allocation table, an error message is
   // displayed. Adjacent free blocks are merged after the
   // release.
   //
   // Parameters: String processId - the process whose memory
   // should be released
   //
   // Returns: N/A
   //
   //***********************************************************
   public void releaseMemory(String processId)
   {
      boolean found;
      found = false;

      int index;
      int numberOfBlocks;
      numberOfBlocks = memoryBlocks.size();
      index = 0;

      while (index < numberOfBlocks)
      {
         MemoryBlock block = memoryBlocks.get(index);

         if (!block.isFree() && block.getProcessId().equals(processId))
         {
            block.setFree(true);
            block.setProcessId("");
            found = true;
         }

         index = index + 1;
      }

      if (!found)
      {
         System.out.println("Error: Process " + processId + " not found.");
      }
      else
      {
         mergeFreeBlocks();
      }
   }

   //***********************************************************
   //
   // Method: compactMemory
   //
   // Description: Compacts memory by moving all allocated
   // blocks towards the beginning of memory, preserving their
   // relative order and creating a single free block at the end
   // that represents all unused memory.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   public void compactMemory()
   {
      ArrayList<MemoryBlock> compactedList;
      compactedList = new ArrayList<MemoryBlock>();

      long nextFreeAddress;
      nextFreeAddress = 0L;

      int index;
      int numberOfBlocks;
      numberOfBlocks = memoryBlocks.size();
      index = 0;

      long allocatedSize;
      long newStartAddress;
      long newEndAddress;

      while (index < numberOfBlocks)
      {
         MemoryBlock block = memoryBlocks.get(index);

         if (!block.isFree())
         {
            allocatedSize = block.getSize();
            newStartAddress = nextFreeAddress;
            newEndAddress = newStartAddress + allocatedSize - 1L;

            MemoryBlock newBlock = new MemoryBlock(newStartAddress,
                                                   newEndAddress,
                                                   false,
                                                   block.getProcessId());
            compactedList.add(newBlock);

            nextFreeAddress = newEndAddress + 1L;
         }

         index = index + 1;
      }

      long freeSize;
      freeSize = totalMemorySize - nextFreeAddress;

      if (freeSize > 0L)
      {
         long freeStartAddress;
         long freeEndAddress;

         freeStartAddress = nextFreeAddress;
         freeEndAddress = totalMemorySize - 1L;

         MemoryBlock freeBlock = new MemoryBlock(freeStartAddress,
                                                 freeEndAddress,
                                                 true,
                                                 "");
         compactedList.add(freeBlock);
      }

      memoryBlocks = compactedList;
   }

   //***********************************************************
   //
   // Method: printMemoryStatus
   //
   // Description: Prints the regions of memory that are
   // allocated and those that are unused. Each region is
   // reported with its starting and ending addresses.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   public void printMemoryStatus()
   {
      int index;
      int numberOfBlocks;
      numberOfBlocks = memoryBlocks.size();
      index = 0;

      while (index < numberOfBlocks)
      {
         MemoryBlock block = memoryBlocks.get(index);
         long startAddress = block.getStartAddress();
         long endAddress = block.getEndAddress();

         if (block.isFree())
         {
            System.out.println("Addresses [" + startAddress + ":" + endAddress + "] Unused");
         }
         else
         {
            System.out.println("Addresses [" + startAddress + ":" + endAddress + "] Process " + block.getProcessId());
         }

         index = index + 1;
      }
   }

   //***********************************************************
   //
   // Method: findHoleIndex
   //
   // Description: Finds the index of a free block (hole) that
   // can satisfy a request of the given size using the specified
   // strategy: F (first fit), B (best fit), or W (worst fit).
   // If no suitable hole exists, -1 is returned.
   //
   // Parameters:
   //   long requestedBytes - the requested block size
   //   char strategy - F, B, or W
   //
   // Returns: int - the index of the selected hole, or -1 if
   // none was found
   //
   //***********************************************************
   private int findHoleIndex(long requestedBytes, char strategy)
   {
      int selectedIndex;
      selectedIndex = -1;

      long bestSize;
      long worstSize;

      bestSize = Long.MAX_VALUE;
      worstSize = 0L;

      int index;
      int numberOfBlocks;
      numberOfBlocks = memoryBlocks.size();
      index = 0;

      while (index < numberOfBlocks)
      {
         MemoryBlock block = memoryBlocks.get(index);

         if (block.isFree())
         {
            long blockSize;
            blockSize = block.getSize();

            if (blockSize >= requestedBytes)
            {
               if (strategy == 'F')
               {
                  if (selectedIndex == -1)
                  {
                     selectedIndex = index;
                  }
               }
               else if (strategy == 'B')
               {
                  if (blockSize < bestSize)
                  {
                     bestSize = blockSize;
                     selectedIndex = index;
                  }
               }
               else if (strategy == 'W')
               {
                  if (blockSize > worstSize)
                  {
                     worstSize = blockSize;
                     selectedIndex = index;
                  }
               }
            }
         }

         index = index + 1;
      }

      return selectedIndex;
   }

   //***********************************************************
   //
   // Method: allocateFromHole
   //
   // Description: Performs the actual allocation once an
   // appropriate free block (hole) index has been selected. It
   // either converts the hole entirely into an allocated block
   // or splits it into an allocated block followed by a smaller
   // free block.
   //
   // Parameters:
   //   String processId - the id of the requesting process
   //   long requestedBytes - the requested block size
   //   int holeIndex - the index of the selected free block
   //
   // Returns: N/A
   //
   //***********************************************************
   private void allocateFromHole(String processId, long requestedBytes, int holeIndex)
   {
      MemoryBlock hole = memoryBlocks.get(holeIndex);

      long holeStart;
      long holeEnd;
      long newAllocStart;
      long newAllocEnd;
      long remainingStart;

      holeStart = hole.getStartAddress();
      holeEnd = hole.getEndAddress();

      newAllocStart = holeStart;
      newAllocEnd = newAllocStart + requestedBytes - 1L;

      long holeSize;
      holeSize = hole.getSize();

      if (holeSize == requestedBytes)
      {
         hole.setFree(false);
         hole.setProcessId(processId);
      }
      else
      {
         remainingStart = newAllocEnd + 1L;
         long remainingEnd;
         remainingEnd = holeEnd;

         MemoryBlock allocatedBlock = new MemoryBlock(newAllocStart,
                                                      newAllocEnd,
                                                      false,
                                                      processId);
         MemoryBlock remainingHole = new MemoryBlock(remainingStart,
                                                     remainingEnd,
                                                     true,
                                                     "");

         memoryBlocks.set(holeIndex, allocatedBlock);
         memoryBlocks.add(holeIndex + 1, remainingHole);
      }
   }

   //***********************************************************
   //
   // Method: mergeFreeBlocks
   //
   // Description: Scans the list of memory blocks and merges
   // any adjacent free blocks into single larger free blocks to
   // keep the list as compact as possible.
   //
   // Parameters: None
   //
   // Returns: N/A
   //
   //***********************************************************
   private void mergeFreeBlocks()
   {
      int index;
      int lastIndex;

      lastIndex = memoryBlocks.size() - 1;
      index = 0;

      while (index < lastIndex)
      {
         MemoryBlock currentBlock = memoryBlocks.get(index);
         MemoryBlock nextBlock = memoryBlocks.get(index + 1);

         if (currentBlock.isFree() && nextBlock.isFree())
         {
            long newEndAddress;
            newEndAddress = nextBlock.getEndAddress();
            currentBlock.setEndAddress(newEndAddress);
            memoryBlocks.remove(index + 1);
            lastIndex = memoryBlocks.size() - 1;
         }
         else
         {
            index = index + 1;
         }
      }
   }
}
