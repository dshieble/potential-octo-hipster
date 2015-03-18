package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * ExactMatchGenerator generates a single suggestion that is the match of the
 * input if it is contained in the dictionary. Useful as a default generator.
 *
 * @author sjl2
 *
 */
public class ExactMatchGenerator implements SuggestionGenerator {

  private DictionaryTrie trie;

  /**
   * Constructor for ExactMatchGenerator.
   * @param trie The trie to use for finding matches.
   */
  public ExactMatchGenerator(DictionaryTrie trie) {
    this.trie = trie;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  @Override
  public List<String> generate(String lastWord) {
    lastWord = lastWord.toLowerCase();
    List<String> suggestions = new ArrayList<String>();
    if (trie.isWord(lastWord)) {
      suggestions.add(lastWord);
    }
    return suggestions;
  }

  @Override
  public SuggestionGenerator copyOf() {
    return new ExactMatchGenerator(trie);
  }

}
