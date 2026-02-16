import java.util.LinkedList;
import java.util.Scanner;

/* An instance of this class represents a deterministic Turing Machine.
** States are identified by integers.  The tape alphabet corresponds to 
** Java's char data type.  The dash/hyphen character '-' acts as the
** special "BLANK" symbol that is assumed initially to occupy all 
** squares of the tape except for those containing symbols of the
** input string.
** Author: R. McCloskey
** Date: May 2024
*/

public class TuringMachine {

   // class constants
   // ---------------

   public static final char BLANK = '-';
   public static final char RIGHT_DIRECTION = 'R';
   public static final char LEFT_DIRECTION = 'L';

   // instance variables
   // ------------------

   private LinkedList<Transition> transitionList;
   private LinkedList<Character> leftTape; 
   private LinkedList<Character> rightTape; 
   private int currentState;
   private Transition nextTransition;  

   // 'leftTape' holds the contents of that portion of the TM's tape that
   // is to the left of the RW-head's position, while 'rightTape' holds the 
   // contents of the rest of the tape, beginning with the square at which
   // the RW-head is positioned.
   // 'nextTransition' refers to the transition that would be applied
   // next, if a move were to be made.  (Its value is 'null' if there is
   // no such transition.)


   // constructor
   // -----------

   /* Establishes this TM to be that described by the data readable
   ** by the given Scanner.
   */
   public TuringMachine(Scanner input) {
      transitionList = new LinkedList<Transition>();
      while (input.hasNextLine()) {
         String line = input.nextLine().trim();
         if (line.length() == 0  ||  line.charAt(0) == '#') {
            // a blank line or one beginning with # is taken to be a comment
         }
         else {
            Scanner lineScanner = new Scanner(line);
            int state = lineScanner.nextInt();
            char ch = lineScanner.next().charAt(0);
            int newState = lineScanner.nextInt();
            char newChar = lineScanner.next().charAt(0);
            char dir = lineScanner.next().charAt(0);
            Transition t = new Transition(state, ch, newState, newChar, dir);
            System.out.print("Transition " + t);
            if (addTransition(t)) {
               System.out.println(" successfully added.");
            }
            else {
               System.out.println(" rejected.");
            }
         }
      }
   }

   /* Establishes this TM as one having no transitions (with the
   ** expectation that calls to addTransition() will supply some of them).
   */
   public TuringMachine() {
      transitionList = new LinkedList<Transition>();
   }

   // observers
   // ---------

   /* Returns a string depicting this TM's instantaneous description.
   ** It shows the contents of the portion of the tape to the left of
   ** the RW-Head's position, followed by the ID of the current state,
   ** followed by the contents of the portion of the tape starting
   ** at the square at which the RW-Head is positioned
   */
   public String imageOfID() {
      return leftTape.toString() + ' ' + currentState + ' ' + 
             rightTape.toString();
   }

   /* If it exists, returns a string describing this TM's transition on 
   ** (state,ch); if no such transition exists, null is returned.
   */
   public String transitionOn(int state, char ch) {
      Transition t = getTransition(state, ch);
      if (t == null) { return null; }
      else { return t.toString(); }
   }

   /* Reports whether or not this TM is in a situation in which
   ** none of its transitions apply.
   */
   public boolean hasHalted() {
      return nextTransition == null;
   }


   // mutators
   // --------

   /* Places this TM into an initial configuration, specified by
   ** the arguments indicating initial state and input string.
   */
   public void start(int initState, String input) {
      currentState = initState;
      leftTape = new LinkedList<Character>();
      rightTape = new LinkedList<Character>();
      if (input.length() == 0) {
         rightTape.addFirst(BLANK);
      }
      else {
         for (int i = 0; i != input.length(); i++) {
            rightTape.addLast(input.charAt(i));
         }
      }
      nextTransition = getTransition(currentState, rightTape.getFirst());
   }

