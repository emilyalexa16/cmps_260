import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/* Application program for the purpose of testing the ACPM class and,
** indirectly, the classes that support it.
** One or two file names are expected as command line arguments.
** The first one is taken to be the name of a file that contains the
** intended lexicon of an ACPM (Aho/Corasick Pattern Matching machine),
** one keyword per line.  An ACPM with that lexicon is created.
** If a second command line argument is provided, it is taken to be
** the name of a file in which all occurrences of keywords in the lexicon
** are to be identified.
*/

public class ACPM_Tester {


   public static void main(String[] args) throws FileNotFoundException {
      Scanner input = new Scanner(new File(args[0]));
      PatternMatcher acpm = new ACPM(input);

      if (args.length > 1) {
         input = new Scanner(new File(args[1]));
         findMatches(acpm, input);
      }
      
      System.out.println("Goodbye.");
   }

   /* Each line of input provided by the given Scanner is processed for
   ** the purpose of finding every occurrence of every member of the
   ** given PatternMatcher's language/lexicon.  Lines of input are
   ** numbered starting at one, as are positions within each line.
   ** For each (lineNum, position) pair at which one or more keywords
   ** end, a line of output is produced that identifies all the keywords
   ** that end there. 
   */
   public static void findMatches(PatternMatcher acpm, Scanner text) {
      int lineNumber = 0;
      while (text.hasNextLine()) {
         acpm.reset();
         String line = text.nextLine().toLowerCase();
         lineNumber++; 
         for (int i = 0; i != line.length(); i++) {
            acpm.processSymbol(line.charAt(i));
            List<String> matchedWords = acpm.matchList();

            if (matchedWords != null  &&  matchedWords.size() != 0) {
               System.out.printf("(%d,%d):", lineNumber, i+1);
               for (String s : matchedWords) {
                  System.out.printf("\"%s\" ", s);
               }
               System.out.println();
            }
         }
      }
   }

}