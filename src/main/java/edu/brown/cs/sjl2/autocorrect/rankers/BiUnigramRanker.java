package edu.brown.cs.sjl2.autocorrect.rankers;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * BiUnigram combines the Bigram and Unigram Rankers into one. Instead of
 * defaulting the bigram, this ranker factors in both values by finding the max
 * between the two. The original plan was to normalize them by dividing by the
 * the number of words in the texts and the number of preceding words for
 * unigram and bigram respectively. These quotients prove to be very similar
 * (only differing by the number of files that were used to build the trie).
 * Thus, it is a valid approximation to assume the numbers are normalized. Using
 * the normalized numbers, I took the average of both the unigram and bigram.
 * This way, suggestions with good frequencies in both categories will stand a
 * chance in the ranking.
 *
 * @author sjl2
 *
 */
public class BiUnigramRanker implements WordRanker {

  private DictionaryTrie trie;
  private String[] input;

  /**
   * Constructor for a BiUnigramRanker.
   * @param trie The trie to base the Bigram Frequencies and Unigram frequencies
   * off of.
   */
  public BiUnigramRanker(DictionaryTrie trie) {
    this.trie = trie;
    this.input = new String[0];
  }

  @Override
  public int compare(String o1, String o2) {
    int l = input.length;
    // Split in case of multiple word suggestion
    String[] word1 = o1.trim().toLowerCase().split("[\\s]+");
    String[] word2 = o2.trim().toLowerCase().split("[\\s]+");

    if (l >= 2) {
      String prev = input[l - 2].toLowerCase();
      boolean noPrev = prev.isEmpty();
      int biFreq1 = noPrev ? trie.getBigramFrequency(word1[0], prev) : 0;
      int biFreq2 = noPrev ? trie.getBigramFrequency(word2[0], prev) : 0;

      int freq1 = trie.getFrequency(word1[0]);
      int freq2 = trie.getFrequency(word2[0]);

      int avg1 = (biFreq1 + freq1) / 2;
      int avg2 = (biFreq2 + freq2) / 2;

      return sign(avg2 - avg1);
    }


    return 0;
  }

  @Override
  public WordRanker copyOf() {
    BigramRanker copy = new BigramRanker(trie);
    copy.updateInput(this.input.clone());
    return copy;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  @Override
  public void updateInput(String[] newInput) {
    this.input = newInput.clone();
  }

}
