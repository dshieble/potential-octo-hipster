package edu.brown.cs.sjl2.autocorrect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.sjl2.autocorrect.generators.EditDistanceGenerator;
import edu.brown.cs.sjl2.autocorrect.generators.ExactMatchGenerator;
import edu.brown.cs.sjl2.autocorrect.generators.MultipleSuggestions;
import edu.brown.cs.sjl2.autocorrect.generators.PrefixGenerator;
import edu.brown.cs.sjl2.autocorrect.generators.SplitWordGenerator;
import edu.brown.cs.sjl2.autocorrect.generators.SuggestionGenerator;
import edu.brown.cs.sjl2.autocorrect.rankers.BigramRanker;
import edu.brown.cs.sjl2.autocorrect.rankers.MultipleRankers;
import edu.brown.cs.sjl2.autocorrect.rankers.MultipleWordRankers;
import edu.brown.cs.sjl2.autocorrect.rankers.SmartRanker;
import edu.brown.cs.sjl2.autocorrect.rankers.UnigramRanker;
import edu.brown.cs.sjl2.autocorrect.rankers.WordRanker;

/**
 * Autocorrect is a class designed to generate suggestions for corrections based
 * on an input. It does this by bringing together several generators and rankers
 * to generate potential suggestions and then to rank them. The class
 * is instantiated through its builder class which allows you to fine tune the
 * options for generating suggestions.
 *
 * @author sjl2
 *
 */
public final class Autocorrect {

  private SuggestionGenerator generator;
  private WordRanker ranker;

  /**
   * Builder Class for Autocorrect. Allows for the generators to be selected
   * before building. Also has an option for smart rank. If no generators are
   * added then it defaults to exact match generation only. Default Ranking
   * will be used unless smart rank is toggled.
   *
   * @author sjl2
   *
   */
  public static class Builder {

    private DictionaryTrie trie;
    private MultipleSuggestions generators;
    private MultipleRankers rankers;

    /**
     * Constructor for an Autocorrect.Builder class. Initializes with a File to
     * obtain words for its internal dictionary.
     * @param dictionaryFile A File that contains words to be used for
     * suggestions
     * @throws IOException Throws IOExceptions on faulty file inputs.
     */
    public Builder(File dictionaryFile) throws IOException {
      trie = new DictionaryTrie(dictionaryFile);
      establishGenerators();
      establishWordRankers();
    }

    /**
     * Constructor for an Autocorrect.Builder class. Initializes with an array
     * of Strings represnting words that will be the basis of suggestions.
     * @param words An array of words to be used for suggestions.
     */
    public Builder(Collection<String> words) {
      trie = new DictionaryTrie(words);
      establishGenerators();
      establishWordRankers();
    }

    /**
     * Private Constructor used for copying.
     */
    private Builder(DictionaryTrie trie) {
      this.trie = trie;
      establishGenerators();
      establishWordRankers();
    }

    /**
     * Constructor for a Autocorrect.Builder class. Initializes with a List of
     * Files to obtain words for its internal dictionary.
     * @param dictionaryFiles A list of files containing words to base the
     * suggestions off of.
     * @throws IOException Throws IOExceptions on faulty file inputs.
     */
    public Builder(List<File> dictionaryFiles) throws IOException {
      trie = new DictionaryTrie(dictionaryFiles);
      establishGenerators();
      establishWordRankers();
    }

    private void establishGenerators() {
      generators = new MultipleSuggestions(trie);
    }

    private void establishWordRankers() {
      rankers = new MultipleWordRankers(trie);
      rankers.addRanker(new BigramRanker(trie));
      rankers.addRanker(new UnigramRanker(trie));
    }

    /**
     * Activates Smart Ranking in Autocorrect.
     * @param smart The boolean for whether smart ranking is used.
     * @return Returns an this builder.
     */
    public Autocorrect.Builder useSmartRank(Boolean smart) {
      if (smart) {
        SmartRanker smartRanker = new SmartRanker(this.trie);
        smartRanker.updateOtherRankers(rankers);
        this.rankers = smartRanker;
      }
      return this;
    }

    /**
     * Activates Levenshtein Edit Distance suggestion generator for Autocorrect.
     * @param led The boolean for whether to activate
     * @param max The maximum edit distance to generate LED solutions.
     * @return Returns this builder.
     */
    public Autocorrect.Builder useLED(Boolean led, int max) {
      if (led) {
        this.generators.addGenerator(new EditDistanceGenerator(trie, max));
      }
      return this;
    }

    /**
     * Activates the Prefix Generator for Autocorrect.
     * @param prefix The boolean for whether to activate prefix.
     * @return Returns this builder.
     */
    public Autocorrect.Builder usePrefix(Boolean prefix) {
      if (prefix) {
        this.generators.addGenerator(new PrefixGenerator(trie));
      }
      return this;
    }

