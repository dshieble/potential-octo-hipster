package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;
import edu.brown.cs.sjl2.autocorrect.DictionaryTrie.Trie;

/**
 * Suggestion Generator that determines sugggestions by splitting the lastWord
 * in two. The suggestions are returned in list format with the two words as
 * one string.
 *
 * @author sjl2
 *
 */
public class SplitWordGenerator implements SuggestionGenerator {

  private DictionaryTrie trie;

  /**
   * Constructor for a SplitWordGenerator. Built from a DictionaryTrie that
   * determines what are words and what are not.
   * @param trie DictionaryTrie to base split words off of.
   */
  public SplitWordGenerator(DictionaryTrie trie) {
    this.trie = trie;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  @Override
  public List<String> generate(String lastWord) {
    List<String> suggestions = new ArrayList<String>();

    String word2 = lastWord;
    String firstLetter;
    String rest;
    String word;

    Trie curr = trie.getRoot();

    while (curr != null && !word2.isEmpty()) {

      if (trie.isWord(word2) && curr.isWord()) {
        word = curr.getWord() + " " + word2;
        suggestions.add(word);
      }

      firstLetter = word2.substring(0, 1);
      rest = word2.substring(1);

      curr = curr.getChildCopy(firstLetter);
      word2 = rest;
    }

    return suggestions;
  }

  @Override
  public SuggestionGenerator copyOf() {
    return new SplitWordGenerator(trie);
  }

}
