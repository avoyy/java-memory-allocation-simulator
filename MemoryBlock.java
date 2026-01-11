/*
 * Memory Allocation Simulator
 *
 * Author: Avoy Tejada
 *
 * Description:
 * Represents a block of memory with start and end addresses,
 * size, and allocation status.
 */

public class MemoryBlock
{
   private long startAddress;
   private long endAddress;
   private boolean free;
   private String processId;

   //***********************************************************
   //
   // Method: MemoryBlock (Constructor)
   //
   // Description: Constructs a MemoryBlock with the specified
   // start and end addresses, free status, and process id.
   //
   // Parameters:
   //   long startAddress - the starting address of the block
   //   long endAddress - the ending address of the block
   //   boolean free - true if the block is free; false if it is
   //                  allocated
   //   String processId - the id of the owning process, or
   //                      an empty string if the block is free
   //
   // Returns: N/A
   //
   //***********************************************************
   public MemoryBlock(long startAddress,
                      long endAddress,
                      boolean free,
                      String processId)
   {
      this.startAddress = startAddress;
      this.endAddress = endAddress;
      this.free = free;
      this.processId = processId;
   }

   // Getters and setters are intentionally simple

   public long getStartAddress()
   {
      return startAddress;
   }

   public void setStartAddress(long startAddress)
   {
      this.startAddress = startAddress;
   }

   public long getEndAddress()
   {
      return endAddress;
   }

   public void setEndAddress(long endAddress)
   {
      this.endAddress = endAddress;
   }

   public boolean isFree()
   {
      return free;
   }

   public void setFree(boolean free)
   {
      this.free = free;
   }

   public String getProcessId()
   {
      return processId;
   }

   public void setProcessId(String processId)
   {
      this.processId = processId;
   }

   //***********************************************************
   //
   // Method: getSize
   //
   // Description: Computes and returns the size of this block
   // in bytes.
   //
   // Parameters: None
   //
   // Returns: long - the number of bytes in the block
   //
   //***********************************************************
   public long getSize()
   {
      return (endAddress - startAddress) + 1L;
   }
}
