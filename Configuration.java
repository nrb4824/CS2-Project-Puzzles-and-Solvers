package puzzles.common.solver;

import java.util.Collection;

public interface Configuration {
    /**
     * the get successors method that is implemented and overriden
     */
    public Collection<Configuration> getSuccessors();

    /**
     * The is goal method that is implemented and overriddent.
     * @return true if the config is the goal
     */
    public boolean isGoal();
    }