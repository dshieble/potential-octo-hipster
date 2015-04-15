package edu.brown.cs.dshieble.maps;

import java.io.Serializable;
import java.util.Comparator;


/**
 * A Comparator that compares the weights of 2 actors.
 *
 * */
public class NodeComparator implements Comparator<Node>, Serializable {

  /**
   *generated id.
   */
  private static final long serialVersionUID = 1L;


  /**
   * Constructor doesn't do anything.
   *
   */
  public NodeComparator() {

  }


  /**
   * compares 2 node' weights.
   * @param o1 - node 1
   * @param o2 - node 2
   * @return 1,-1, or 1 depending on which actor has a smaller
   * GUESSED WEIGHT (weight + heuristic)
   */
  @Override
  public final int compare(final Node o1, final Node o2) {
    double w1 = o1.getWeight() + o1.getHeuristic();
    double w2 = o2.getWeight() + o2.getHeuristic();
    if (w1 > w2) {
      return 1;
    } else if (w1 < w2) {
      return -1;
    } else {
      return 0;
    }
  }
}
