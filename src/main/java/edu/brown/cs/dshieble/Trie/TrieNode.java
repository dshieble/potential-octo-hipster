package edu.brown.cs.dshieble.Trie;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * This  class represents a node in the trie data structure. It stores the
 * prefix or word that terminates in this node.
 *
 * Each node stores at most one word. Although nodes that would only have one
 * child are typically coalesced with that child, a parent and child node that
 * each store distinct valid words (not just prefixes) remain seperate
 *
 */
class TrieNode {

  /**
   * letters stored at this node.
   */
  private String letters;
  /**
   * whether or not node is word.
   */
  private Boolean isWord;
  /**
   * children of this node.
   */
  private HashSet<TrieNode> children;

  /**
   * Constructor for the TrieNode.
   *
   * @param words The set of words that all share a prefix with this node.
   * @param pre The prefix leading up to this node. This node's letter
   * should start as one longer than its prefix
   *
   */
  public TrieNode(final Set<String> words, final String pre) {
    String prefix = pre;
    int oldLength = prefix.length();
    isWord = false;
    if (words.size() > 0) {
      Boolean prefixTooShort = true;
      while (prefixTooShort) {
        String newLetter = "";
        Iterator<String> wordIterator = words.iterator();
        //Find the first word longer than prefix, assign new letter
        prefixTooShort = false;
        if (wordIterator.hasNext()) {
          String w = wordIterator.next();
          if (w.length() > prefix.length()) {
            newLetter = w.substring(prefix.length(), prefix.length() + 1);
            prefixTooShort = true;
          } else {
            prefixTooShort = false;
          }
        }
        //check if each other word also starts with extended prefix
        while (wordIterator.hasNext()) {
          String w = wordIterator.next();
          if (w.length() > prefix.length()) {
            if (!w.startsWith(prefix.concat(newLetter))) {
              prefixTooShort = false;
              break;
            }
          } else {
            prefixTooShort = false;
            break;
          }
        }
        //check if the prefix is equal to any word. If it is, stop here
        while (wordIterator.hasNext()) {
          if (wordIterator.next().equals(prefix)) {
            prefixTooShort = false;
            break;
          }
        }
        if (prefixTooShort) {
          prefix = prefix.concat(newLetter);
        }
      }
      if (prefix.length() > 0) {
        letters = prefix.substring(oldLength);
      } else {
        letters = "";
      }
      Hashtable<String, HashSet<String>> prefixHash =
          new Hashtable<String, HashSet<String>>();
      Iterator<String> wordIterator = words.iterator();
      while (wordIterator.hasNext()) {
        String w = wordIterator.next();
        if (w.equals(prefix)) {
          isWord = true;
        } else {
          String p = w.substring(0, prefix.length() + 1);
          if (!prefixHash.containsKey(p)) {
            prefixHash.put(p, new HashSet<String>());
          }
          prefixHash.get(p).add(w);
        }
      }
      this.children = new HashSet<TrieNode>();
      Enumeration<String> prefixEnumerator = prefixHash.keys();
      while (prefixEnumerator.hasMoreElements()) {
        String p = prefixEnumerator.nextElement();
        this.children.add(new TrieNode(prefixHash.get(p), prefix));
      }
    }
  }

  /**
   * @param prefix - The prefix up to this node
   * @param searchString - The string that we are searching the tree for
   * @return returns the child node who's string is a prefix
   * -returns null if no child node's string is a prefix of searchString
   */
  public TrieNode getChild(final String prefix, final String searchString) {
    Iterator<TrieNode> childIterator = children.iterator();
    while (childIterator.hasNext()) {
      TrieNode child = childIterator.next();
      String childString = prefix.concat(letters).concat(child.getLetters());
      if (searchString.startsWith(childString) || childString.startsWith(
          searchString)) {
        return child;
      }
    }
    return null;
  }

  /**
   * @param prefix - The prefix up to this node
   * @param toFill - the HashSet to populate with words
   */
  public void getChildWords(final String prefix,
      final HashSet<String> toFill) {
    if (isWord) {
      toFill.add(prefix.concat(letters));
    }
    Iterator<TrieNode> childIterator = children.iterator();
    while (childIterator.hasNext()) {
      TrieNode child = childIterator.next();
      child.getChildWords(prefix.concat(letters), toFill);
    }
  }

  /**
   * @return simple getter for this node's letters
   */
  public String getLetters() {
    return letters;
  }

  /**
   * @return simple getter for this node's children
   */
  public HashSet<TrieNode> getChildren() {
    return children;
  }

  /**
   * @return simple getter for this node's isWord
   */
  public Boolean getIsWord() {
    return isWord;
  }
}
