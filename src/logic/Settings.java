package logic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Settings extends JFrame implements ActionListener {;
	private JTextField blackMinutes, blackSeconds, whiteMinutes, whiteSeconds, boardWidth;
	private JCheckBox mirroredBox;
	
	public Settings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Chess GUI Settings");
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(8, 1));
		settingsPanel.add(new JLabel("Settings", JLabel.CENTER));
		
		JPanel heightPanel = new JPanel();
		heightPanel.add(new JLabel("Height:", JLabel.CENTER));
		boardWidth = new JTextField();
		boardWidth.setPreferredSize(new Dimension(50, 22));
		heightPanel.add(boardWidth);
		
		settingsPanel.add(heightPanel);
		
		settingsPanel.add(new JLabel("White"));
		JPanel whiteTime = new JPanel(new GridLayout(1, 4));
		whiteMinutes = new JTextField();
		whiteTime.add(whiteMinutes);
		whiteTime.add(new JLabel("Minutes"));
		whiteSeconds = new JTextField();
		whiteTime.add(whiteSeconds);
		whiteTime.add(new JLabel("Seconds"));
		
		settingsPanel.add(whiteTime);
		
		settingsPanel.add(new JLabel("Black"));
		JPanel blackTime = new JPanel(new GridLayout(1, 4));
		blackMinutes = new JTextField();
		blackTime.add(blackMinutes);
		blackTime.add(new JLabel("Minutes"));
		blackSeconds = new JTextField();
		blackTime.add(blackSeconds);
		blackTime.add(new JLabel("Seconds"));
		
		settingsPanel.add(blackTime);
		
		mirroredBox = new JCheckBox("Mirrored");
		mirroredBox.setHorizontalAlignment(JCheckBox.CENTER);
		
		settingsPanel.add(mirroredBox);
		
		JButton start = new JButton("Start");
		start.addActionListener(this);
		
		settingsPanel.add(start);
		
		add(settingsPanel);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Start")) {
			int whiteMinutes, blackMinutes, whiteSeconds, blackSeconds, boardWidth;
			String error = "";
			try {
				error = "White's minutes";
				whiteMinutes = Math.abs(Integer.parseInt(this.whiteMinutes.getText()));
				error = "White's seconds";
				whiteSeconds = Math.abs(Integer.parseInt(this.whiteSeconds.getText()));
				
				error = "Black's minutes";
				blackMinutes = Math.abs(Integer.parseInt(this.blackMinutes.getText()));
				error = "Black's seconds";
				blackSeconds = Math.abs(Integer.parseInt(this.blackSeconds.getText()));
				
				error = "The board width";
				boardWidth = Math.abs(Integer.parseInt(this.boardWidth.getText()));
			}
			catch(NumberFormatException n) {
				JOptionPane.showMessageDialog(this, error + " needs to be a positive integer!", "Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				JFrame boardFrame = new JFrame();
				boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				boardFrame.setTitle("Chess GUI");
				Board board = new Board(boardFrame, mirroredBox.isSelected(), boardWidth, whiteMinutes, whiteSeconds, blackMinutes, blackSeconds);
				boardFrame.setContentPane(board);
				boardFrame.pack();
				boardFrame.setLocationRelativeTo(null);
				boardFrame.setResizable(true);
				boardFrame.setVisible(true);
				board.setLayout(null);
				board.requestFocusInWindow();
				new BoardListener(board, boardFrame);
				dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
