package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Nathan Borkholder
 */


/**
 * Takes a file argument and creates a hopper config, solves the config, and outputs the steps taken to solve the config
 */
public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        HoppersConfig config = new HoppersConfig(args[0]);
        Solver solver = new Solver(config);
        solver.solve(true);
    }
}
