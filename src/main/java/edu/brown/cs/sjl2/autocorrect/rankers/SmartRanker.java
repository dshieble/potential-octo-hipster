package edu.brown.cs.sjl2.autocorrect.rankers;

import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * SmartRanker is a ranker that makes uses BiUnigram above other rankers to
 * rank words.
 *
 * @author sjl2
 *
 */
public class SmartRanker implements MultipleRankers {

  private DictionaryTrie trie;
  private String[] input;
  private MultipleWordRankers rankers;


  /**
   * Constructs a SmartRanker.
   * @param trie The DictionaryTrie to be used for ranking.
   */
  public SmartRanker(DictionaryTrie trie) {
    rankers = new MultipleWordRankers(trie);
    rankers.addRanker(new BiUnigramRanker(trie));
    updateDictionary(trie);
    updateInput(new String[0]);
  }

  /**
   * Update the MultipleRanker used to hold the back up rankers.
   * @param other MultipleRanker with other rankers to be used as default for
   * ties in smart rank.
   */
  public void updateOtherRankers(MultipleRankers other) {
    rankers = (MultipleWordRankers) other.copyOf();
  }

  @Override
  public int compare(String o1, String o2) {
    return rankers.compare(o1, o2);
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
    rankers.updateDictionary(this.trie);
  }

  @Override
  public void updateInput(String[] newInput) {
    this.input = newInput.clone();
    rankers.updateInput(this.input);
  }

  @Override
  public WordRanker copyOf() {
    SmartRanker copy = new SmartRanker(trie);
    copy.updateInput(this.input.clone());
    return copy;
  }

  @Override
  public void addRanker(WordRanker ranker) {
    rankers.addRanker(ranker);

  }

  @Override
  public void addRankers(List<WordRanker> moreRankers) {
    this.rankers.addRankers(moreRankers);
  }

}
