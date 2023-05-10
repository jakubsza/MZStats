package com.szarawara.jakub.MZStats.data;

import com.szarawara.jakub.MZStats.WalkoverException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.szarawara.jakub.MZStats.utils.XmlUtils.*;
import static com.szarawara.jakub.MZStats.utils.XmlUtils.getNode;

public class Shots {

    private String matchUrl;
    private String checkMatchUrl;

    private String playerId;

    private static final String GOALS = "goals";
    private static final String SHOTS_WIDE = "shotsWide";
    private static final String SHOTS_ON_GOAL = "shotsOnGoal";
    private static final String SOCCER_STATISTIC = "SoccerStatistics";
    private static final String PLAYER = "Player";
    private static final String STATISTICS = "Statistics";

    public Shots(String matchId, String playerId) {
        this.matchUrl = "https://www.managerzone.com/matchviewer/getMatchFiles.php?type=stats&mid=" + matchId + "&sport=soccer";
        this.checkMatchUrl = "https://www.managerzone.com/ajax.php?p=matchViewer&sub=check-match&type=2d&sport=soccer&mid=" + matchId;
        this.playerId = playerId;
    }

    public PlayerStatistics getPlayerStats() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        try {
            loadMatchResult();
        } catch (WalkoverException e) {
            return null;
        }
        Document matchXml = getXml(matchUrl);
        Node soccerStatNode = getNode(matchXml.getChildNodes(), SOCCER_STATISTIC);
        if (soccerStatNode == null) {
            return null;
        }
        Node playerNode = getNodeById(soccerStatNode.getChildNodes(), PLAYER, this.playerId);
        if (playerNode == null) {
            return null;
        }
        Node playerStatsNode = getNode(playerNode.getChildNodes(), STATISTICS);
        if (playerStatsNode == null) {
            return null;
        }
        PlayerStatistics playerStatistics = new PlayerStatistics();
        playerStatistics.goals = Integer.parseInt(playerStatsNode.getAttributes().getNamedItem(GOALS).getNodeValue());
        int shotsOnGoal = Integer.parseInt(playerStatsNode.getAttributes().getNamedItem(SHOTS_ON_GOAL).getNodeValue());
        int shotsWide = Integer.parseInt(playerStatsNode.getAttributes().getNamedItem(SHOTS_WIDE).getNodeValue());
        playerStatistics.totalShots = shotsWide + shotsOnGoal;
        return playerStatistics;
    }

    private void loadMatchResult() throws IOException, InterruptedException, WalkoverException {
        JSONObject jsonObject = openUrl(this.checkMatchUrl);
        if (jsonObject.get("response").equals("ok")) {
            return;
        } else if (jsonObject.get("response").equals("walkover")) {
            throw new WalkoverException("Walkover for " + this.matchUrl);
        } else {
            TimeUnit.SECONDS.sleep(1);
            loadMatchResult();
        }
    }

}
