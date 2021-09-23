package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;


/**
 * @author Nathan Borkholder
 */

public class HoppersGUI extends Application implements Observer<HoppersModel, HoppersClientData> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** Creates the images that will be used in the game */
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));
    private Image lily_pad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));

    /** creates the field for model */
    private HoppersModel model;

    /** a 2d array to hold all the buttons */
    private Button buttons[][];

    /** The top label */
    public Label topLabel;

    public Stage stage;

    /**
     * Initialize the view
     */
    public void init() {
        this.model= new HoppersModel(getParameters().getRaw().get(0));
        this.model.addObserver(this);
        String filename = getParameters().getRaw().get(0);
        topLabel = new Label("Loaded: " + filename);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        //creates the overall borderpane
        BorderPane borderPane = new BorderPane();

        //Add a label to the top of the borderpane
        borderPane.setTop(topLabel);
        BorderPane.setAlignment(topLabel, Pos.CENTER);

        //add a flowPane to the bottom of the borderpane
        FlowPane flowPane = makeFlowPane();
        borderPane.setBottom(flowPane);
        borderPane.setAlignment(flowPane, Pos.CENTER);

        //add a gridPane to the center of the borderpane
        GridPane gridPane = makeGridPane();
        borderPane.setCenter(gridPane);
        borderPane.setAlignment(gridPane, Pos.CENTER);

        stage.sizeToScene();
        // add the border pane to the scene and display it
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Hoppers GUI");
        stage.setWidth(1000);
        stage.setHeight(1500);
        stage.show();
    }


    /**
     * A helper function that builds a grid of buttons to return.
     *
     * @return the grid pane
     */
    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(false);
        double width = water.getWidth();
        double height = water.getHeight();
        buttons=new Button[this.model.currentConfig.rowDim][this.model.currentConfig.colDim];
        for (int row = 0; row < this.model.currentConfig.rowDim; ++row) {
            for (int col = 0; col < this.model.currentConfig.colDim; ++col) {
                //Checks to see what type of spot the current row, col is looking at
                Button button = new Button();
               if(this.model.currentConfig.field[row][col]=='*'){
                    button.setGraphic(new ImageView(water));
                   button.setMinSize(height, width);
                   button.setMaxSize(height,width);
               }
               else if(this.model.currentConfig.field[row][col]=='.'){
                    button.setGraphic(new ImageView(lily_pad));
                   button.setMinSize(height, width);
                   button.setMaxSize(height,width);
               }
               else if(this.model.currentConfig.field[row][col]=='G'){
                    button.setGraphic(new ImageView(greenFrog));
                   button.setMinSize(height, width);
                   button.setMaxSize(height,width);
               }
               else if(this.model.currentConfig.field[row][col]=='R'){
                   button.setGraphic(new ImageView(redFrog));
                   button.setMinSize(height, width);
                   button.setMaxSize(height,width);
               }
                buttons[row][col]=button;
                int finalCol = col;
                int finalRow = row;
                button.setOnAction(event -> model.select(finalRow, finalCol));

                // JavaFX uses (x, y) pixel coordinates instead of
                // (row, col), so must invert when adding
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * makes the flow pane with the reset, undo, and cheat buttons
     * @return a flow pane
     */
    private FlowPane makeFlowPane(){
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        Button reset= new Button("Load");
        reset.setOnAction((event)->load());
        Button undo= new Button("Reset");
        undo.setOnAction((event)->reset());
        Button cheat= new Button("Hint");
        cheat.setOnAction((event)->hint());
        flowPane.getChildren().addAll(reset,undo,cheat);

        // set the vertical and horizontal gaps between the check boxes
        flowPane.setVgap(1);
        flowPane.setHgap(1);
        return flowPane;
    }

    /**
     * Loads a new game file from a selected file
     */
    private void load(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        this.model.loadFile(file.getAbsolutePath());
    }

    /**
     * Resets the game
     */
    private void reset(){
        this.model.reset();
    }

    /**
     * Displays the next move
     */
    private void hint(){
        this.model.hint();
    }

    /**
     * Updates the board and labels after each click, also checks to see if the hoppersClientData loaded a file.
     * @param hoppersModel the model that is currently being used
     * @param hoppersClientData a string with a message correlating to the click
     */
    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        if(hoppersClientData.toString().split(" ")[0].equals("Loaded:")){
            try{
                start(this.stage);
                this.stage.sizeToScene();
            }
            catch(Exception e){
                this.topLabel.setText("Error: board was not succesfully created from file");
            }
        }
        this.topLabel.setText(hoppersClientData.toString());
        for (int row = 0; row < this.model.currentConfig.rowDim; ++row) {
            for (int col = 0; col < this.model.currentConfig.colDim; ++col) {
                //Checks to see what type of spot the current row, col is looking at
                if (this.model.currentConfig.field[row][col] == '.') {
                    buttons[row][col].setGraphic(new ImageView(lily_pad));
                } else if (this.model.currentConfig.field[row][col] == '*') {
                    buttons[row][col].setGraphic(new ImageView(water));
                } else if (this.model.currentConfig.field[row][col] == 'G') {
                    buttons[row][col].setGraphic(new ImageView(greenFrog));
                } else if (this.model.currentConfig.field[row][col] == 'R') {
                    buttons[row][col].setGraphic(new ImageView(redFrog));
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
