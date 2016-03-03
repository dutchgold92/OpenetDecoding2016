package com.openet.decoding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jay on 27/02/16.
 */
public class LeagueCalculator {
    private static final int GROUP_SIZE = 4;
    private String delimiter;
    private Map<String, Group> groups;
    private Map<String, Team> teams;
    private List<Match> matches;
    private Group knockOutPhaseGroup;

    public LeagueCalculator(final String inputFilePath, String delimiter) {
        this.delimiter = delimiter;
        this.groups = new HashMap<>();
        this.teams = new HashMap<>();
        this.matches = new ArrayList<>();
        this.knockOutPhaseGroup = new Group("KnockOutPhase");
        populateConstants();
        parseInput(inputFilePath);
        computeGroups();

        assert(teams.size() == 24);
        assert(groups.size() == 6);

        for (Group g : groups.values()) {
            assert(g.getTeams().size() == GROUP_SIZE);
        }

        computeKnockOutPhaseTeams();
    }

    private void populateConstants() {
        Group groupA = new Group("A");
        Group groupB = new Group("B");
        Group groupC = new Group("C");
        Group groupD = new Group("D");
        Group groupE = new Group("E");
        Group groupF = new Group("F");

        Team france = new Team("France");
        Team ireland = new Team("Republic of Ireland");
        Team ukraine = new Team("Ukraine");
        Team albania = new Team("Albania");
        Team hungary = new Team("Hungary");
        Team iceland = new Team("Iceland");

        groupA.addTeam(france);
        groupB.addTeam(ireland);
        groupC.addTeam(ukraine);
        groupD.addTeam(albania);
        groupE.addTeam(hungary);
        groupF.addTeam(iceland);

        this.teams.put(france.getName(), france);
        this.teams.put(ireland.getName(), ireland);
        this.teams.put(ukraine.getName(), ukraine);
        this.teams.put(albania.getName(), albania);
        this.teams.put(hungary.getName(), hungary);
        this.teams.put(iceland.getName(), iceland);

        this.groups.put(groupA.getName(), groupA);
        this.groups.put(groupB.getName(), groupB);
        this.groups.put(groupC.getName(), groupC);
        this.groups.put(groupD.getName(), groupD);
        this.groups.put(groupE.getName(), groupE);
        this.groups.put(groupF.getName(), groupF);
    }

    private void parseInput(final String inputFilePath) {
        try {
            Files.lines(Paths.get(inputFilePath)).forEach(this::parseMatchLine);
        } catch (IOException ex) {
            throw new RuntimeException("File parsing failed: " + ex.getMessage());
        }
    }

    private void parseMatchLine(String line) {
        String[] tokens = line.split(delimiter);
        assert(tokens.length == 4);

        final String team1Name = tokens[0];
        final String team2Name = tokens[3];
        final int team1Goals = Integer.parseInt(tokens[1]);
        final int team2Goals = Integer.parseInt(tokens[2]);

        if (!teams.containsKey(team1Name)) {
            Team newTeam = new Team(team1Name);
            teams.put(newTeam.getName(), newTeam);
        }

        if (!teams.containsKey(team2Name)) {
            Team newTeam = new Team(team2Name);
            teams.put(newTeam.getName(), newTeam);
        }

        Team team1 = teams.get(team1Name);
        Team team2 = teams.get(team2Name);

        assert(team1 != null);
        assert(team2 != null);

        team1.addGoalsScored(team1Goals);
        team1.addGoalsConceded(team2Goals);
        team2.addGoalsScored(team2Goals);
        team2.addGoalsConceded(team1Goals);

        matches.add(new Match(team1, team1Goals, team2, team2Goals));
    }

    private void computeGroups() {
        for (Group g : this.groups.values()) {
            for (Match m : this.matches) {
                Team t1 = m.getTeam1();
                Team t2 = m.getTeam2();

                if (g.has(t1) && !g.has(t2)) {
                    g.addTeam(t2);
                } else if (g.has(t2) && !g.has(t1)) {
                    g.addTeam(t1);
                }

                if (g.size() == GROUP_SIZE) {
                    break;
                }
            }

            g.sort();
        }
    }

    public void computeKnockOutPhaseTeams() {
        Group thirdPlaceTeamsGroup = new Group("ThirdPlaceTeamsGroup");

        for (Group g : groups.values()) {
            List<Team> groupTeams = g.getTeams();
            knockOutPhaseGroup.addTeam(groupTeams.get(0));
            knockOutPhaseGroup.addTeam(groupTeams.get(1));
            thirdPlaceTeamsGroup.addTeam(groupTeams.get(2));
        }

        for (int i = 0; i < 4; i++) {
            knockOutPhaseGroup.addTeam(thirdPlaceTeamsGroup.getTeams().get(i));
        }

        knockOutPhaseGroup.sort();
    }

