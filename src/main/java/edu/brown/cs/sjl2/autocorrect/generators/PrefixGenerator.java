package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;
import edu.brown.cs.sjl2.autocorrect.DictionaryTrie.Trie;

/**
 * PrefixGenerator is a SuggestionGenerator that finds all the words in the
 * dictionary that the last word of the input is a prefix for.
 *
 * @author sjl2
 *
 */
public class PrefixGenerator implements SuggestionGenerator {

  private DictionaryTrie trie;

  /**
   * Constructor for PrefixGenerator.
   * @param trie The dictionary trie to base suggestions off of. This will be
   * used to find the suggestions that have a prefix of the input.
   */
  public PrefixGenerator(DictionaryTrie trie) {
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

    Trie node = trie.search(lastWord);

    if (node != null) {
      ArrayList<Trie> nodes = new ArrayList<Trie>();
      nodes.add(node);
      Trie curr;

      while (nodes.size() > 0) {
        curr = nodes.remove(0);
        nodes.addAll(curr.getChildren().values());

        if (curr.isWord()) {
          suggestions.add(curr.getWord());
        }
      }
    }

    return suggestions;
  }

  @Override
  public SuggestionGenerator copyOf() {
    return new PrefixGenerator(trie);
  }

}