   /* Performs r moves, or until hasHalted() holds
   ** pre: r >= 0
   */
   public void move(int r) {
      int cntr = 0;
      while (!hasHalted() &&  cntr != r) {
         move();
         cntr++;
      }
   }

   /* Performs a single move of this TM.
   ** pre: !hasHalted()
   */
   public void move() {
      if (nextTransition.direction == RIGHT_DIRECTION) {
         if (nextTransition.newChar == BLANK && leftTape.size() == 0) {
            // left tape is empty; leave it that way
         }
         else {
            leftTape.addLast(nextTransition.newChar);
         }
         rightTape.removeFirst();
         if (rightTape.size() == 0) {
            rightTape.addFirst(BLANK);
         }
      }
      else if (nextTransition.direction == LEFT_DIRECTION) {
         rightTape.set(0, nextTransition.newChar);
         if (leftTape.size() == 0) {
            rightTape.addFirst(BLANK);
         }
         else {
            rightTape.addFirst(leftTape.removeLast());
         }
         if (rightTape.size() == 2  &&  rightTape.getLast() == BLANK) {
            rightTape.removeLast();
         }
      }
      currentState = nextTransition.newState;
      nextTransition = getTransition(currentState, rightTape.getFirst());
   }

   /* If this TM fails to have a transition on (oldState, oldChar),
   ** then the transition described by the arguments is added and
   ** the value true is returned.  Otherwise the described transition
   ** is ignored and false is returned.
   */
   public boolean addTransition(int oldState, char oldCh,
                                int newState, char newCh, char dir) {

      Transition t = new Transition(oldState, oldCh, newState, newCh, dir);
      return addTransition(t);
   }

   private boolean addTransition(Transition t) {
      boolean result;
      if (indexOfTransition(t.oldState, t.oldChar) != -1) {
         result = false;
      }
      else {
         transitionList.add(t);
         result = true;
      }
      return result;
   }


   /* If it exists, the transition on (state, ch) is removed from this 
   ** TM's set of transitions and true is returned.  Otherwise its set
   ** of transitions is unchanged and false is returned.
   */
   public boolean removeTransition(int state, char ch) {
      Transition t = new Transition(state, ch, -1, ' ', ' ');
      return transitionList.remove(t);
   }

   
  
   // utility methods
   // ---------------

   /* Returns the position, within the transitionList, of the transition 
   ** on the pair (state, ch), or -1 if no such transition exists.
   */
   private int indexOfTransition(int state, char ch) {
      Transition t = new Transition(state, ch, -1, ' ', ' ');
      return transitionList.indexOf(t);
   }

   /* Returns the transition on (state, ch), if it exists.
   ** Otherwise returns null.
   */
   private Transition getTransition(int state, char ch) {
      int k = indexOfTransition(state, ch);
      if (k == -1) { return null; }
      else { return  transitionList.get(k); }
   }


   // nested class
   // ------------

   private class Transition {

      // instance variables
      // ------------------
      public int oldState;
      public char oldChar;
      public int newState;
      public char newChar;
      public char direction;  // 'L' or 'R'

      // constructor
      // -----------

      public Transition(int oldState, char oldChar,
                        int newState, char newChar,
                        char direction) {
         this.oldState = oldState;
         this.oldChar = oldChar;
         this.newState = newState;
         this.newChar = newChar;
         this.direction = direction;
      }

      // observers
      // ---------

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof Transition) {
            return this.equals((Transition)obj);
            //Transition t = (Transition)obj;
            //return t.oldState == this.oldState &&
            //       t.oldChar == this.oldChar;
         }
         else {
            return false;
         }
      }

      public boolean equals(Transition t) {
         return t.oldState == this.oldState && 
                t.oldChar == this.oldChar;
      }

      @Override
      public String toString() {
         return "(" + oldState + ',' + oldChar + ',' +
                newState + ',' + newChar + ',' + direction + ')';
      }

   }  // end of nested Transition class

}
