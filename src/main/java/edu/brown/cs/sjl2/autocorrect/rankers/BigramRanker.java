package edu.brown.cs.sjl2.autocorrect.rankers;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * BigramRanker is a WordRanker that compares words (Strings) by the frequency
 * that they follow the word in front of the word in question. It ranks higher
 * frequencies first.
 *
 * @author sjl2
 *
 */
public class BigramRanker implements WordRanker {

  private DictionaryTrie trie;
  private String[] input;

  /**
   * Constructor for BigramRanker.
   * @param trie The trie from which to obtain the Bigram Frequencies.
   */
  public BigramRanker(DictionaryTrie trie) {
    this.trie = trie;
    this.input = new String[0];
  }

  @Override
  public int compare(String o1, String o2) {
    int l = input.length;

    // Split in case of multiple word suggestion
    String[] word1 = o1.trim().split("[\\s]+");
    String[] word2 = o2.trim().split("[\\s]+");

    if (l >= 2) {
      String prev = input[l - 2].toLowerCase();
      boolean noPrev = !prev.isEmpty();
      int biFreq1 = noPrev ? trie.getBigramFrequency(word1[0], prev) : 0;
      int biFreq2 = noPrev ? trie.getBigramFrequency(word2[0], prev) : 0;
      return sign(biFreq2 - biFreq1); // Higher Frequencies First;
    }

    return 0;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  @Override
  public void updateInput(String[] newInput) {
    this.input = newInput.clone();
  }

  @Override
  public WordRanker copyOf() {
    BigramRanker copy = new BigramRanker(trie);
    copy.updateInput(this.input.clone());
    return copy;
  }

}
