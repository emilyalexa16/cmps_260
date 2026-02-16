import java.util.LinkedList;
import java.util.List;

/* An instance of this class represents a node in an ACPM
** (i.e., Aho-Corasick pattern matching machine). 
**
** Authors: R. McCloskey and Emily Alexa Gotiangco
** Date: March 2024
*/

public class ACPM_Node {

   // instance variables
   // ------------------

   protected final char label; 
   protected final ACPM_Node parent;
   protected List<ACPM_Node> children;
   protected ACPM_Node failureNode;
   protected boolean isFinal; 

   // Description:
   //   label: the character labeling the incoming transition to this node
   //   parent: this node's parent
   //   children: list of this node's children
   //   failureNode: failureNode.wordOf() is the longest proper suffix of 
   //                this.wordOf() that is a prefix of some keyword
   //   isFinal: true iff this.wordOf() is a keyword (i.e., in the lexicon)


   // constructor
   // -----------

   /* Establishes this new node's parent and the label on the
   ** incoming edge/transition from that parent.
   */
   public ACPM_Node(ACPM_Node parent, char label) {
      this.parent = parent;
      this.label = label;
      children = new LinkedList<ACPM_Node>(); 
      failureNode = null;
      isFinal = false;
   }

   /* Establishes this new node as being the root node in its machine.
   */
   public ACPM_Node() { this(null, '\0'); }


   // observers
   // ---------

   public String toString() {
      String result;
      if (isRoot()) 
         { result = "Root!"; }
      else {
         result = "label:" + label +
                  " parent:" + parent.toString() + "; ";
      }
      return result;
   }

   /* Reports whether or not this is a final node.
   ** A final node x is one such that x.wordOf() is a keyword
   ** (i.e., a member of the lexicon of the ACPM in which x lies).
   */
   public boolean isFinal() { return isFinal; }

   /* Reports whether or not this node is the root of the ACPM 
   ** in which it lies.
   */
   public boolean isRoot() { return parent == null; }

   /* Returns this node's parent node.
   ** pre: !isRoot()
   */
   public ACPM_Node parentOf() { return parent; }

   /* Returns the label on the edge/transition
   ** leading into this node from its parent.
   ** pre: !isRoot()
   */
   public char label() { return label; }

   /* Returns this node's failure node.
   */
   public ACPM_Node failureNodeOf() { return failureNode; }

   /* Returns the child node, if it exists, to which this node has an
   ** outgoing edge labeled with the given character.  If no such 
   ** child node exists, null is returned, except in the case that 
   ** this node is the root, in which case the root itself is returned.
   */
   public ACPM_Node goTo(char label) {
      for (ACPM_Node child : children) {
         if (child.label() == label) 
            { return child; }
      }
      if (this.isRoot()) { return this; }
      return null;
   }   

   /* Returns that child of this node, if it exists, whose incoming
   ** edge/transition has the indicated label.  If no such child node
   ** exists, null is returned.
   */
   protected ACPM_Node childOf(char label) {
      for (ACPM_Node child : children) {
         if (child.label() == label) 
            { return child; }
      }
      return null;
   }

   /* Returns the string formed by concatenating the labels on the
   ** sequence of edges/transitions leading to this node from the root.
   */
   public String wordOf() {
      StringBuilder sb = new StringBuilder();
      ACPM_Node node = this;
      while (!node.isRoot()) {
         sb.append(node.label());
         node = node.parentOf();
      }
      return sb.reverse().toString();
   }

   /* Returns a list containing all the keywords that are suffixes
   ** of this.wordOf().  If there are none, null is returned.
   */
   public List<String> keyWordsOf() {
      return keyWordsOfAux(this, null); 
   }

   /* Auxiliary to the method above, it inserts into the given
   ** list all the keywords that are suffixes of node.wordOf()
   ** and returns the resulting list.
   ** The logic is to follow the chain of failure nodes from the
   ** given node until reaching the root.  For each node along that
   ** chain, if it is final (and thus its word is a keyword), add 
   ** its word to the list.
   */
   private List<String> keyWordsOfAux(ACPM_Node node, 
                                      List<String> resultSoFar) {
      if (node.isFinal()) { 
         if (resultSoFar == null) {
           resultSoFar = new LinkedList<String>();
         }
         resultSoFar.add(0, node.wordOf());
      }
      if (node.isRoot()) {
         return resultSoFar;
      }
      else {
         return keyWordsOfAux(node.failureNode, resultSoFar);
      }
   }


   // mutators
   // --------

   /* Makes this a final node.
   */
   public void setToFinal() { isFinal = true; }

   /* Creates a new child of this node and establishes the given
   ** character as being the label on the edge/transition into
   ** that child.  Returns (a reference to) the new child node.
   ** pre: this.childOf(label) == null
   */
   public ACPM_Node addChild(char label) {
      ACPM_Node newChild = new ACPM_Node(this, label);
      this.children.add(newChild);
      return newChild;
   }

}
