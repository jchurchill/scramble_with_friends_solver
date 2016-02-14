package swf.solver;

import java.io.IOException;
import java.util.ArrayList;

public class WordFinder2 {
	
	Dictionary dict = new Dictionary();
	String[] dictionaryFiles = {"/resource/CROSSWD.TXT", "/resource/CRSWD-D.TXT"};

	public WordFinder2() {
		try {
			initializeDictionary();
		} catch (Exception e) {
			System.out.println("Error: could not load dictionary.");
		}
	}
	
	private void initializeDictionary() throws IllegalArgumentException, IOException {
		for(String f : dictionaryFiles) {
			Dictionary.makeDictionary(dict, f);
		}
	}
	
	public ArrayList<String> findWords(char[][] board) {
		WordBoard wb = new WordBoard(board);
		return wb.getWords(dict);
	}
}
