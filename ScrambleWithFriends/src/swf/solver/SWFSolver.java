package swf.solver;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class SWFSolver extends JFrame {

	private static final long serialVersionUID = 1L;

	// Solving engine
	private WordFinder2 finder;
	
	// Letter input
	private JTextField letterInput[][] = new JTextField[WordBoard.SIDE_LEN][WordBoard.SIDE_LEN];
	
	// Find button
	private JButton findButton;
	
	// Results display
	private JLabel resultCount;
	private JScrollPane resultsPane;
	private JList resultsList;
	private JLabel timeReport;
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		SWFSolver s = new SWFSolver();
	}
	
	public SWFSolver() {
		super("Scramble with Friends Solver v1.0 by Justin Churchill");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finder = new WordFinder2();
		
		JPanel all = new JPanel();
		all.setLayout(new BoxLayout(all, BoxLayout.LINE_AXIS));
		getContentPane().add(all);
		
		int borderWidth = 50;
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.add(Box.createVerticalStrut(borderWidth));
		left.add(Box.createGlue());
		left.add(letterInputArray());
		left.add(Box.createGlue());
		left.add(findButton());
		left.add(Box.createGlue());
		left.add(Box.createVerticalStrut(borderWidth));
		
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
		right.add(Box.createVerticalStrut(borderWidth));
		right.add(resultsArea());
		right.add(Box.createVerticalStrut(borderWidth));
		
		all.add(Box.createHorizontalStrut(borderWidth));
		all.add(Box.createGlue());
		all.add(left);
		all.add(Box.createHorizontalStrut(borderWidth));
		all.add(right);
		all.add(Box.createGlue());
		all.add(Box.createHorizontalStrut(borderWidth));
	
		findButtonPressed();
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	private JPanel resultsArea() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		result.add(Box.createGlue());
		resultsList = new JList();
		resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsPane = new JScrollPane(resultsList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultCount = new JLabel("Results (0)");
		result.add(resultCount);
		result.add(Box.createGlue());
		result.add(resultsPane);
		timeReport = new JLabel("");
		result.add(Box.createGlue());
		result.add(timeReport);
		result.add(Box.createGlue());
		return result;
	}
	
	private JPanel findButton() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.LINE_AXIS));
		result.add(Box.createGlue());
		result.add(new JLabel(new ImageIcon(getClass().getResource("/resource/tile.gif"))));
		result.add(Box.createHorizontalStrut(10));
		findButton = new JButton("Find Words!");
		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findButtonPressed();
			}
		});
		result.add(findButton);
		result.add(Box.createGlue());
		return result;
	}
	
	private JPanel letterInputArray() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		for(int i=0; i<WordBoard.SIDE_LEN; i++) {
			JPanel jp = new JPanel();
			jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
			result.add(jp);
			for(int j=0; j<WordBoard.SIDE_LEN; j++) {
				letterInput[i][j] = new JTextField("" + (char)('A' + i + j*WordBoard.SIDE_LEN));
				jp.add(letterInput[i][j]);
				letterInput[i][j].addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent arg0) {
						JTextField src = (JTextField)arg0.getSource();
						src.selectAll();
					}
					@Override
					public void focusLost(FocusEvent arg0) {
					}
				});
				letterInput[i][j].setPreferredSize(new Dimension(30,30));
				letterInput[i][j].setHorizontalAlignment(JTextField.CENTER);
			}
		}
		return result;
	}

	
	private void findButtonPressed() {
		long time = System.currentTimeMillis();
		char[][] board = new char[WordBoard.SIDE_LEN][WordBoard.SIDE_LEN];
		for(int i=0; i<WordBoard.SIDE_LEN; i++) {
			for(int j=0; j<WordBoard.SIDE_LEN; j++) {
				String text = letterInput[i][j].getText();
				if(!Character.isLetter(text.charAt(0))) {
					String[] message = new String[] {
							"Error in letter",
							"input at box #", 
							("{" + (i+1) + "," + (j+1) + "}"),
							"(from top left)"
							};
					resultsList.setListData(message);
					resultCount.setText("Results");
					return;
				}
				else {
					board[i][j] = text.charAt(0);
				}
			}
		}
		ArrayList<String> words = finder.findWords(board);
		resultsList.setListData(words.toArray());
		resultCount.setText("Results (" + words.size() + ")");
		time = System.currentTimeMillis() - time;
		timeReport.setText(String.format("Search took %.3f seconds.", (double)time/1000));
	}

}
