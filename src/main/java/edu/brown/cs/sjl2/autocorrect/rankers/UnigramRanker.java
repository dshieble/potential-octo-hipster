package edu.brown.cs.sjl2.autocorrect.rankers;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * UnigramRanker ranks words by their frequency within a Dictionary.
 * The higher the frequency, the earlier it will be appear in a sorted list.
 *
 * @author sjl2
 *
 */
public class UnigramRanker implements WordRanker {

  private DictionaryTrie trie;

  /**
   * Constructor for a UnigramRanker.
   * @param trie The trie to find the frequencies of the suggestions.
   */
  public UnigramRanker(DictionaryTrie trie) {
    this.trie = trie;
  }

  @Override
  public int compare(String o1, String o2) {
    // Split in case of multiple word suggestion
    String[] word1 = o1.trim().split("[\\s]+");
    String[] word2 = o2.trim().split("[\\s]+");

    int freq1 = trie.getFrequency(word1[0]);
    int freq2 = trie.getFrequency(word2[0]);
    return sign(freq2 - freq1); // Higher Frequencies First
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  @Override
  public void updateInput(String[] newInput) {

  }

  @Override
  public WordRanker copyOf() {
    return new UnigramRanker(trie);
  }

}
