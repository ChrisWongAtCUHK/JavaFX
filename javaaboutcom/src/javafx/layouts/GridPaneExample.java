package javafx.layouts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.HPos;

public class GridPaneExample extends Application {
    private final String[] gridStrings  = {"Rank", "Player", "Goals", "1", "Alan Shearer", };
    
	
    @Override
    public void start(Stage primaryStage) {
        //Create a GridPane for the Text Controls
        GridPane playerGrid = new GridPane();
        playerGrid.setHgap(10);
        playerGrid.setVgap(10);
        playerGrid.setPadding(new Insets(0, 10, 0, 10));

        //The title for the table spans for columns of the GridPane
        Text title = new Text("Top Scorers in English Premier League");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        playerGrid.add(title, 0,0,4,1);

        //Each set of three Text controls makes up a row of the table
        Text rankTitle = new Text("Rank");
        rankTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        playerGrid.add(rankTitle, 0,3);

        Text playerTitle = new Text("Player");
        playerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        playerGrid.add(playerTitle, 1,3);

        Text goalTitle = new Text("Goals");
        goalTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        playerGrid.add(goalTitle, 2,3);

        Text rank1 = new Text("1");
        rank1.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(rank1, HPos.CENTER);
        playerGrid.add(rank1, 0,4);

        Text player1 = new Text("Alan Shearer");
        player1.setFont(Font.font("Arial", 16));
        playerGrid.add(player1, 1,4);

        Text goals1 = new Text("260");
        goals1.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(goals1, HPos.CENTER);
        playerGrid.add(goals1, 2,4);

        Text rank2 = new Text("2");
        rank2.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(rank2, HPos.CENTER);
        playerGrid.add(rank2, 0,5);

        Text player2 = new Text("Andrew Cole");
        player2.setFont(Font.font("Arial", 16));
        playerGrid.add(player2, 1,5);

        Text goals2 = new Text("187");
        goals2.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(goals2, HPos.CENTER);
        playerGrid.add(goals2, 2,5);

        Text rank3 = new Text("3");
        rank3.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(rank3, HPos.CENTER);
        playerGrid.add(rank3, 0,6);

        Text player3 = new Text("Thierry Henry");
        player3.setFont(Font.font("Arial", 16));
        playerGrid.add(player3, 1,6);

        Text goals3 = new Text("175");
        goals3.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(goals3, HPos.CENTER);
        playerGrid.add(goals3, 2,6);
        
        Text rank4 = new Text("4");
        rank4.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(rank4, HPos.CENTER);
        playerGrid.add(rank4, 0,7);

        Text player4 = new Text("Frank Lampard");
        player4.setFont(Font.font("Arial", 16));
        playerGrid.add(player4, 1,7);

        Text goals4 = new Text("165");
        goals4.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(goals4, HPos.CENTER);
        playerGrid.add(goals4, 2,7);

        Text rank5 = new Text("5");
        rank5.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(rank5, HPos.CENTER);
        playerGrid.add(rank5, 0,8);

        Text player5 = new Text("Robbie Fowler");
        player5.setFont(Font.font("Arial", 16));
        playerGrid.add(player5, 1,8);

        Text goals5 = new Text("162");
        goals5.setFont(Font.font("Arial", 16));
        GridPane.setHalignment(goals5, HPos.CENTER);
        playerGrid.add(goals5, 2,8);
        
        //playerGrid.setGridLinesVisible(true);
        Scene scene = new Scene(playerGrid, 500, 500);
        
        primaryStage.setTitle("Goal Scorers!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Function to add node to grid
    public void gridAdd(GridPane grid, String string, Font font, HPos hpos, int columnIndex, int rowIndex){
    	Text text = new Text(string);
    	text.setFont(font);
        GridPane.setHalignment(text, hpos);
        grid.add(text, columnIndex, rowIndex);
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}