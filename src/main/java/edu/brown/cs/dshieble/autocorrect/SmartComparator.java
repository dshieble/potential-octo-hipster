package edu.brown.cs.dshieble.autocorrect;

import java.util.Comparator;

import com.google.common.collect.HashMultiset;

/**A Comparator that compares 2 words.
 * */
public class SmartComparator implements Comparator<String> {

  /**
   * stores unigram.
   */
  private HashMultiset<String> unigramHash;
  /**
   * stores bigram.
   */
  private HashMultiset<String> bigramHash;
  /**
   * stores unigram.
   */
  private HashMultiset<String> unigramHash1;
  /**
   * stores unigram.
   */
  private HashMultiset<String> bigramHash1;

  /**Instantiates the Comparator.
   * @param u1 the unigram HashMultiSet for typed text
   * @param b1 the bigram HashMultiSet or typed text
   * @param u the unigram HashMultiSet
   * @param b the bigram HashMultiSet
   * * */
  public SmartComparator(
      final HashMultiset<String> u1,
      final HashMultiset<String> b1,
      final HashMultiset<String> u,
      final HashMultiset<String> b) {
    this.unigramHash = unigramHash;
    this.bigramHash = bigramHash;
    this.unigramHash1 = unigramHash1;
    this.bigramHash1 = bigramHash1;
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
    //Process based on typed text
    if (bigramHash1.count(o1) > bigramHash1.count(o2)) {
      return -1;
    } else if (bigramHash1.count(o1) < bigramHash1.count(o2)) {
      return 1;
    } else if (unigramHash1.count(o1) > unigramHash1.count(o2)) {
      return -1;
    } else if (unigramHash1.count(o1) < unigramHash1.count(o2)) {
      return 1;
    }
    //Process based on text corpus
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

