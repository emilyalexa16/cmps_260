import java.util.Iterator;

/* An instance of this class is for the purpose of building an ACPM
** (i.e., Aho/Corasick pattern matching machine) so that its lexicon
** (i.e., the set of keywords that it will be able to match) includes
** all the strings provided to either of the two mutator methods.
*/
public class ACPM_Lexicon_Builder {

   private ACPM_Node root; // root of the "resident" ACPM, whose lexicon
                           // is to be built/augmented here

   // constructor
   // -----------

   /* Establishes that the given ACPM_Node is the root node of the 
   ** resident Aho/Corasick pattern matching machine whose lexicon
   ** this object is intended to augment.
   */
   public ACPM_Lexicon_Builder(ACPM_Node root) {
      this.root = root;
   }

   // mutators
   // --------

   /* Adds the given string to the lexicon of the resident ACPM.
   */
   public void addKeyWord(String word) {
      System.out.printf("Adding key word: %s ...\n", word);
      
      ACPM_Node currentNode = root;
      char[] charsInWord = word.toCharArray();
      for (char character : charsInWord) {
         if (currentNode.childOf(character) == null) {
            ACPM_Node newCharNode = currentNode.addChild(character);
            currentNode = newCharNode;
         } else {
            currentNode = currentNode.childOf(character);
         }
      }
      
      currentNode.setToFinal();

      System.out.printf("Finished adding key word: %s\n", word);
   }

   /* Adds all the strings provided by the given iterator to the
   ** lexicon of the resident ACPM.
   */
   public void addKeyWords(Iterator<String> iter) {
      while (iter.hasNext()) {
         addKeyWord(iter.next());
      }
   }

   // methods to aid debugging
   // ------------------------

   /* Produces output that shows the structure of the tree that 
   ** represents the lexicon of the resident ACPM.  Each line of
   ** output describes one node by indicating the label on its
   ** incoming edge/transition and whether or not it is a final node.
   ** The description of each node is indented in accord to its depth
   ** in the tree (i.e., its distance from the root).
   */
   public void printTreeStructure() {
      printTreeStructureAux(root, 0);
   }

   /* Auxiliary to method above, it produces output for those nodes
   ** that are descendants of the given node.  The second argument
   ** indicates the depth of the given node.
   */
   private void printTreeStructureAux(ACPM_Node node, int depth) {
      printSpaces(2*depth);
      if (node == root) { System.out.print("Root"); }
      else {
         System.out.printf("label:%c", node.label);
         // Alternative to line above:
         //System.out.print(node.wordOf());
         if (node.isFinal()) { System.out.print(" (Final)"); }
      }
      System.out.println();
      // Recursively produce output for the subtrees rooted at the
      // child nodes.
      for (ACPM_Node child : node.children) {
         printTreeStructureAux(child, depth+1);
      }
   }

   /* Prints the specified number of spaces.
   */
   private void printSpaces(int k) {
      for (int i = 0; i != k; i++) { System.out.print(' '); }
   }

   /* Prints, one per line, all the keywords in the lexicon of the
   ** resident ACPM.
   */
   public void printLexicon() {
      printLexiconAux(root);
   }

   /* Auxiliary to method above, it prints the keywords in the lexicon 
   ** that are represented by final nodes that are descendants of the
   ** given node.
   */
   private void printLexiconAux(ACPM_Node node) {
      for (ACPM_Node child : node.children) {
         if (child.isFinal()) { System.out.println(child.wordOf()); }
         printLexiconAux(child);
      }
   }

}