package edu.brown.cs.sjl2.autocorrect.rankers;

import java.util.Comparator;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * WordRanker is a way to compare two strings by their value as a word in a
 * Dictionary of by where they are in an input.
 *
 * @author sjl2
 *
 */
public interface WordRanker extends Comparator<String> {


  /**
   * Creates a new instance of the the WordRanker with the same fields as the
   * original object.
   * @return Returns a copy of the original WordRanker
   */
  WordRanker copyOf();
  /**
   * Change the DictionaryTrie that the Ranker uses to rank suggestions.
   * @param newTrie The new trie to use for suggestions.
   */
  void updateDictionary(DictionaryTrie newTrie);

  /**
   * Change the input that the ranker should use to rank the words.
   * @param newInput A String array of the words of the input.
   */
  void updateInput(String[] newInput);

  /**
   * Obtains the sign of a number.
   * @param number A number that needs to be turned to a sign.
   * @return Returns 1 if the number is positive, -1 if the number is negative
   * and 0 if the number is zero.
   */
  default int sign(int number) {
    if (number > 0) {
      return 1;
    } else if (number < 0) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * Obtains the sign of a number.
   * @param number A double number that needs to be turned to a sign.
   * @return Returns 1 if the number is positive, -1 if the number is negative
   * and 0 if the number is zero.
   */
  default int sign(double number) {
    if (number > 0) {
      return 1;
    } else if (number < 0) {
      return -1;
    } else {
      return 0;
    }
  }
}