    public static void compareTeamsWinProbability(final int team1Points, final int team1GoalDiff, final int team2Points, final int team2GoalDiff, final int iterations) {
        Team t1 = new Team("Team 1");
        Team t2 = new Team("Team 2");
        t1.addPoints(team1Points);
        t2.addPoints(team2Points);
        t1.setGoalDifference(team1GoalDiff);
        t2.setGoalDifference(team2GoalDiff);

        int team1Wins = 0;
        int team2Wins = 0;
        final double totalMatchWeighting = (t1.getWeighting() + t2.getWeighting());
        final double team1WinProbability = (((double) t1.getWeighting()) / totalMatchWeighting);
        final double team2WinProbability = (1.0 - team1WinProbability);

        for (int i = 0; i < iterations; i++) {
            final double rand = new java.util.Random().nextDouble();

            if (rand <= team1WinProbability) {
                team1Wins++;
            } else {
                team2Wins++;
            }
        }

        println("Team\t|\tWin Probability\t\t|\tWins");
        println("Team 1\t|\t" + team1WinProbability + "\t|\t" + team1Wins);
        println("Team 2\t|\t" + team2WinProbability + "\t|\t" + team2Wins);
    }

    private Team computeSimulatedMatchWinner(final Team t1, final Team t2, final int iterations) {
        int team1Wins = 0;
        int team2Wins = 0;
        final double totalMatchWeighting = (t1.getWeighting() + t2.getWeighting());
        final double team1WinProbability = (((double) t1.getWeighting()) / totalMatchWeighting);
        final double team2WinProbability = (1.0 - team1WinProbability);

        for (int i = 0; i < iterations; i++) {
            final double rand = new Random(System.nanoTime()).nextDouble();

            if (rand <= team1WinProbability) {
                team1Wins++;
            } else {
                team2Wins++;
            }
        }

        if (team1Wins != team2Wins) {
            return (team1Wins > team2Wins) ? t1 : t2;
        } else {
            return (t1.getGoalDifference() > t2.getGoalDifference()) ? t1 : t2;
        }
    }

    public void printLeagueFinals() {
        computeLeagueFinals(knockOutPhaseGroup.getTeams(), 1);
    }

    private void computeLeagueFinals(List<Team> teams, int round) {
        if (teams.size() == 1) {
            println("WINNER: " + teams.get(0).getName());
        } else {
            List<Integer> randoms = new ArrayList<>();

            while (randoms.size() < teams.size()) {
                int rand = new Random(System.nanoTime()).nextInt(teams.size());

                if (!randoms.contains(rand)) {
                    randoms.add(rand);
                }
            }

            println("\nRound " + round);
            println("##################");

            List<Team> winners = new ArrayList<>();

            for (int i = 0; i < teams.size(); i += 2) {
                Team t1 = teams.get(randoms.get(0));
                randoms.remove(0);
                Team t2 = teams.get(randoms.get(0));
                randoms.remove(0);

                Team winner = computeSimulatedMatchWinner(t1, t2, 10000);
                winners.add(winner);

                println(t1.getName() + " vs " + t2.getName() + " -> " + winner.getName() + " wins");
            }

            computeLeagueFinals(winners, round + 1);
        }
    }

    public void printTeams() {
        println("\nTeams:\n");

        for (Team t : this.teams.values()) {
            println(t.getName());
        }
    }

    public void printGroups() {
        println("\nGroups:");

        for (Group g : this.groups.values()) {
            println("\nGroup " + g.getName() + "\t|\tPoints\t|\tGoal Difference");
            println("##################");

            for(Team t : g.getTeams()) {
                println(t.getName().substring(0, (t.getName().length() > 7 ? 7 : t.getName().length())) + "\t|\t" + t.getPoints() + "\t|\t" + t.getGoalDifference());
            }
        }
    }

    public void printKnockOutGroups() {
        println("Knock-Out Phase Group");
        println("Team\t|\tPoints\t|\tGoal Difference");
        println("##################");

        for (Team t : knockOutPhaseGroup.getTeams()) {
            println(t.getName().substring(0, (t.getName().length() > 7 ? 7 : t.getName().length())) + "\t|\t" + t.getPoints() + "\t\t|\t" + t.getGoalDifference());
        }
    }

    private static void println(String message) {
        System.out.println(message);
    }
}