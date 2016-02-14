package swf.solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Dictionary {
	private DictionaryNode root = new DictionaryNode();
	private int wordCount = 0;
	
	private int addWord(String word) throws IllegalArgumentException {
		DictionaryNode dn = root;
		for(int i=0; i<word.length(); i++) {
			// Throw out characters which aren't letters, raise an alert
			if(!Character.isLetter(word.charAt(i))) {
				System.out.println("Warning: \"" + word + "\" added to dictionary with non-letter character \"" + word.charAt(i) + "\" removed.");
			}
			else {
				if(i == word.length()-1) {
					try {
						dn.addLastLetter(word.charAt(i));
					}
					catch (Exception e) {
						System.out.println("Word \"" + word + "\" attemped to be added to dictionary twice, was rejected.");
					}
				}
				else {
					dn = dn.addLetter(word.charAt(i));
				}
			}
		}
		return wordCount++;
	}
	
	/**
	 * Returns -1 if not a word and there are no paths existing in this tree at all
	 * with the path defined by word.
	 * Returns 0 if not a word but there is a path existing in this tree defined by
	 * word. Example: PROGR returns 0 because it is not a word, but the path defined
	 * by PROGR exists because of the presence of the word PROGRAM in the dictionary trie.
	 * Returns 1 if in the dictionary.
	 */
	public int containsWord(String word) {
		word = word.toUpperCase();
		DictionaryNode node = root;
		while(word.length() > 0) {
			char letter = word.charAt(0);
			word = word.substring(1);
			node = node.getNextLetter(letter);
			if(node == null) { 
				return -1;
			}
		}
		return node.isEndOfWord() ? 1 : 0;
	}
	
	public static Dictionary makeDictionary(Dictionary dict, String dictfilename) throws IllegalArgumentException, IOException {
		long timetaken = System.currentTimeMillis();
		System.out.println("Filling dictionary with " + dictfilename + "...");
		String word;
		InputStream is = dict.getClass().getResourceAsStream(dictfilename);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while((word = br.readLine()) != null) {
			dict.addWord(word);
		}
		timetaken = System.currentTimeMillis() - timetaken;
		System.out.println(String.format("Dictionary filled. Time: %.3f s", (double)timetaken/1000));
		return dict;
	}
}
