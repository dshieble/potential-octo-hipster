package edu.brown.cs.dshieble.autocorrect;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.MinMaxPriorityQueue;

import edu.brown.cs.dshieble.Trie.Trie;

/**
 * This class stores the Trie variable, contains the autocorrection
 * methods and contains the word ranking logic.
 *
 * Words are stored in following forms:
 * -Trie
 * -Bigram 2 layered hash  - bigramHash[word1][word2] = number
 * of times word1 is followed by word2 (if word1 is never followed by
 * any word, it will not appear in the bigramHash)
 * -Unigram 1 layered hash - unigramHash[word] = number of times "word"
 * appears in the text
 *
 *
 * @author dshieble
 *
 */
public class TrieManager {

  /**
   * stores unigram freq.
   */
  private HashMultiset<String> unigramHash;
  /**
   * stores bigram freq.
   */
  private Hashtable<String, HashMultiset<String>> bigramHash;
  /**
   * the trie for correction.
   */
  private Trie trie;

  /**
   * @param text - The corpus of text that we use to build the trie
   */
  public TrieManager(final String[] text) {
    unigramHash = HashMultiset.create();
    bigramHash = new Hashtable<String, HashMultiset<String>>();
    //populate bigram and unigram hashes
    for (int i = 0; i < text.length; i++) {
      if (text[i].length() > 0) {
        unigramHash.add(text[i]);
        if (i > 0) {
          if (!bigramHash.containsKey(text[i - 1])) {
            HashMultiset<String> newMultiset = HashMultiset.create();
            bigramHash.put(text[i - 1], newMultiset);
          }
          bigramHash.get(text[i - 1]).add(text[i]);
        }
      }
    }
    trie = new Trie(unigramHash.elementSet());
  }


  /**
   * Generates top 5 suggestions for the input word.
   * Assumes that the user input has at least one string
   * @param userInput - options
   * @param usePrefix - options
   * @param levenshtein - options
   * @param useWhitespace - options
   * @param useSmartComparator - options
   * @return a list storing the top 5 suggestions
   */
  public final String[] getSuggestions(final String[] userInput,
      final boolean usePrefix,
      final int levenshtein, final boolean useWhitespace,
      final boolean useSmartComparator) {
    int capacity = 5;
    HashMultiset<String> bHash = null;
    String prefix = userInput[userInput.length - 1];
    //If the previous word is not an actual word, then we ignore bigram
    Comparator<String> comparator = null;
    if (userInput.length >= 2) {
      if (bigramHash.containsKey(userInput[userInput.length - 2])) {
        bHash = bigramHash.get(userInput[userInput.length - 2]);
      } else {
        bHash = unigramHash;
      }
    } else {
      bHash = unigramHash;
    }
    if (useSmartComparator) {
      HashMultiset<String> unigramHash1 = HashMultiset.create();
      HashMultiset<String> bigramHash1 = HashMultiset.create();
      String prevWord = "";
      if (userInput.length >= 2) {
        prevWord = userInput[userInput.length - 2];
      }
      for (int i = 0; i < userInput.length - 1; i++) {
        if (userInput[i].length() > 0) {
          unigramHash1.add(userInput[i]);
          if (i > 0) {
            if (userInput[i - 1].equals(prevWord)) {
              bigramHash1.add(userInput[i]);
            }
          }
        }
      }
      comparator = new SmartComparator(unigramHash1, bigramHash1,
                      unigramHash, bHash);
    } else {
      comparator = new BasicComparator(unigramHash, bHash);
    }
    //order words by using a PQ
    MinMaxPriorityQueue<String> suggestionHeap =
        MinMaxPriorityQueue
        .orderedBy(comparator)
        .maximumSize(capacity)
        .create();
    HashSet<String> wordSet = new HashSet<String>();

    if (usePrefix) {
      wordSet.addAll(trie.wordsWithPrefix(prefix));
    }
    if (levenshtein > 0) {
      wordSet.addAll(trie.wordsWithinLevenshtein(prefix, levenshtein));
    }
    if (useWhitespace) {
      wordSet.addAll(trie.wordsBySplitting(prefix));
    }
    wordSet.removeAll(Collections.singleton(prefix));
    suggestionHeap.addAll(wordSet);
    int start = 0;
    if (trie.contains(prefix)) {
      start = 1;
    }
    String[] words = new String[Math.min(suggestionHeap.size()
        + start, capacity)];
    if (start == 1) {
      words[0] = prefix;
    }
    for (int i = start; i < words.length;  i++) {
      words[i] = suggestionHeap.removeFirst();
    }
    return words;
  }


  /**
   * Simple Getter.
   * @return the uni hash
   */
  public final HashMultiset<String> getUnigramHash() {
    return unigramHash;
  }

  /**
   * Simple Getter.
   * @return the bi hash
   */
  public final Hashtable<String, HashMultiset<String>> getBigramHash() {
    return bigramHash;
  }

  /**
   * Simple Getter.
   * @return the trie
   */
  public final Trie getTrie() {
    return trie;
  }
}
