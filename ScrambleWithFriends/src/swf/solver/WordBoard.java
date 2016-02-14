package swf.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class WordBoard {
	public static final int SIDE_LEN = 4;
	
	BoardLetter[][] board = new BoardLetter[SIDE_LEN][SIDE_LEN];
	
	public WordBoard(char[][] board) {
		if(board.length != SIDE_LEN) {
			System.out.println("Bad board input to WordBoard constructor.");
		}
		else {
			for(int i=0; i<board.length; i++) {
				if(board[i].length != SIDE_LEN) {
					System.out.println("Bad board input to WordBoard constructor.");
				}
				else {
					for(int j=0; j<board[i].length; j++) {
						this.board[i][j] = new BoardLetter(Character.toUpperCase(board[i][j]), i, j);
					}
				}
			}
		}
	}
	
	public WordBoard(WordBoard wb) {
		for(int i=0; i< SIDE_LEN; i++) {
			for(int j=0; j< SIDE_LEN; j++) {
				board[i][j] = wb.board[i][j];
			}
		}
	}
	
	public ArrayList<String> getWords(Dictionary dict) {
		ArrayList<String> result = new ArrayList<String>();
		HashSet<String> hs = new HashSet<String>();
		for(int i=0; i< SIDE_LEN; i++) {
			for(int j=0; j< SIDE_LEN; j++) {
				ArrayList<String> wordsFromRoot = getWordsAtRoot(dict, new ArrayList<BoardLetter>(),i,j,"");
				for(String word : wordsFromRoot) {
					if(hs.add(word)) {
						result.add(word);
					}
				}
			}
		}
		Collections.sort(result, new StrLenComparator());
		return result;
	}
	
	private ArrayList<String> getWordsAtRoot(Dictionary dict, ArrayList<BoardLetter> used,
			int row, int col, String wordSoFar) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<BoardLetter> adj = getAdjacent(row,col);
		ArrayList<BoardLetter> used2 = new ArrayList<BoardLetter>();
		used2.addAll(used);
		used2.add(board[row][col]);
		wordSoFar += board[row][col].c;
		// Special case for Qu
		if(board[row][col].c == 'Q') {
			wordSoFar += 'U';
		}
		
		int wordPresence = dict.containsWord(wordSoFar);
		switch (wordPresence) {
		case -1:
			// Word not in dictionary and no point in searching further down this branch
			break;
		case 0:
			// Word not in dictionary but there might be some down this branch
			for(BoardLetter bl : adj) {
				if(!used2.contains(bl)) {
					result.addAll(getWordsAtRoot(dict, used2, bl.row, bl.col, wordSoFar));
				}
			}
			break;
		case 1:
			// Word in dictionary
			result.add(wordSoFar);
			for(BoardLetter bl : adj) {
				if(!used.contains(bl)) {
					result.addAll(getWordsAtRoot(dict, used2, bl.row, bl.col, wordSoFar));
				}
			}
			break;
		}
		
		return result;
	}
	
	private ArrayList<BoardLetter> getAdjacent(int row, int col) {
		ArrayList<BoardLetter> result = new ArrayList<BoardLetter>();
		int rr, cc;
		for(int r : new int[] {-1, 0, 1}) {
			for(int c : new int[] {-1, 0, 1}) {
				if(!(r==0 && c==0)) {
					rr = row + r;
					cc = col + c;
					if(rr >= 0 && rr < SIDE_LEN && cc >= 0 && cc < SIDE_LEN) {
						result.add(board[rr][cc]);
					}
				}
			}
		}
		return result;
	}
}

class BoardLetter {
	public char c;
	public int row;
	public int col;
	
	public BoardLetter(char c, int row, int col) {
		this.c = c;
		this.row = row;
		this.col = col;
	}
}

class StrLenComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		return arg1.length() - arg0.length();
	}
}
