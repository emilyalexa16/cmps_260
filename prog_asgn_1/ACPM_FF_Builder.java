import java.util.LinkedList;

/* This class is for the purpose of completing the construction of an 
** ACPM (Aho/Corasick pattern matching machine) by computing each node's
** failure node.  
*/

public class ACPM_FF_Builder {

   /* Given the root node of an ACPM (Aho/Corasick pattern matching machine)
   ** whose lexicon has been established, computes the failure node of each
   ** node in the machine (and assigns it to that node's 'failureNode'
   ** instance variable).
   ** Let x be a node.  Then the value returned from the call x.wordOf()
   ** is the string formed by concatenating the labels on the transitions
   ** leading to node x from the root node.  The failure node of x must be
   ** that node y such that y.wordOf() is the longest proper suffix of 
   ** x.wordOf() that is a prefix of some keyword in the machine's lexicon.
   */
   public static void computeFailures(ACPM_Node root) {

      // For convenience, the root is made to be its own failure node.
      root.failureNode = root;

      // Create a NodeQueue object.  For every child z of the root, place
      // it onto the queue and set its failure node to be the root. 
      NodeQueue nodeQueue = new NodeQueue();
      
      for (ACPM_Node child : root.children) {
         nodeQueue.enqueue(child);
         child.failureNode = root;
      }
      
   

      // Develop a loop that iterates as long as the queue is not empty.
      // Each iteration should remove a node x from the queue and, for
      // each child z of x
      // (1) compute the failure node of z (and update z.failureNode 
      //     accordingly), and 
      // (2) place z into the queue.
      while (!nodeQueue.isEmpty()) {
      
         ACPM_Node currentNode = nodeQueue.dequeue();
         
         for (ACPM_Node child : currentNode.children) {
   
            ACPM_Node currentFailureNode = currentNode.failureNodeOf();
            
            boolean failureNodeIsRoot = false;
            boolean failureFound = false;
            
            while (!failureNodeIsRoot && !failureFound) {
            
               if (currentFailureNode == root && root.childOf(child.label) == null) {
                  failureNodeIsRoot = true;
               } else if (currentFailureNode.childOf(child.label) != null) {
                  failureFound = true;
               } else {
                  currentFailureNode = currentFailureNode.failureNodeOf();
               }
               
            }
            
            if (failureNodeIsRoot) { child.failureNode = root; }
            else if (failureFound) { child.failureNode = currentFailureNode.childOf(child.label); }
            nodeQueue.enqueue(child);
         }
      }
    
      // For debugging purposes:
      // printFailures(root);
   }


   // Nested class
   // ------------

   /* An instance of this class represents a queue containing
   ** elements of type ACPM_Node.
   */
   private static class NodeQueue {

      // instance variable
      // -----------------
      private LinkedList<ACPM_Node> list;

      // constructor
      // -----------

      /* Establishes this queue as being empty.
      */
      public NodeQueue() { list = new LinkedList<ACPM_Node>(); }

      // observers
      // ---------

      public boolean isEmpty() { return list.size() == 0; }

      public ACPM_Node frontOf() { return list.getFirst(); }

      // mutators
      // --------

      public void enqueue(ACPM_Node node) { list.addLast(node); }

      public ACPM_Node dequeue() { return list.removeFirst(); }

   }  // end of private NodeQueue class



   // methods to aid debugging
   // ------------------------

   /* For each node z in the subtree rooted at the given node, prints
   ** z.wordOf() and z.failureNode.wordOf(), separated by a space.
   */
   private static void printFailures(ACPM_Node node) {
      System.out.println(quoted(node.wordOf()) + ' ' +
                         quoted(node.failureNode.wordOf()));
      for (ACPM_Node child : node.children) {
         printFailures(child);
      }
   }

   /* Returns the given string, augmented with double quotes on
   ** both sides.
   */
   private static String quoted(String s) {
      char DOUBLE_QUOTE = '\"';
      return DOUBLE_QUOTE + s + DOUBLE_QUOTE;
   }

}