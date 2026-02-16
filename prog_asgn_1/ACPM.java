import java.util.List;
import java.util.Iterator;

/* An instance of this class represents an Aho/Corasick 
** pattern matching machine.
*/
public class ACPM implements PatternMatcher {

   // instance variables
   // ------------------

   private ACPM_Node root;
   private ACPM_Node currentNode;


   // constructor
   // -----------

   /* Establishes this ACPM as one whose lexicon includes precisely the
   ** strings provided by the given iterator.
   */
   public ACPM(Iterator<String> lexiconIter) {
      root = new ACPM_Node();
      ACPM_Lexicon_Builder lexiconBuilder = new ACPM_Lexicon_Builder(root);
      while (lexiconIter.hasNext()) {
         String keyWord = lexiconIter.next();
         lexiconBuilder.addKeyWord(keyWord);
      }
      //lexiconBuilder.printTreeStructure();
      //lexiconBuilder.printLexicon();

      // Having established this ACPM's goTo() function,
      // now compute its failure function.
      ACPM_FF_Builder.computeFailures(root);

      reset();
   }

   // observer
   // --------

   /* Returns a list containing all the currently-matched keywords,
   ** or null if there are none.
   */
   public List<String> matchList() {
      return currentNode.keyWordsOf();
   }
   
   // mutators
   // --------

   /* Resets this ACPM to begin matching from scratch
   ** (i.e., as though no symbols had been processed yet).
   */
   public void reset() { currentNode = root; }

   /* Processes the given symbol (after which matchList() can be 
   ** called to obtain a list of matched keywords).
   */
   public void processSymbol(char ch) {
      while (currentNode.goTo(ch) == null) {
         currentNode = currentNode.failureNodeOf();
      }
      currentNode = currentNode.goTo(ch);
   }

}