    /**
     * Activates the Whitespace Generator for Autocorrect.
     * @param whitespace The boolean for whether to activate whitespace.
     * @return Returns this builder.
     */
    public Autocorrect.Builder useWhitespace(boolean whitespace) {
      if (whitespace) {
        this.generators.addGenerator(new SplitWordGenerator(trie));
      }
      return this;
    }

    /**
     * Adds additional generators to the builder for autocorrect.
     * @param generator A generator to add.
     * @return Returns this builder.
     */
    public Autocorrect.Builder addGenerator(SuggestionGenerator generator) {
      this.generators.addGenerator(generator);
      return this;
    }

    /**
     * Adds additional rankers to the builder for autocorrect.
     * @param ranker A ranker to add.
     * @return Returns this builder.
     */
    public Autocorrect.Builder addRanker(WordRanker ranker) {
      rankers.addRanker(ranker);
      return this;
    }

    /**
     * Getter for the Trie of this Autocorrect Builder.
     * @return Returns a DictionaryTrie that is the trie that will be used to
     * build an Autocorrect.
     */
    public DictionaryTrie getTrie() {
      return trie;
    }

    /**
     * Getter for the SuggestionGenerator of this Autocorrect Builder.
     * @return Returns a SuggestionGenerator that will be used in Autocorrect.
     */
    public SuggestionGenerator getGenerators() {
      return generators.copyOf();
    }

    /**
     * Getter for the WordRanker of this AutocorrectBuilder.
     * @return Returns a WordRanker that will be used in Autocorrect.
     */
    public WordRanker getWordRankers() {
      return rankers.copyOf();
    }

    /**
     * Creates a copy of the builder.
     * @return Returns a copy of this builder.
     */
    public Builder copyOf() {
      Builder copy = new Builder(trie);
      copy.generators = (MultipleSuggestions) getGenerators();
      copy.rankers = (MultipleRankers) getWordRankers();
      return copy;
    }

    /**
     * Builds an Autocorrect class to the specifications of the builder.
     * @return Returns an Autocorrect with information edited through the
     * builder.
     */
    public Autocorrect build() {
      if (generators.getSize() == 0) {
        generators.addGenerator(new ExactMatchGenerator(trie));
      }
      return new Autocorrect(this);
    }


  }

  private Autocorrect(Builder builder) {
    this.generator = builder.getGenerators();
    this.ranker = builder.getWordRankers();
  }

  /**
   * Finds and ranks all the best corrections for the input.
   * @param input The input that is to be corrected. The last word will be
   * replaced with the generated suggestions. Everything before the last word
   * will be returned in lower case and each word will be separated by spaces
   * without punctuation.
   * @return Returns a List of Strings sorted by the built-in ranker. Each
   * suggestion will show up once.
   */
  public List<String> getAllSuggestions(String input) {
    List<String> suggestions = new ArrayList<String>();

    input = input.replaceAll("[\\p{Punct}]+", " ");
    int l = input.length();
    boolean stillTyping = l == 0 || Character.isWhitespace(input.charAt(l - 1));

    String[] words = input.toLowerCase().split("[\\s]+");
    int numWords = words.length;

    if (stillTyping || numWords == 0) {
      // Still Typing -> No suggestions!
      // Empty Input -> No suggestions!
      return suggestions;
    }


    String lastWord = words[numWords - 1];

    suggestions = generator.generate(lastWord);

    ranker.updateInput(words);
    Collections.sort(suggestions, ranker);


    // Clean up rest of input
    StringBuilder outPrefix = new StringBuilder();
    for (int i = 0; i < (numWords - 1); i++) {
      // Create Prefix
      String word = words[i];
      if (!word.isEmpty()) {
        outPrefix.append(words[i]);
        outPrefix.append(" ");
      }
    }

    String prefix = outPrefix.toString();

    // Prefix the suggestions with the original input
    for (int i = 0; i < suggestions.size(); i++) {
      suggestions.set(i, prefix + suggestions.get(i));
    }

    return suggestions;
  }

  /**
   * Obtain the top n suggestions for the last word of the input. If there are
   * not n suggestions, the function will return all of the suggestions.
   * @param n The maximum number of suggestions wanted.
   * @param input The input to find corrections for.
   * @return Returns a list of corrections for the input.
   */
  public List<String> getNSuggestions(int n, String input) {
    if (n < 0) {
      throw new IllegalArgumentException("N must be positive.");
    }

    List<String> suggestions = getAllSuggestions(input);
    if (suggestions.size() > n) {
      suggestions = suggestions.subList(0, n);
    }
    return suggestions;
  }



}
