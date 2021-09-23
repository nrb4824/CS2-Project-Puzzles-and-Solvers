package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Nathan Borkholder
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** Arraylist to hold the row col of the two selected spots. index 0=row, index 1 = col*/
    private ArrayList<Integer> firstSelected = new ArrayList<>();
    private ArrayList<Integer> secondSelected =  new ArrayList<>();

    /** the current configuration */
    public HoppersConfig currentConfig;

    private String filename;

    /**
     * The constructor for HoppersModel
     * @param filename the file being passed in
     * @throws IOException when the file doesn't match the format
     */
    public HoppersModel(String filename) {
        try {
            this.filename = filename;
            this.currentConfig = new HoppersConfig(filename);
        }catch(IOException e){
            System.out.println("Error: incorrect file");
        }
    }

    /**
     * Solves the puzzle based on the given configuration, then changes the currentConfig to the next config in the solved solution.
     */
    public void hint() {
        Solver solver = new Solver(currentConfig);
        ArrayList<Configuration> path = solver.solve(false);
        if(this.winCheck()){
            alertObservers(new HoppersClientData("Already Solved!"));
        }
        else if(path==null){
            alertObservers(new HoppersClientData("No Solution!"));
        }
        else{
            for(int i = 0;i<path.size();++i){
                if(path.get(i).equals(currentConfig)){
                    HoppersConfig config = (HoppersConfig)path.get(i+1);
                    this.currentConfig=config;
                    break;
                }
            }
            alertObservers(new HoppersClientData("Next Step!"));
        }
    }

    /**
     * Checks to see if the puzzle is solved or not
     * @return true if the puzzle is solved
     */
    public boolean winCheck(){
        if(currentConfig.isGoal()){
            return true;
        }
            return false;
    }
    /**
     * Selects a space and stores the selected space
     * @param row the row of the selected space
     * @param col the col of the selected space
     */
    public void select(int row,int col){
        if(this.winCheck()){
            alertObservers(new HoppersClientData("Already Solved!"));
        }else {
            if (currentConfig.field[row][col] != '*') {
                if (firstSelected.size() > 0) {
                    secondSelected.add(0, row);
                    secondSelected.add(1, col);
                    int jumpedRow = (firstSelected.get(0) + secondSelected.get(0)) / 2;
                    int jumpedCol = (firstSelected.get(1) + secondSelected.get(1)) / 2;
                    if (currentConfig.field[jumpedRow][jumpedCol] == 'G' && currentConfig.field[row][col]=='.'){
                        jump();
                        alertObservers(new HoppersClientData("Jumped from (" + firstSelected.get(0) + ", " + firstSelected.get(1) + ")" +
                                " to (" + secondSelected.get(0) + ", " + secondSelected.get(1) + ")"));

                        firstSelected.clear();
                        secondSelected.clear();
                    } else {
                        alertObservers(new HoppersClientData("Can't jump from (" + firstSelected.get(0) + ", " + firstSelected.get(1) + ") to (" +
                                secondSelected.get(0) + ", " + secondSelected.get(1) + ")"));
                        firstSelected.clear();
                        secondSelected.clear();
                    }
                } else {
                    firstSelected.add(0, row);
                    firstSelected.add(1, col);
                    alertObservers(new HoppersClientData("Selected (" + firstSelected.get(0) + ", " + firstSelected.get(1) + ")"));
                }

            } else {
                alertObservers(new HoppersClientData("Invalid selection (" + row + ", " + col + ")"));
            }
        }
    }

    /**
     * jumps the frog from the first selected point to the second selected point
     */
    public void jump(){
        int jumpedRow = (firstSelected.get(0)+secondSelected.get(0))/2;
        int jumpedCol = (firstSelected.get(1)+secondSelected.get(1))/2;
        char frog = this.currentConfig.field[firstSelected.get(0)][firstSelected.get(1)];
        char empty = this.currentConfig.field[secondSelected.get(0)][secondSelected.get(1)];
        //Changes the original space to empty
        this.currentConfig.field[firstSelected.get(0)][firstSelected.get(1)] = empty;
        //Changes the jumped to space to the frog
        this.currentConfig.field[secondSelected.get(0)][secondSelected.get(1)] = frog;
        //Changes the jumped over space to empty
        this.currentConfig.field[jumpedRow][jumpedCol]=empty;

    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * Resets the game
     * @throws IOException when the file isn't to format
     */
    public void reset() {
        try {
            this.currentConfig = new HoppersConfig(filename);
        }
        catch(IOException e){
            System.out.println("Failed to read file");
        }
        alertObservers(new HoppersClientData("Loaded: " + this.filename));
        alertObservers(new HoppersClientData("Puzzle reset!"));
    }

    /**
     * Loads a game from a certain file
     * @param file the file to load the game from
     * @throws IOException when the file is in wrong format
     */
    public void loadFile(String file) {
        boolean failed = false;
        try{
            this.currentConfig = new HoppersConfig(file);
        }
        catch(IOException g) {
            failed=true;
            alertObservers(new HoppersClientData("Failed to load: " + file));

        }
        if(failed==false){
            this.filename = file;
            alertObservers(new HoppersClientData("Loaded: " + this.filename));
        }
    }
    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(HoppersClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
