package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author Nathan Borkholder
 * @author Clara Pitkins
 */

public class Clock  implements Configuration {
    private int hours;
    private int start;
    private int end;
    private int cursor;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start stop");
        } else {
            Clock clock = new Clock(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            Solver solver = new Solver(clock);
            solver.solve(true);
        }
    }

    /**
     * Constructor for clock
     *
     * @param hours The number of hours the clock has starting at 1 and going to the number of hours inclusively.
     *              It is guaranteed to be a positive integer that is greater than or equal to 1.
     * @param start The starting hour. If present it is guaranteed to be a positive integer and within the range of hours.
     * @param end   The ending hour. If present it is guaranteed to be a positive integer.
     */
    public Clock(int hours, int start, int end) {
        this.hours = hours;
        this.start = start;
        this.end = end;
        this.cursor = start;
        System.out.println("Hours: " + this.hours + ", Start: " + this.start + ", End: " + this.end);

    }

    /**
     * The copy constructor for clock
     *
     * @param clock  the clock that the copy constructor is based on
     * @param cursor
     */
    public Clock(Clock clock, int cursor) {
        this.start = clock.start;
        this.end = clock.end;
        this.hours = clock.hours;
        if (cursor > hours) {
            this.cursor = 1;
        } else if (cursor < 1) {
            this.cursor = hours;
        } else {
            this.cursor = cursor;
        }
    }

    /**
     * Creates a unique check to see if an object is equal to another object in the clock class
     *
     * @param o an object to check to see if it is equal. Needs to be a clock.
     * @return True if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Clock) {
            Clock clock = (Clock) o;
            return clock.hours == this.hours && clock.end == this.end && clock.start == this.start && clock.cursor == this.cursor;
        }
        return false;
    }

    /**
     * Finds a unique hash code for this clock.
     *
     * @return a number that is the hashcode for the clock.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.hours);
    }

    /**
     * Generates a linkedlist of successors from a given configuration
     *
     * @return a linked list, with all the children in it.
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new LinkedList<>();
        Clock successor1 = new Clock(this, this.cursor + 1);
        Clock successor2 = new Clock(this, this.cursor - 1);
        successors.add(successor1);
        successors.add(successor2);
        return successors;
    }

    /**
     * Checks to see if the configuration is the desired configuration
     *
     * @return true if it is the goal
     */
    @Override
    public boolean isGoal() {
        if (this.cursor == end) {
            return true;
        }
        return false;
    }

    /**
     * Gets the info necessary to print
     *
     * @return the cursor value
     */
    @Override
    public String toString() {
        return String.valueOf(this.cursor);
    }

}