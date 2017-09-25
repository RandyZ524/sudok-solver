import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.*;

import java.util.Random;

public class zhangsudokusolver extends Application {
 public static void main(String[] args) {
	Application.launch(args); //Launches application
 }

	static int i, j, k;
	int position = 0;
	int currentdigit = 1;
	boolean currentlyrunning = false;
	AnimationTimer timer = null;
	
 
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("My first analog clock: the sequel");
		
		Group root = new Group();
		Scene scene = new Scene(root, 600, 600);
		
		Button startTimer = new Button();
		startTimer.setLayoutX(20);
		startTimer.setLayoutY(565);
		startTimer.setText("Start!");
		Button stopTimer = new Button();
		stopTimer.setLayoutX(75);
		stopTimer.setLayoutY(565);
		stopTimer.setText("Stop!");
		root.getChildren().addAll(startTimer, stopTimer);
		
		Rectangle[] backgroundsquares = new Rectangle[81];
		Text[][] currentnumbers = new Text[9][9];
		Line[] bigdaddylines = new Line[8];
		boolean[] userinputted = new boolean[81];
		int[] lastchecked = new int[81];
		
		for (i = 0; i < 81; i++) {
			Rectangle backgroundsquare = new Rectangle(30 + 60 * (i % 9), 10 + 60 * (i / 9), 60, 60);
			backgroundsquare.setStroke(Color.BLACK);
			backgroundsquare.setFill(Color.WHITE);
			
			currentnumbers[i / 9][i % 9] = new Text(45 + 60 * (i % 9), 55 + 60 * (i / 9), "");
			currentnumbers[i / 9][i % 9].setFont(Font.font("Verdana", FontWeight.BOLD, 40));
			root.getChildren().add(currentnumbers[i / 9][i % 9]);
			
			userinputted[i] = false;
			lastchecked[i] = 0;
			
			final int index = i;
			
			backgroundsquare.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
			
				public void handle(MouseEvent event) {
					
					if (backgroundsquare.getFill().equals(Color.WHITE) && !currentlyrunning) {
						
						for (i = 0; i < 81; i++) {
							backgroundsquares[i].setFill(Color.WHITE);
						}
						
						backgroundsquare.setFill(Color.GREEN);
						
						scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
							@Override
							
							public void handle(KeyEvent event) {
								
								try {
									
									if (!event.getText().equals("0")) {
										Integer.parseInt(event.getText());
										currentnumbers[index / 9][index % 9].setText(event.getText());
										currentnumbers[index / 9][index % 9].toFront();
										userinputted[index] = true;
									} else {
										currentnumbers[index / 9][index % 9].setText("");
										userinputted[index] = false;
									}
									
								} catch (NumberFormatException e) {
									System.out.println("nice try");
								}
								
							}
							
						});
						
					} else if (!currentlyrunning) {
						backgroundsquare.setFill(Color.WHITE);
					}
					
				}
				
			});
			
			backgroundsquares[i] = backgroundsquare;
			root.getChildren().add(backgroundsquares[i]);
		}
		
		for (i = 0; i < 4; i++) {
			bigdaddylines[i] = new Line(30 + 180 * i, 10, 30 + 180 * i, 550);
			bigdaddylines[i].setStrokeWidth(3);
			
			bigdaddylines[i + 4] = new Line(30, 10 + 180 * i, 570, 10 + 180 * i);
			bigdaddylines[i + 4].setStrokeWidth(3);
			root.getChildren().addAll(bigdaddylines[i], bigdaddylines[i + 4]);
		}
		
		startTimer.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				boolean invalidated = false;
				
				if (startTimer.getText().equals("Start!")) {
				
					for (i = 0; i < 81; i++) {
						
						if (backgroundsquares[i].getFill() == Color.GREEN) {
							backgroundsquares[i].setFill(Color.WHITE);
						}
						
						if (currentnumbers[i / 9][i % 9].getText().length() != 0) {
							
							for (j = 0; j < 9; j++) {
								
								if (j != i / 9 && currentnumbers[i / 9][i % 9].getText().equals(currentnumbers[j][i % 9].getText())) {
									invalidated = true;
									backgroundsquares[i].setFill(Color.RED);
									backgroundsquares[(9 * j) + (i % 9)].setFill(Color.RED);
								}
								
							}
							
							for (j = 0; j < 9; j++) {
								
								if (j != i % 9 && currentnumbers[i / 9][i % 9].getText().equals(currentnumbers[i / 9][j].getText())) {
									invalidated = true;
									backgroundsquares[i].setFill(Color.RED);
									backgroundsquares[i - (i % 9) + j].setFill(Color.RED);
								}
								
							}
							
							for (j = 0; j < 9; j++) {
								
								if (j != 3 * ((i / 9) % 3) + ((i % 9) / 3) && currentnumbers[i / 9][i % 9].getText().equals(currentnumbers[(i / 9) - ((i / 9) % 3) + (j / 3)][(i % 9) - ((i % 9) % 3) + (j % 3)].getText())) {
									invalidated = true;
									backgroundsquares[i].setFill(Color.RED);
									backgroundsquares[9 * ((i / 9) - ((i / 9) % 3) + (j / 3)) + (i % 9) - ((i % 9) % 3) + (j % 3)].setFill(Color.RED);
								}
								
							}
							
						}
						
					}
					
					if (!invalidated) {
						currentlyrunning = true;
						startTimer.setText("Reset!");
						
						timer = new AnimationTimer() {
							@Override
							
							public void handle(long now) {
							
								if (userinputted[position]) {
									position++;
								}
								
								for (currentdigit = lastchecked[position] + 1; currentdigit <= 9; currentdigit++) {
									
									if (SudokuCheck(currentnumbers, position, Integer.toString(currentdigit))) {
										currentnumbers[position / 9][position % 9].setText(Integer.toString(currentdigit));
										currentnumbers[position / 9][position % 9].toFront();
										lastchecked[position] = currentdigit;
										break;
									}
									
								}
								
								if (currentdigit > 9) {
									currentnumbers[position / 9][position % 9].setText("");
									lastchecked[position] = 0;
									position -= userinputted[position - 1] ? 2 : 1;
								} else {
									position++;
								}
								
								if (position > 80) {
									currentlyrunning = false;
									timer.stop();
								}
								
							}
							
						};
						
						timer.start();
					}
					
				} else {
					currentlyrunning = false;
					timer.stop();
					startTimer.setText("Start!");
					position = 0;
					
					for (i = 0; i < 81; i++) {
						currentnumbers[i / 9][i % 9].setText("");
						lastchecked[i] = 0;
						userinputted[i] = false;
						
						if (backgroundsquares[i].getFill() == Color.GREEN) {
							backgroundsquares[i].setFill(Color.WHITE);
						}
						
					}
					
				}
				
			}
			
		});
		
		stopTimer.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				
				if (currentlyrunning) {
				
					if (stopTimer.getText().equals("Stop!")) {
						stopTimer.setText("Resume!");
						timer.stop();
					} else {
						stopTimer.setText("Stop!");
						timer.start();
					}
					
				}
				
			}
			
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private static boolean SudokuCheck(Text[][] tempcurrentnumbers, int currentposition, String digitchecked) {
		
		for (int boxchecked = 0; boxchecked < 9; boxchecked++) {
			
			if (tempcurrentnumbers[currentposition / 9][boxchecked].getText().equals(digitchecked) && boxchecked != currentposition % 9) {
				return false;
			}
			
			if (tempcurrentnumbers[boxchecked][currentposition % 9].getText().equals(digitchecked) && boxchecked != currentposition / 9) {
				return false;
			}
			
			if (tempcurrentnumbers[(currentposition / 9) - ((currentposition / 9) % 3) + (boxchecked / 3)][(currentposition % 9) - ((currentposition % 9) % 3) + (boxchecked % 3)].getText().equals(digitchecked) && boxchecked != 3 * ((currentposition / 9) % 3) + ((currentposition % 9) / 3)) {
				return false;
			}
			
		}
		
		return true;
	}
	
}