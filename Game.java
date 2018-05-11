package games;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.util.*;
import java.io.*;

public class Game 
{
	private Grid grid;
	private int userRow; // keeps track of what row the user is in
	private int msElapsed; // keeps track of milliseconds elapsed
	private int timesGet; // keeps track of paper airplanes hit
	private int timesAvoid;// keeps track of clouds hit
	private int maxRows; // stores the total amount of rows
	private int maxCols; // stores the total amount of columns
	private int times; // keeps track of answered questions
	private double correct; // keeps track of the correct number of answers
	private int randomNum; // int used to store a random int
	private int temp; // int used to advance to the next level
	private String[] problems = new String[100]; // array for math problems
	private String[] choices = new String[400]; // array for choices to math problems
	private String[] answers = new String[100]; // array for answers to the math problems
	private Object[] candidates = { "A", "B", "C", "D" }; //choices for questions
	private Object[] yAndN = {"Yes", "No"}; //choices for play again

	public Game() 
	{
		maxRows = 4; // set rows to 4
		maxCols = 8; // sets columns to 8
		grid = new Grid(maxRows, maxCols); // creates a grid with the dimensions
		userRow = 0; // starts the user off in the 0th row
		msElapsed = 0; // starts game at 0 ms
		timesGet = 0; // starts paper plane score at 0
		timesAvoid = 0;// starts clouds score at 0
		times = 1; // set to one for the first time through
		correct = 0; // sets correct answers to 0
		temp = 0; // set temporary int to 0
		updateTitle();
		grid.setImage(new Location(userRow, 0), "pig.gif"); // renders the user image on the screen
	}// GAME
	
	public void scoreRecorder()
	{
		Heap h = new Heap();
	    
		//creates arrays for names and scores
		Object[] hsNames = new Object[100];
		int[] hsScores = new int[100];
	    
		//reads in scores
	    try 
		{
			File scores = new File("scores.txt");								
			Scanner s = new Scanner(scores);
				
			//inserts names and scores into heap
			while(s.hasNext())
			{
				String name = s.next();
				int score = s.nextInt();
				
				h.insert(new String(name), score);
			}
		    s.close();
		}// TRY

		catch (IOException e) 
		{
			System.out.println(e.getStackTrace());
		}// CATCH

	    //Reads in the new name and score
	    String enterName = JOptionPane.showInputDialog("Enter a name: ");
	    int enterInt = getScore();
	    
	    //inserts new name and score into heap
	    h.insert(new String(enterName), enterInt);
	    
	    
	    //empties heap and places names and scores into arrays
	    int i = 0;
	    while (!h.isEmpty())
	    {
	        HeapEntry node = (HeapEntry) h.remove();
	        hsNames[i] = node.getElement();
	        hsScores[i] = node.getPriority();	        
	        i++;
	    }//WHILE
	    
	    //prints top 10 scores
	    JOptionPane.showMessageDialog(null, "Top 10 High Scores\n" + "1. " + hsNames[0] + ": " + hsScores[0] +
	    		"\n2. " + hsNames[1] + ": " + hsScores[1] + "\n3. " + hsNames[2] + ": " + hsScores[2] +
	    		"\n4. " + hsNames[3] + ": " + hsScores[3] + "\n5. " + hsNames[4] + ": " + hsScores[4] +
	    		"\n6. " + hsNames[5] + ": " + hsScores[5] + "\n7. " + hsNames[6] + ": " + hsScores[6] +
	    		"\n8. " + hsNames[7] + ": " + hsScores[7] + "\n9. " + hsNames[8] + ": " + hsScores[7] +
	    		"\n10. " + hsNames[9] + ": " + hsScores[9]);
	    
	    //reads in the new score
	    try
		{
			FileOutputStream fos = new FileOutputStream ("scores.txt", true);
			PrintWriter pw = new PrintWriter(fos);
			
			pw.print(enterName + " ");
			pw.println(getScore());
		
			pw.close();
		}
		 
		catch(FileNotFoundException fnfe)
		{
			System.out.print("Unable to find scores.txt");
		}
	    
	    
	}//SCORES

