import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

/**
 * Author      : David Huang
 * Date        : 13 Feb. 2020
 * Description : Wordle.java creates a GUI for users to play a mimic
 * version of the popular word guessing game WORDLE
 */
public class Wordle extends JFrame implements ActionListener
{
	private WordBank          zzz;
	private GuessBank         zzz1;
	private GuessBank2        zzz2;
	private static JFrame     f;
	private JPanel            p;

	private JTextField[][] 	  fields;
	private JTextField		  winnerText, input;
	private JButton           reset;

	private int       		  tryNumber;

	private String[]		  wordBank, guessBank, guessBank2;
	private static String     wordle;
	private char[]            wordleToChar;
	private Color             backgroundColor, borderColor, greenColor, yellowColor;

	/**
	 * Constructor()
	 */
	public Wordle()
	{
		zzz = new WordBank();
		zzz1 = new GuessBank();
		zzz2 = new GuessBank2();

		f = new JFrame("WORDLE");
		p = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		borderColor     = new Color(58,58,60);
		backgroundColor = new Color(18,18,19);
		greenColor      = new Color(83,141,78);
		yellowColor     = new Color(181,159,59);

		// WORDLE FONT
		Font wordleFont = new Font("Neue Helvetica", Font.BOLD, 26);
		input           = new JTextField("");
		input.setFont(wordleFont);
		input.setEditable(true);

		// sets color of block and border + border thickness
		input.setBackground(backgroundColor);
		input.setBorder(new LineBorder(borderColor, 2));

		// sets color of font
		input.setForeground(Color.WHITE);

		c.fill      = GridBagConstraints.BOTH;

		// sets space around textfields
		c.insets    = new Insets(2,2,2,2);
        c.weightx   = 0.1;
        c.weighty   = 0.1;
        c.gridx     = 1;
        c.gridy     = 0;
		c.gridwidth = 3;

		// limit textfield to 5 character input
		input.setDocument(new JTextFieldLimit(5));
		input.addActionListener(this);
		input.setHorizontalAlignment(JTextField.CENTER);
		p.add(input, c);

		c.gridwidth = 1;

		fields = new JTextField[6][5];

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				fields[i][j] = new JTextField("");
				fields[i][j].setEditable(false);
				fields[i][j].setHorizontalAlignment(JTextField.CENTER);
				fields[i][j].setFont(wordleFont);
				fields[i][j].setForeground(Color.WHITE);
				fields[i][j].setBackground(backgroundColor);
				fields[i][j].setBorder(new LineBorder(borderColor,2));
				Dimension f = new Dimension(50,50);
				fields[i][j].setPreferredSize(f);
				c.gridx = j;
				c.gridy = i+1;
				p.add(fields[i][j], c);
			}
		}

		winnerText = new JTextField();
		Font winnerFont = new Font("Neue Helvetica", Font.BOLD, 14);
		winnerText.setFont(winnerFont);
		winnerText.setForeground(Color.WHITE);
		winnerText.setHorizontalAlignment(JTextField.CENTER);
		winnerText.setEditable(false);
		winnerText.setBackground(backgroundColor);
		winnerText.setBorder(new LineBorder(borderColor,2));

		c.gridx = 0;
		c.gridy = 0;
		p.add(winnerText, c);

		reset = new JButton();
		reset.setFont(winnerFont);
		reset.setForeground(Color.WHITE);
		reset.setHorizontalAlignment(JTextField.CENTER);
		reset.setBorder(new LineBorder(borderColor,2));
		reset.setBackground(backgroundColor);
		reset.setOpaque(true);
		reset.setText("Reset");
		reset.addActionListener(this);

		c.gridx = 4;
		c.gridy = 0;
		p.add(reset, c);

		p.setBackground(backgroundColor);
		getWordList();
		getWord();
		f.setContentPane(p);
	}

	/**
	 * Converts GuessBank.txt and WordBank.txt into arraylists
	 */
    public void getWordList()
	{
		wordBank = zzz.getWordBank();
		guessBank = zzz1.getGuessBank();
		guessBank2 = zzz2.getGuessBank2();
	}

	/**
	 * Creates a wordle
	 */
	public void getWord()
	{
		wordle       = wordBank[ThreadLocalRandom.current().nextInt(0, 2316)];
		wordleToChar = wordle.toCharArray();
	}

	/**
	 * Runs program
	 */
	public static void main(String arg[])
	{
		Wordle app = new Wordle();

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500,620);
		f.setVisible(true);
		f.setResizable(false);
		System.out.println(wordle);
	}

	public int countChar(String str, char c)
	{
		int count = 0;

		for(int i=0; i < str.length(); i++)
		{    if(str.charAt(i) == c)
				count++;
		}

		return count;
	}

	/**
	 * Handles mouse and enter inputs
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input)
		{
			char[] toChar = input.getText().toCharArray();
			String l = "";

			// word is in guessbank
			if (Arrays.asList(guessBank).contains(input.getText().toLowerCase()) || Arrays.asList(guessBank2).contains(input.getText().toLowerCase()))
			{
				try
				{
					// characters in guess not correctly guessed in place

					for (int x = 0; x < 5; x++)
					{
						if (toChar[x] != wordleToChar[x])
						{
							l += wordleToChar[x];
						}
					}

					// puts guess in textfields
					for (int i = 0; i < 5; i++)
					{
						// sets characters in textfields
						JTextField a = fields[tryNumber][i];
						a.setText(String.valueOf(toChar[i]).toUpperCase());
						a.setBackground(borderColor);
						// changes correct fields to green
						if (toChar[i] == (wordleToChar[i]))
						{
							a.setBackground(greenColor);
						}
						
						else
						{
							// if the guessed character shows up in the incorrectly guessed charcters
							if (countChar(l, toChar[i]) > 0)
							{
								// remove first such character from unguessed characters list
								// if there is two such characters, another guessed character can show up
								l = l.replaceFirst(Character.toString(toChar[i]), "");
								a.setBackground(yellowColor);
							}
							else
							{
							}
						}

					}
					
					// word guessed or not
					boolean a = true;

					for (int i = 0; i < 5; i++)
					{
						if (fields[tryNumber][i].getBackground() != greenColor)
						{
							a = false;
						}
					}

					if (a == true)
					{
						String[] messages = {"Genius", "Magnificent", "Impressive", "Splendid", "Great", "Phew"};
						winnerText.setText(messages[tryNumber]);
						winnerText.setBackground(greenColor);
						input.removeActionListener(this);
						input.setEditable(false);
					}

					tryNumber++;
					input.setText("");
				}
				// if fields out of exception due to tryNumber > 5 (word not guessed in 6 tries)
				catch (Exception c)
				{
					input.setText(wordle);
				}
			}
			// word is not in word bank
			else
			{
				System.out.println("That's not a valid word");
			}
		}

		if (e.getSource() == reset)
		{
			input.addActionListener(this);
			input.setEditable(true);
			winnerText.setText("");
			winnerText.setBackground(backgroundColor);
			getWord();
			tryNumber = 0;
			System.out.println(wordle);
			for (int i = 0; i < 6; i++)
			{
				for (int j = 0; j < 5; j++)
				{
					fields[i][j].setBackground(backgroundColor);
					fields[i][j].setText("");
				}
			}

		}
		repaint();
	}
}

