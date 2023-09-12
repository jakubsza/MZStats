package com.szarawara.jakub.MZStats.rest;

import com.szarawara.jakub.MZStats.utils.MatchId;
import com.szarawara.jakub.MZStats.data.PlayerStatistics;
import com.szarawara.jakub.MZStats.calculation.Shots;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TacklingRate {

    @PostMapping(path = "/tacklingRate/{id}")
    public double getTacklingRate(@RequestBody String body, @PathVariable String id, @RequestParam(required = false) String teamId) throws ParserConfigurationException, IOException, InterruptedException, SAXException {
        List<String> matchIds = new ArrayList<>();
        if (teamId != null) {
            matchIds = MatchId.getMatchIdsFromMZUrl(teamId);
        } else {
            matchIds = MatchId.getMatchIdsFromBody(body);
        }
        int goals = 0;
        int totalShots = 0;
        for (String matchId : matchIds) {
            Shots shots = new Shots(matchId, id);
            PlayerStatistics playerStatistics = shots.getPlayerStats();
            if (playerStatistics != null) {
                goals += playerStatistics.goals;
                totalShots += playerStatistics.totalShots;
            }
        }
        return (double) goals / (double) totalShots;
    }
}