	public void fileReader() 
	{
		//reads in the questions, answers, and choices
		try 
		{
			//Creates scanners for the questions and answers
			File qAndC = new File("questions.txt"); 
			Scanner qAndCScanner = new Scanner(qAndC);
			File a = new File("answers.txt"); 
			Scanner aScanner = new Scanner(a);
			
			//Read questions and answers into appropriate arrays
			for (int i = 0; i < 80; i++) 
			{
				String question = qAndCScanner.nextLine(); 
				problems[i] = question; 
				String correct = aScanner.nextLine(); 
				answers[i] = correct; 
				
				//Places the choice in correct spot in choice array
				for (int j = 1; j < 5; j++) 
				{
					String choice = qAndCScanner.nextLine(); 
					choices[j + (i * 4)] = choice; 
				}// FOR
			}// FOR
			qAndCScanner.close();
			aScanner.close();
		}// TRY
		
		catch (IOException e) 
		{
			System.out.println(e.getStackTrace());
		}// CATCH
	}// fileReader

	public void questionTime() 
	{
		//Runs every 10 seconds
		if ((msElapsed - (7500 * times)) == 0) // starts question loop every 7.5 seconds
		{
			//Run through the addition questions
			if (correct < 3) 
			{
				Random num = new Random();
				randomNum = num.nextInt(25);
			}// IF
			
			//Runs through subtraction questions
			else if (correct > 2 && correct < 6)
			{
				Random num = new Random();
				randomNum = (num.nextInt(25) + 25);
			}// ELSE IF
			
			//Runs through multiplication
			else if (correct > 5) 
			{
				Random num = new Random(); 
				randomNum = (num.nextInt(25) + 50);
			}// ELSE IF
			
			// chooses a question using randomNum
			int ans = JOptionPane.showOptionDialog(null, problems[randomNum] + "\nA. " 
					+ choices[(randomNum * 4) + 1] + "\nB. "+ choices[(randomNum * 4) + 2] 
					+ "\nC. " + choices[(randomNum * 4) + 3] + "\nD. "
					+ choices[(randomNum * 4) + 4], "Title", JOptionPane.DEFAULT_OPTION, 
					JOptionPane.QUESTION_MESSAGE, null, candidates, candidates[0]);
			
			String userChoice = choices[(randomNum * 4) + (ans + 1)];
			String correctChoice = answers[randomNum];
			
			//Runs if question is answered correctly
			if (userChoice.equals(correctChoice)) 
			{
				times++;
				correct++;
				JLabel subtractionInstructions = new JLabel();
				subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

				subtractionInstructions.setText("Good Job!!!");
				JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
			}// IF
			
			//if answered incorrectly will inform user and tell them the correct answer
			else 
			{
				times++;
				JLabel subtractionInstructions = new JLabel();
				subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

				subtractionInstructions.setText("Not quite! " + problems[randomNum] + " " + answers[randomNum]);
				JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
			}// ELSE
		}// IF
	}

	public void questionType() 
	{
		// Starts the subtraction portion of the game
		if (correct == 3 && temp == 0) 
		{
			temp++; // increases temp so the loop isn't infinite
			instructions();
		}// IF
	
		// Starts the multiplication portion of the game
		if (correct == 6 && temp == 1) 
		{
			temp++; // increases temp so the loop isn't infinite
			instructions();
		}// IF
	}// QUESTIONTYPE

	public void play() 
	{
		//reads in file
		fileReader(); 

		//welcomes the player and gives instructions
		welcome(); 
		instructions(); 
		
		//Runs the game
		while (!isGameOver()) 
		{
			handleKeyPress();
			grid.pause(100);

			if (msElapsed % 500 == 0) 
			{
				scrollLeft(); 
				populateRightEdge();
				questionType();
				questionTime();
			}// IF
			updateTitle();
			msElapsed += 100;
		}// WHILE
	}// play

	public void handleKeyPress() 
	{
		//gets last key pressed
		int key = grid.checkLastKeyPressed();

		//moves user down
		if ((key == 38) && (userRow > 0)) {
			handleCollision(new Location(userRow - 1, 0));
			grid.setImage(new Location(userRow, 0), null);
			userRow--;
		}// IF

		//moves user up
		if ((key == 40) && (userRow < maxRows - 1)) {
			handleCollision(new Location(userRow + 1, 0));
			grid.setImage(new Location(userRow, 0), null);
			userRow++;
		}// IF

		//puts user in correct position
		grid.setImage(new Location(userRow, 0), "pig.gif");
	}// handleKeyPress

	public void populateRightEdge() 
	{
		//runs through every row
		for (int i = 0; i < grid.getNumRows(); i++) 
		{
			//randomly chooses a cloud or an airplane
			int max = 5;
			int min = 0;
			int random = (int) (Math.random() * (max - min));
			if (random == 1)
				grid.setImage(new Location(i, grid.getNumCols() - 1),
						"cloud.gif");
			else if (random == 2)
				grid.setImage(new Location(i, grid.getNumCols() - 1),
						"airplane.gif");
			else
				grid.setImage(new Location(i, grid.getNumCols() -  1), null);
		}// FOR

	}// populateRightEdge

