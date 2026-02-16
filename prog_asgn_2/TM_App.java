import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/* Java application that uses an instance of the TuringMachine class
** to simulate a computation of a Turing Machine.  
*/

public class TM_App {

   /* Three command-line arguments are expected:
   ** args[0]: name of file containing list of TM transitions
   **   Each transition should be on a line by itself.
   **   Example: "3 a 1 b R" describes the transition from state 3 to state 1
   **      on an 'a', which is replaced by 'b' and the read-write head moves
   **      one place to the Right.  (Use 'L' for left.)
   ** args[1]: ID of the initial state (an integer)
   ** args[2]: input string to be processed
   */
   public static void main(String[] args) throws FileNotFoundException {

      // Create a Turing Machine whose transitions are those found in
      // the file whose name is the first command line argument.
      String fileName = args[0];
      Scanner in = new Scanner(new File(fileName));
      TuringMachine tm = new TuringMachine(in);

      // Establish the initial state and the input string, which are
      // specified by the 2nd and 3rd command line arguments.
      int initState = Integer.parseInt(args[1]);
      String inputString = args[2];

      // Establish the Turing Machine's initial instantaneous description
      // and display it.
      tm.start(initState, inputString);
      System.out.println(tm.imageOfID());

      // Run the Turing Machine until it halts, displaying the
      // computation's instantaneous description after every move.
      while (!tm.hasHalted()) {
         tm.move();
         System.out.println(tm.imageOfID());
      }
   }
}
