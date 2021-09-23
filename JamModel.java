package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, JamClientData>> observers = new LinkedList<>();

    private ArrayList<Integer> firstSelected = new ArrayList<>();
    private ArrayList<Integer> secondSelected =  new ArrayList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    private String filename;

    public JamModel(String filename){
        try {
            this.filename = filename;
            this.currentConfig = new JamConfig(filename);
        }catch(IOException e){
            System.out.println("Error: incorrect file");
        }
    }

    public void hint() {
        Solver solver = new Solver(currentConfig);
        ArrayList<Configuration> path = solver.solve(false);
        if(this.winCheck()){
            alertObservers(new JamClientData("Already Solved!"));
        }
        else if(path==null){
            alertObservers(new JamClientData("No Solution!"));
        }
        else{
            for(int i = 0;i<path.size();++i){
                if(path.get(i).equals(currentConfig)){
                    JamConfig config = (JamConfig)path.get(i+1);
                    this.currentConfig=config;
                    break;
                }
            }
            alertObservers(new JamClientData("Next Step!"));
        }
    }

    public boolean winCheck(){
        if(currentConfig.isGoal()){
            return true;
        }
        return false;
    }

    public void select(int row, int col){
        if(this.winCheck()){
            alertObservers(new JamClientData("Already Solved!"));
        }
        else{
            if(firstSelected.size()> 0){
                secondSelected.add(0, row);
                secondSelected.add(1, col);

            }
        }
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, JamClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(JamClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
