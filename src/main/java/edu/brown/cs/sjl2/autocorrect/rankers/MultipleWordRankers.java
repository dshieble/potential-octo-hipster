package edu.brown.cs.sjl2.autocorrect.rankers;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * MultipleRankers is a Ranker that allows for the comparison of Strings for
 * multiple Rankers. This is done by storing the Rankers in the order of
 * application. Then, the first ranker is applied. It's result is returned
 * unless zero. In the case of ties, the result uses the next ranker in the
 * list. If all rankers yield zero, the strings are compared by their natural
 * ordering using compareTo.
 * @author sjl2
 *
 */
public class MultipleWordRankers implements MultipleRankers {

  private List<WordRanker> rankers;
  private DictionaryTrie trie;
  private String[] input;

  /**
   * Constructor for MultipleRankers. Initializes with a dictionary trie and
   * zero input. This class is not necessarily useful until input is updated
   * with updateInput(String[] input).
   * @param trie The dictionary to use to rank words. It is the form of a
   * DictionaryTrie.
   */
  public MultipleWordRankers(DictionaryTrie trie) {
    this.trie = trie;
    this.input = new String[0];
    this.rankers = new ArrayList<WordRanker>();
    addRanker(new ExactMatchRanker());
  }

  /**
   * Constructor for MultipleRankers. Initializes with a dictionary trie and
   * no input. Updates the tries and input for all of the rankers added upon
   * construction and saves them. The order of the rankers in this list will
   * be preserved as the order to apply the rankers.
   * @param trie The DictionaryTrie to rank the words.
   * @param rankers A List of WordRankers that determine the order of a list of
   * words based on the trie and input.
   */
  public MultipleWordRankers(DictionaryTrie trie, List<WordRanker> rankers) {
    this.rankers = new ArrayList<WordRanker>(rankers);
    updateDictionary(trie);
    updateInput(new String[0]);
  }


  @Override
  public void addRanker(WordRanker ranker) {
    ranker.updateDictionary(trie);
    ranker.updateInput(input);
    rankers.add(ranker);
  }

  @Override
  public void addRankers(List<WordRanker> moreRankers) {
    for (WordRanker r : moreRankers) {
      addRanker(r);
    }
  }

  @Override
  public int compare(String o1, String o2) {
    for (WordRanker r : rankers) {
      int result = r.compare(o1, o2);
      if (result != 0) {
        return result;
      }
    }

    return o1.compareTo(o2);
  }

  @Override
  public WordRanker copyOf() {
    MultipleWordRankers copy = new MultipleWordRankers(trie);
    copy.updateInput(input.clone());
    for (WordRanker r : rankers) {
      copy.addRanker(r.copyOf());
    }

    return copy;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
    for (WordRanker r : rankers) {
      r.updateDictionary(newTrie);
    }
  }

  @Override
  public void updateInput(String[] newInput) {
    this.input = newInput.clone();
    for (WordRanker r : rankers) {
      r.updateInput(newInput);
    }
  }



}
