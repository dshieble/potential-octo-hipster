package edu.brown.cs.sjl2.autocorrect.generators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.sjl2.autocorrect.DictionaryTrie;
import edu.brown.cs.sjl2.autocorrect.DictionaryTrie.Trie;

/**
 * This Class generates suggestions for an corrected input from a dictionary
 * using the Levenshtein Edit Distance. The generator takes in a max distance
 * that a word can be away in terms of substitutions, insertions, and deletions.
 *
 * The Dictionary that the Generator uses can be updated.
 *
 * @author sjl2
 *
 */
public class EditDistanceGenerator implements SuggestionGenerator {

  private int max;
  private DictionaryTrie trie;

  /**
   * Constructor for a Levenshtein Edit Distance suggestion generator.
   * @param trie The trie to use for finding edit distances
   * @param max The maximum edit distance for suggestions to be returned.
   */
  public EditDistanceGenerator(DictionaryTrie trie, int max) {
    this.trie = trie;
    this.max = max;
  }

  @Override
  public void updateDictionary(DictionaryTrie newTrie) {
    this.trie = newTrie;
  }

  private Set<String> generateHelper(Trie curr, String input, int distance) {
    Set<String> suggestions = new HashSet<String>();

    if (distance <= max) {
      String firstLetter = null; // Break shit if bad
      String rest = null; // Break shit if bad

      // Not empty
      if (!input.isEmpty()) {
        // Initialize parts of input
        firstLetter = input.substring(0, 1);
        rest = input.substring(1);

        // INSERTION
        // Correct for Insertion of First Character
        suggestions.addAll(generateHelper(curr, rest, distance + 1));
      } else if (curr.isWord() && !suggestions.contains(curr.getWord())) {
        // if input is empty and curr is word, add it. (also in distance).
        suggestions.add(curr.getWord());
      }

      List<Trie> kids = curr.listChildren();

      // Recur through all of the kids
      for (Trie child : kids) {
        if (!input.isEmpty()) {
          if (child.getLetter().equals(firstLetter)) {
            // NOTHING
            // On track, continue down children with rest, same distance)
            suggestions.addAll(generateHelper(child, rest, distance));
          } else {
            // SUBSTITUTION
            // Substituted firstLetter instead of child's letter.
            suggestions.addAll(generateHelper(child, rest, distance + 1));
          }
        }

        // DELETION
        // Assume child letter was deleted, and needs appended on input
        suggestions.addAll(generateHelper(child, input, distance + 1));

      }
    }

    return suggestions;
  }

  @Override
  public List<String> generate(String lastWord) {
    lastWord = lastWord.toLowerCase();
    return new ArrayList<String>(generateHelper(trie.getRoot(), lastWord, 0));
  }

  @Override
  public SuggestionGenerator copyOf() {
    return new EditDistanceGenerator(trie, max);
  }

}
