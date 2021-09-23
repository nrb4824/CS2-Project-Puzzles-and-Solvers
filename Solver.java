package puzzles.common.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Nathan Borkhlder
 * @author Clara Pitkins
 */
public class Solver {
    private int totalConfig;
    private int uniqueConfig;
    private int step=0;
    private Configuration initial;
    private ArrayList<Configuration> adj = new ArrayList<>();
    public ArrayList<Configuration> path = new ArrayList<>();
    private HashMap<Configuration,Configuration> predecessor = new HashMap<>();

    /**
     * The constructor for the solver class.
     * @param config the initial configuration
     */
    public Solver(Configuration config) {
        this.totalConfig = 0;
        this.uniqueConfig = 0;
        this.initial = config;
        adj.add(config);
        predecessor.put(config,null);
    }

    /**
     * The main method that solves the puzzle
     * @param normal a check to see if the print statment should run or not
     * @return The path of the shortest solution
     */
    public ArrayList<Configuration> solve(boolean normal) {
        while (adj.size() > 0) {
            Configuration current = adj.remove(0);
            if (current.isGoal()) {
                path.add(current);
                path(current);
                if(normal==true){
                    print();
                }
                return path;
            } else {
                for (Configuration child : current.getSuccessors()) {
                    if(predecessor.containsKey(child)){
                        totalConfig += 1;
                        continue;
                    } else{
                        predecessor.put(child, current);
                        adj.add(child);
                        uniqueConfig += 1;
                        totalConfig += 1;
                    }
                }
            }
        }
        if(normal) {
            System.out.println("No Solution!");
        }
        return null;
    }

    /**
     * Creates the path that the backtraker must take to get from the start to finish.
     * @param config the configuration that was the goal
     * @return an arraylist of the path.
     */
    public ArrayList<Configuration> path(Configuration config){
        if(config.equals(initial)){
            return path;
        }else{
            for(Configuration configuration: predecessor.keySet()){
                if(predecessor.get(config).equals(configuration)){
                    path.add(0,configuration);
                    return path(configuration);
                }
            }
        }
        return null;
    }

    /**
     * Correctly formats the print statements.
     */
    public void print(){
        System.out.println("Total configs: " + this.totalConfig);
        System.out.println("Unique configs: " + this.uniqueConfig);
        for(Configuration config: path){
            System.out.println("Step " + step + ": " + config.toString());
            ++this.step;
        }
    }
}
