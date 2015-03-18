package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.List;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * The SuggestionGenerator Interface is used to generate suggestions for an
 * autocorrect. Almost all of them depend on a dictoinary trie that be updated.
 * These classes can be copied as needed, and the suggestions are generated
 * using generate. If multiple are needed, MultipleSuggestions is recommended.
 *
 * @author sjl2
 *
 */
public interface SuggestionGenerator {

  /**
   * Change the dictionary trie that the Suggestion Generator is using.
   * @param trie The new dictionary trie to base the suggestions off of.
   */
  void updateDictionary(DictionaryTrie trie);

  /**
   * Generates a list of suggestions for the last word in input. This last word
   * should be the input of generate.
   * @param lastWord The String input that is the last word of the input. This
   * will be what the suggestions are based off of.
   * @return Returns a list of String suggestions for possible corrections
   * to the last word in input. This list will only include each unique word
   * once. It does not guarantee sorting.
   */
  List<String> generate(String lastWord);

  /**
   * Creates a copy of the generator.
   * @return Returns a copy of the SuggestionGenerator;
   */
  SuggestionGenerator copyOf();
}
