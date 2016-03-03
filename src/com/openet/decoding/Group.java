package com.openet.decoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jay on 27/02/16.
 */
public class Group {
    private String name;
    private List<Team> teams;

    public Group(final String name) {
        this.name = name;
        this.teams = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean has(Team team) {
        return teams.contains(team);
    }

    public int size() {
        return teams.size();
    }

    public void sort() {
        Collections.sort(teams);
    }
}
