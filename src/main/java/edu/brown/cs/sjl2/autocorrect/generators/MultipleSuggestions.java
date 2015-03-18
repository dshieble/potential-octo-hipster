package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;

/**
 * SuggestionGenerators is a SuggestionGenerator that is used to generate
 * suggestions from multiple generators.
 *
 * @author sjl2
 *
 */
public class MultipleSuggestions implements SuggestionGenerator {

  private List<SuggestionGenerator> generators;
  private DictionaryTrie trie;

  /**
   * Constructor for SuggestionGenerators. This Suggestion generator will not
   * generate suggestions until generators are added. It will not break though,
   * but it will not be useful.
   * @param trie The dictionary to use to create suggestions. This will be the
   * same throughout all the generators used by this class.
   */
  public MultipleSuggestions(DictionaryTrie trie) {
    this.generators = new ArrayList<SuggestionGenerator>();
    this.trie = trie;
  }

  /**
   * Constructor for SuggestionGenerators. Initializes the list of generators to
   * generators so that it can generate useful suggestions from initialization.
   * @param generators SuggestionGenerators to generate suggestions from
   * @param trie The dictionary trie to use for suggestions.
   */
  public MultipleSuggestions(List<SuggestionGenerator> generators,
      DictionaryTrie trie) {
    this.generators = generators;
    this.trie = trie;

    // Ensure same dictionary was used.
    for (SuggestionGenerator g : this.generators) {
      g.updateDictionary(trie);
    }
  }

  /**
   * Add a generator for additional suggestions.
   * @param generator The generator to add.
   */
  public void addGenerator(SuggestionGenerator generator) {
    generator.updateDictionary(trie);
    generators.add(generator);
  }

  /**
   * Getter for the number of generators being used in the MultipleSuggestion
   * Generator.
   * @return Returns an int of the number of generators used by this generator.
   */
  public int getSize() {
    return this.generators.size();
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
    for (SuggestionGenerator g : generators) {
      g.updateDictionary(newTrie);
    }

  }

  @Override
  public List<String> generate(String lastWord) {
    lastWord = lastWord.toLowerCase();
    Set<String> suggestions = new HashSet<String>();
    for (SuggestionGenerator g : generators) {
      suggestions.addAll(g.generate(lastWord));
    }
    return new ArrayList<String>(suggestions);
  }

  @Override
  public SuggestionGenerator copyOf() {
    MultipleSuggestions copy = new MultipleSuggestions(trie);
    for (SuggestionGenerator g : generators) {
      copy.addGenerator(g.copyOf());
    }
    return copy;
  }

}
