package edu.brown.cs.dshieble.Trie;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This  class represents a trie data structure. It can be queried to return
 * words based on prefix, levenshtein distance and whitespace algorithms.
 *
 *
 */
public class Trie {

  /**
   * list of words.
   */
  private Set<String> words;
  /**
   * root of tree.
   */
  private TrieNode root;

  /**
   * @param w - the dictionary of words for this trie to store
   */
  public Trie(final Set<String> w) {
    this.words = w;
    this.root = new TrieNode(this.words, "");
  }


  /**
   * @param searchWord - the word we are looking for
   * @return true if contained, false otherwise
   */
  public final boolean contains(final String searchWord) {
    TrieNode node = getNode(searchWord);
    if (node == null) {
      return false;
    } else {
      return node.getIsWord();
    }
  }

  /**
   * @param searchWord - the word we are looking for
   * @return the node that contains that word, or null
   */
  public final TrieNode getNode(final String searchWord) {
    return getNodeHelper(searchWord, "", root);
  }

  /**
   * this is a private method. Why do I need to javadoc it?
   * @param searchWord words
   * @param prefix so
   * @param node many
   * @return words
   */
  private TrieNode getNodeHelper(final String searchWord,
      final String prefix, final TrieNode node) {
    String prefixPlus = prefix.concat(node.getLetters());
    if (prefixPlus.equals(searchWord)) {
      return node;
    } else if (searchWord.startsWith(prefixPlus)) {
      HashSet<TrieNode> childNodes = new HashSet<TrieNode>();
      Iterator<TrieNode> childIterator = node.getChildren().iterator();
      while (childIterator.hasNext()) {
        TrieNode child = childIterator.next();
        TrieNode target = getNodeHelper(searchWord, prefixPlus, child);
        if (target != null) {
          return target;
        }
      }
    }
    return null;
  }

  /**
   * @param searchWord - the word we are splitting
   * @return the set of possible splits of this word
   */
  public final HashSet<String> wordsBySplitting(final String searchWord) {
    HashSet<String> suggestions = new HashSet<String>();
    for (int i = 1; i < searchWord.length(); i++) {
      String first = searchWord.substring(0, i);
      String second = searchWord.substring(i);
      TrieNode firstN = getNode(first);
      TrieNode secondN = getNode(second);
      if (firstN != null && secondN != null) {
        if (firstN.getIsWord() && secondN.getIsWord()) {
          suggestions.add(first.concat(" ").concat(second));
        }
      }
    }
    return suggestions;
  }

  /**
   * @param searchWord - the word we are looking around
   * @param distance - the max levenshtein distance from this word
   * @return the words with the input prefix
   */
  public final HashSet<String> wordsWithinLevenshtein(final String searchWord,
                  final int distance) {
    HashSet<String> toFill = new HashSet<String>();
    int[] row = new int[searchWord.length() + 1];
    for (int i = 0; i < row.length; i++) {
      row[i] = i;
    }
    levenshteinHelper(root, searchWord, distance, toFill, row, "");
    return toFill;
  }

  /**
   *
   * @param node my
   * @param searchWord hands
   * @param distance are
   * @param toFill typing
   * @param row words
   * @param prefix fdafbhf
   */
  private void levenshteinHelper(final TrieNode node,
                  final String searchWord, final int distance,
                  final HashSet<String> toFill, final int[] row,
                  final String prefix) {
    String letters = node.getLetters();
    String prefixPlus = prefix.concat(letters);
    int d = cumulativeEditDistance(searchWord, row, letters);
    if (node.getIsWord() && d <= distance) {
      toFill.add(prefixPlus);
    }
    if (d <= distance || prefixPlus.length() <= searchWord.length()) {
      Iterator<TrieNode> childIterator = node.getChildren().iterator();
      while (childIterator.hasNext()) {
        TrieNode child = childIterator.next();
        levenshteinHelper(child, searchWord, distance,
                        toFill, row.clone(), prefixPlus);
      }
    }
  }

  /**
   * @param s1 - the word on the top
   * @param row - the current row
   * @param letters - the letter to add to the column
   * @return the minimum levenshtein distance
   */
  public final int cumulativeEditDistance(final String s1,
      final int[] row, final String letters) {
    for (int l = 0; l < letters.length(); l++) {
      String letter = letters.substring(l, l + 1);
      int holder = row[0] + 1;
      int temp = 0;
      for (int i = 1; i <= s1.length(); i++) {
        if (s1.charAt(i - 1) == letter.charAt(0)) {
          temp = row[i - 1];
        } else {
          temp = Math.min(row[i - 1], Math.min(row[i], holder)) + 1;
        }
        row[i - 1] = holder; //Setting previous row[i]
        holder = temp; //What row[i] will be
      }
      row[row.length - 1] = holder;
    }
    return row[row.length - 1];
  }


  /**
   * @param searchPrefix - prefix of words we are searching for
   * @return the words with the input prefix
   */
  public final HashSet<String> wordsWithPrefix(final String searchPrefix) {
    return wordsWithPrefixHelper(searchPrefix, root, "");
  }

  /**
   *
   * @param searchPrefix private
   * @param node method
   * @param builtPrefix private
   * @return method
   */
  private HashSet<String> wordsWithPrefixHelper(
    final String searchPrefix, final TrieNode node,
    final String builtPrefix) {
    String newBuiltPrefix = builtPrefix.concat(node.getLetters());
    if (newBuiltPrefix.startsWith(searchPrefix)) {
      HashSet<String> outputSet = new HashSet<String>();
      node.getChildWords(builtPrefix, outputSet);
      return outputSet;
    } else {
      TrieNode child = node.getChild(builtPrefix, searchPrefix);
      if (child == null) {
        return new HashSet<String>();
      } else {
        return wordsWithPrefixHelper(searchPrefix, child, newBuiltPrefix);
      }
    }
  }

}