	public void scrollLeft() 
	{
		handleCollision(new Location(userRow, 1));

		for (int r = 0; r < grid.getNumRows(); r++) 
		{
			for (int c = 0; c < grid.getNumCols() - 1; c++) 
			{
				String replace = grid.getImage(new Location(r, c + 1));
				grid.setImage(new Location(r, c), replace);
				grid.setImage(new Location(r, c + 1), null);
			}// FOR
		}// FOR
	}// scrollLeft

	public void handleCollision(Location loc) 
	{		
		//identifies collision with cloud or airplane
		if(grid.getImage(loc) != null)
		{
			if(grid.getImage(loc).equals("cloud.gif"))
			{
				timesAvoid++;
			}//IF
			
			else if(grid.getImage(loc).equals("airplane.gif"))
			{
				timesGet++;
			}//ELSE IF
		}//IF
	}//HANDLECOLLISION

	public void welcome() 
	{
		//welcomes user when called
		JLabel welcomeMessage = new JLabel();
		welcomeMessage.setFont(new Font("Bauhaus 93", Font.BOLD, 60));
		welcomeMessage.setText("Welcome to When Pigs Fly!");
		JOptionPane.showMessageDialog(null, welcomeMessage, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
	}// welcome

	public void instructions() 
	{
		//shows basic instructions and addition instructions
		if (correct == 0) 
		{
			JLabel additionInstructions = new JLabel();
			additionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

			additionInstructions.setText("Wilbur needs help collecting his paper airplanes!\n "
							+ "Use addition to help him fly around and get them! But watch out for the clouds!");

			JOptionPane.showMessageDialog(null, additionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
		}// IF

		//shows subtraction instructions
		else if (temp == 1) // addition sequence
		{
			JLabel subtractionInstructions = new JLabel();
			subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

			subtractionInstructions.setText("You're doing an awesome job!! Now use subtraction to help Wilbur!");
			JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
		}// ELSE IF

		//shows multiplication instructions
		else if (temp == 2) 
		{
			JLabel subtractionInstructions = new JLabel();
			subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

			subtractionInstructions.setText("Almost there! Now Wilbur needs "
					+ "you to use multiplication to help him!");
			JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
		}// ELSE IF
	}// instructions

	public int getScore() 
	{
		return timesGet - timesAvoid;
	}// getScore

	public void updateTitle() 
	{
		grid.setTitle("Game:  " + getScore());
	}// updateTitle

	public boolean isGameOver() 
	{
		//finished game when 9 questions have been answered correctly
		if (correct == 9) 
		{
			JLabel subtractionInstructions = new JLabel();
			subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

			subtractionInstructions.setText("You win!!");
			JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
			
			scoreRecorder();
			grid.closeFrame();
			return playAgain();
		}// IF
		
		//finishes game when 5 clouds have been hit
		else if (getScore() == -5) 
		{
			JLabel subtractionInstructions = new JLabel();
			subtractionInstructions.setFont(new Font("Berlin Sans FB", Font.BOLD, 30));

			subtractionInstructions.setText("Game over! Wilbur got caught in too many clouds! You completed " + (correct / 9) + "% of the game.");
			JOptionPane.showMessageDialog(null, subtractionInstructions, "Phase II Game", JOptionPane.PLAIN_MESSAGE, null);
			scoreRecorder();
			grid.closeFrame();
			return playAgain();
		}// ELSE IF
		
		else
			return false;
	}// isGameOver
	
	public boolean playAgain() 
	{
		//uses buttons to prompt user to play again
		int playAgain = JOptionPane.showOptionDialog(null, "Would you like to play again?",
				"Play Again?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, yAndN, yAndN[0]);
						
		if (playAgain == 0) 
		{
			reset();
			return false;
		} 
		
		else
		{
			return true;
		} 
	}
 
	public void reset()
	{
		//resets game to original variables
		maxRows = 4;
		maxCols = 8;
		grid = new Grid(maxRows, maxCols); 
		userRow = 0; 
		msElapsed = 0; 
		timesGet = 0; 
		timesAvoid = 0;
		times = 1; 
		correct = 0;
		temp = 0; 
		updateTitle();
		grid.setImage(new Location(userRow, 0), "pig.gif");
	}
	
	public static void test() 
	{
		Game game = new Game();
		game.play();
	}// test

	public static void main(String[] args)
	{
		test();
	}// main
}// GAME