import java.util.List;

/* An instance of an implementing class performs pattern matching.
*/
public interface PatternMatcher {

   // observer
   // --------

   /* Returns a list containing all the current matches.
   ** Let z be the string formed by concatenating the characters
   ** passed to the processSymbol() method since the last time reset()
   ** was called (or, if reset() has never been called, since creation). 
   ** Then a "match" is any suffix of z that is in the language of this
   ** pattern matcher.
   */
   List<String> matchList();

   // mutators
   // --------

   /* Resets this pattern matcher to begin from scratch
   ** (i.e., as though no symbols had been processed yet).
   */
   void reset();

   /* Processes the given symbol (after which a call to matchList()
   ** will provide a list of the current matches).
   */
   void processSymbol(char ch);

}