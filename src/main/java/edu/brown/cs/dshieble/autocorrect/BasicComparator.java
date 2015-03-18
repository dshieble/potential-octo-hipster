package edu.brown.cs.dshieble.autocorrect;

import java.util.Comparator;

import com.google.common.collect.HashMultiset;

/**A Comparator that compares 2 words.
 * */
public class BasicComparator implements Comparator<String> {

  /**
   * stores unigram freq.
   */
  private HashMultiset<String> unigramHash;
  /**
   * stores bigram freq.
   */
  private HashMultiset<String> bigramHash;

  /**Instantiates the Comparator.
   * @param u the unigram HashMultiSet
   * @param b the bigram HashMultiSet
   * */
  public BasicComparator(final HashMultiset<String> u,
      final HashMultiset<String> b) {
    this.unigramHash = u;
    this.bigramHash = b;
  }

  /**
   * This method assumes that both words are in the hashsets.
   * Otherwise, an error will be thrown
   * @param oa First word
   * @param ob Second word
   * @return Determination of closer point
   */
  @Override
  public final int compare(final String oa, final String ob) {
    String o1 = oa.split(" ")[0];
    String o2 = ob.split(" ")[0];
    if (bigramHash.count(o1) > bigramHash.count(o2)) {
      return -1;
    } else if (bigramHash.count(o1) < bigramHash.count(o2)) {
      return 1;
    } else if (unigramHash.count(o1) > unigramHash.count(o2)) {
      return -1;
    } else if (unigramHash.count(o1) < unigramHash.count(o2)) {
      return 1;
    } else if (o1.compareTo(o2) < 0) {
      return -1;
    } else if (o1.compareTo(o2) > 0) {
      return 1;
    }
    return 0;
  }

}

