package com.szarawara.jakub.MZStats.data;

public class Result {

    public String result;
    public String colour;
    public String opponentColour;
    public String mentality;
    public String pressing;
    public String opponentMentality;
    public String opponentPressing;
    public String matchUrl;
    public String opponentId;

    public Team homeTeam;
    public Team awayTeam;

    public Result(String matchUrl) {
        this.matchUrl = matchUrl;
    }

    public static class Team {
        public String name;
        public int goals;
        public String shots;
    }
}
