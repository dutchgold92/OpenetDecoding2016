package com.openet.decoding;

/**
 * Created by jay on 27/02/16.
 */
public class Match {
    private static final int WIN_POINTS = 3;
    private static final int DRAW_POINTS = 1;
    private static final int LOSE_POINTS = 1;
    private Team team1;
    private int team1Goals;
    private Team team2;
    private int team2Goals;
    private boolean draw;
    private Team winner;
    private Team loser;

    public Match(final Team team1, final int team1Goals, final Team team2, final int team2Goals) {
        this.team1 = team1;
        this.team1Goals = team1Goals;
        this.team2 = team2;
        this.team2Goals = team2Goals;
        this.compute();
    }

    public boolean isDraw() {
        return draw;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getWinner() {
        return winner;
    }

    public Team getLoser() {
        return loser;
    }

    private void compute() {
        if (team1Goals == team2Goals) {
            draw = true;
            winner = null;
            loser = null;

            team1.addPoints(DRAW_POINTS);
            team2.addPoints(DRAW_POINTS);
        } else {
            draw = false;

            if (team1Goals > team2Goals) {
                winner = team1;
                loser = team2;
            } else {
                winner = team2;
                loser = team1;
            }

            winner.addPoints(WIN_POINTS);
            loser.addPoints(LOSE_POINTS);
        }
    }
}
