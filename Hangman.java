import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;

public class Hangman extends Application {
	private TextField tfGuess = new TextField();
	private TextField tfLettersGuessed = new TextField();

	private Text[] text;
	private Label guessesRemaining;
	private int letters;
	private int lives;
	private int check = 0;
	private boolean guessed = false;
	private boolean canPlay = true;

	private ArrayList<Shape> body;
	private ObservableList<Node> children;
	private StringBuilder guessedLetters;
	private String theWord;
	private GridPane gridPane = new GridPane();
	private HBox error = new HBox();



	private void initGallows() { //draws the gallows where stickfigure will hang
		Line gallow1 = new Line(25, 25, 200, 25);
		gallow1.setStroke(Color.BLACK);
		gallow1.setStrokeWidth(3);
		children.add(gallow1);

		Line gallow2 = new Line(25, 25, 25, 300);
		gallow2.setStroke(Color.BLACK);
		gallow2.setStrokeWidth(3);
		children.add(gallow2);

		Line gallow3 = new Line(300, 300, 25, 300);
		gallow3.setStroke(Color.BLACK);
		gallow3.setStrokeWidth(3);
		children.add(gallow3);

		Line rope = new Line(200, 25, 200, 75);
		rope.setStroke(Color.BROWN);
		rope.setStrokeWidth(3);
		children.add(rope);

	}

	private void initBody() { //draws my stick figure person and makes them invisible
		body = new ArrayList<Shape>();

		Ellipse head = new Ellipse(200, 112, 35, 35);
		head.setStroke(Color.BLACK);
		head.setFill(Color.WHITE);
		head.setStrokeWidth(5);
		head.setVisible(false);
		children.add(head);
		body.add(head);

		Line torso = new Line(200, 200, 200, 150);
		torso.setStroke(Color.GREEN);
		torso.setStrokeWidth(5);
		torso.setVisible(false);
		children.add(torso);
		body.add(torso);

		Line leftArm = new Line(150, 150, 200, 175);
		leftArm.setStroke(Color.BLACK);
		leftArm.setStrokeWidth(5);
		leftArm.setVisible(false);
		children.add(leftArm);
		body.add(leftArm);

		Line rightArm = new Line(250, 150, 200, 175);
		rightArm.setStroke(Color.BLACK);
		rightArm.setStrokeWidth(5);
		rightArm.setVisible(false);
		children.add(rightArm);
		body.add(rightArm);

		Line leftLeg = new Line(200, 200, 175, 275);
		leftLeg.setStroke(Color.BLACK);
		leftLeg.setStrokeWidth(5);
		leftLeg.setVisible(false);
		children.add(leftLeg);
		body.add(leftLeg);

		Line rightLeg = new Line(200, 200, 225, 275);
		rightLeg.setStroke(Color.BLACK);
		rightLeg.setStrokeWidth(5);
		rightLeg.setVisible(false);
		children.add(rightLeg);
		body.add(rightLeg);
	}

	private void initBlanks(String word) { //puts blank lines for word that needs to be guessed

		Line[] blanks = new Line[word.length()];
		int xStart = 375;
		int lineLength = 25;
		int lineSpacing = 35;

		for(int i = 0; i < blanks.length; i++) {
			int xCoord = xStart + (lineSpacing * i);

			blanks[i] = new Line(xCoord, 225, xCoord - lineLength, 225);
			blanks[i].setStroke(Color.BLACK);
			blanks[i].setStrokeWidth(3);
			children.add(blanks[i]);
		}
	}

	private Text[] initText(String word) {
		word.toUpperCase();
		Text[] text = new Text[word.length()];
		int xStart = 355;
		int lineSpacing = 35;

		for(int i = 0; i < text.length; i++) { //adds the word's letters above the lines but sets them invisible
			int xCoord = xStart + (lineSpacing * i);

			text[i] = new Text(word.substring(i, i + 1));
			text[i].setFont(new Font(30));
			text[i].setX(xCoord);
			text[i].setY(220);
			text[i].setVisible(false);
			children.add(text[i]);
		}

		return text;
	}

