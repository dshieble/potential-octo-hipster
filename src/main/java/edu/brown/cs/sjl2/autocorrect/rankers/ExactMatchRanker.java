package edu.brown.cs.sjl2.autocorrect.rankers;

import java.io.Serializable;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * Exact Match Ranker ranks words based on if they are exact matches to the
 * lastWord of the input or not. If they are an exact match, they are determined
 * equivalent. If they are not, the one that is not an exact match will be
 * ordered after the exact match.
 *
 * @author sjl2
 *
 */
public class ExactMatchRanker implements WordRanker, Serializable {

  private static final long serialVersionUID = 1L;
  private String[] input;

  /**
   *  The constructor for a DefaultRanker. This Ranker is designed to rank words
   *  by whether they are an exact match to the input or not.
   */
  public ExactMatchRanker() {
    this.input = new String[0];
  }

  /**
   * Compares o1 and o2 returning them ordered by whether they are exact matches
   * to the last word of the input. If they are both equal to the word, or both
   * not equal, then zero is returned as their order does not matter. compare
   * converts the input string to lower case before running.
   * @param o1 First string
   * @param o2 Second string
   * @return Returns the int -1 if o1 is less than o2, 1 if greater, and 0 if
   * equal.
   */
  @Override
  public int compare(String o1, String o2) {
    o1 = o1.toLowerCase();
    o2 = o2.toLowerCase();
    int l = input.length;
    if (l >= 1) {
      String lastWord = input[l - 1].toLowerCase();
      if (lastWord.equals(o1) && lastWord.equals(o2)) {
        return 0;
      } else if (lastWord.equals(o1)) {
        return -1;
      } else if (lastWord.equals(o2)) {
        return 1;
      }
    }
    return 0;
  }

  @Override
  public void updateDictionary(DictionaryTrie trie) {
    // Not Implemented
  }

  @Override
  public void updateInput(String[] newInput) {
    this.input = newInput.clone();
  }

  @Override
  public WordRanker copyOf() {
    ExactMatchRanker copy = new ExactMatchRanker();
    copy.updateInput(input.clone());
    return copy;
  }

}
