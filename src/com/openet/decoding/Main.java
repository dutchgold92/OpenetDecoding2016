package com.openet.decoding;

public class Main {
    private static final String INPUT_FILE_PATH = "/home/jay/workspace/Decoding2016/resources/input.csv";

    public static void main(String[] args) {
        LeagueCalculator lc = new LeagueCalculator(INPUT_FILE_PATH, ",");
        //lc.printGroups();
        //lc.printTeams();
        //lc.printKnockOutGroups();
        //LeagueCalculator.compareTeamsWinProbability(6, 8, 5, 10, 10000);
        lc.printLeagueFinals();
    }
}