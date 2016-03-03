package com.openet.decoding;

/**
 * Created by jay on 27/02/16.
 */
public class Team implements Comparable<Team> {
    private String name;
    private int points;
    private int goalsScored;
    private int goalsConceded;
    private Integer goalDifference;

    public Team(final String name) {
        this.name = name;
        this.points = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
    }

    public String getName() {
        return name;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void addGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public void addGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getGoalDifference() {
        if (goalDifference == null) {
            goalDifference = (goalsScored - goalsConceded);
        }

        return goalDifference;
    }

    public int getWeighting() {
        return ((points * 5) + getGoalDifference());
    }

    @Override
    public int compareTo(Team other) {
        if (other.getPoints() != points) {
            return Integer.compare(other.getPoints(), points);
        } else {
            return Integer.compare(other.getGoalDifference(), getGoalDifference());
        }
    }
}