	public void start(Stage primaryStage) {
		Pane pane = new Pane();
		error.setPadding(new Insets(10, 0, 10, 0));

		children = pane.getChildren();

		initGallows(); //draws the gallows
		initBody(); //draws the body but is invisble to player

		Scanner scan = new Scanner(System.in);
		System.out.println("Enter word: ");
		theWord = scan.nextLine(); 
		letters = theWord.length();

		guessedLetters = new StringBuilder();
		scan.close();

		initBlanks(theWord);

		text = initText(theWord.toUpperCase());

		lives = 6;

		gridPane.setHgap(5);
		gridPane.setVgap(5);

		gridPane.add(new Label("Enter a letter:"), 0, 0);
		gridPane.add(tfGuess, 1, 0);
		gridPane.add(new Label("Letters Guessed:"), 0, 1);
		gridPane.add(tfLettersGuessed, 1, 1);
		gridPane.add(new Label("Guesses Remaining: "), 0, 2);
		guessesRemaining = new Label(String.valueOf(lives));
		gridPane.add(guessesRemaining, 0, 3);
		gridPane.add(error, 0, 4);

		BorderPane borderPane = new BorderPane();
		borderPane.setRight(gridPane);
		borderPane.setCenter(pane);

		tfGuess.setOnAction(e -> playGame());

		Scene scene = new Scene(borderPane, 700, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void playGame() {
		Label errorMessage = new Label();
		String guess = tfGuess.getText();
		if(guess.length() == 0 && canPlay) {
			//guess box was empty
			//TO DO: Write error message
			guessed = false;
			error.getChildren().clear();
			errorMessage.setText("No letter guessed. Please enter a letter.");
			error.getChildren().add(errorMessage);

		}

		if(guess.length() > 1 && canPlay) { //if guess contains more than one letter, take the first letter
			if(guess.substring(0, 1) == " ") {
				guessed = false;
				error.getChildren().clear();
				errorMessage.setText("Spaces are not allowed.");
				error.getChildren().add(errorMessage);
			}
			else { 
				guess = guess.substring(0, 1);
			}
		}

		guess = guess.toUpperCase();
		tfGuess.clear(); //reset the text box

		if (guessedLetters.length() > 0 && canPlay) {
			//check if letter has already been guessed
			if (guessedLetters.indexOf(guess) > -1) {
				//give error message
				guessed = false;
				error.getChildren().clear();
				errorMessage.setText(guess.toUpperCase() + " has already been guessed.");
				error.getChildren().add(errorMessage);
			}
			
			else if(guess.startsWith(" ")) {
				guessed = false;
				error.getChildren().clear();
				errorMessage.setText("Spaces are not allowed");
				error.getChildren().add(errorMessage);
			}

			else if(canPlay) {
				guessedLetters.append(guess.toUpperCase());
				guessed = true;
				check = 0;
				for (int i = 0; i < theWord.length(); i++) {
					if(guess.equalsIgnoreCase(theWord.substring(i, i + 1))) {
						text[i].setVisible(true);
						check++;
						letters--;
					}
				}
			}
		}

		else if(canPlay) { //first guess
			guessedLetters.append(guess.toUpperCase());
			guessed = true;
			check = 0;
			for (int i = 0; i < theWord.length(); i++) {
				if(guess.equalsIgnoreCase(theWord.substring(i, i + 1))) {
					text[i].setVisible(true);
					check++;
					letters--;
				}
			}
		} 

		tfLettersGuessed.setText(guessedLetters.toString());

		if(check == 0 && guessed && canPlay) {
			lives --;
			guessesRemaining.setText(String.valueOf(lives));
			error.getChildren().clear();
			errorMessage.setText("Sorry, " + guess.toUpperCase() + " is not part of the word.");
			error.getChildren().add(errorMessage);
			
			if(lives == 5) {
				body.get(0).setVisible(true);
			}
			
			else if(lives == 4) {
				body.get(1).setVisible(true);
			}
			
			else if(lives == 3) {
				body.get(2).setVisible(true);
			}
			
			else if(lives == 2) {
				body.get(3).setVisible(true);
			}
			
			else if(lives == 1) {
				body.get(4).setVisible(true);
			}
			
			else if(lives == 0) {
				body.get(5).setVisible(true);
				canPlay = false;
			}
		}

		if (lives <= 0) {
			//game over
			canPlay = false;
			for(int i = 0; i < theWord.length(); i++) {
				text[i].setVisible(true);
			}
			error.getChildren().clear();
			errorMessage.setText("GAME OVER. YOU HAVE LOST, BETTER LUCK NEXT TIME");
			error.getChildren().add(errorMessage);
		}
		
		if (letters == 0) {
			//game won
			canPlay = false;
			error.getChildren().clear();
			errorMessage.setText("CONGRATULATIONS! YOU HAVE WON!");
			error.getChildren().add(errorMessage);
		}
	}
}
