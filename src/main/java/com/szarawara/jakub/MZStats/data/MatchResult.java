package com.szarawara.jakub.MZStats.data;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.szarawara.jakub.MZStats.utils.XmlUtils.*;

public class MatchResult {

    private String matchUrl;
    private String matchXmlUrl;
    private String checkMatchUrl;

    private static final String TEAM_ID = "420147";
    private static final String SOCCER_STATISTIC = "SoccerStatistics";
    private static final String TEAM = "Team";
    private static final String STATISTICS = "Statistics";

    public MatchResult(String matchId) {
        this.matchUrl = "https://www.managerzone.com/?p=match&sub=result&mid=" + matchId;
        this.matchXmlUrl = "https://www.managerzone.com/matchviewer/getMatchFiles.php?type=stats&mid=" + matchId + "&sport=soccer";
        this.checkMatchUrl = "https://www.managerzone.com/ajax.php?p=matchViewer&sub=check-match&type=2d&sport=soccer&mid=" + matchId;
    }

    public Result getMatch() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        loadMatchResult();
        Document matchXml = getXml(matchXmlUrl);
        Node soccerStatNode = getNode(matchXml.getChildNodes(), SOCCER_STATISTIC);
        if (soccerStatNode == null) {
            return null;
        }
        List<Node> teamNodes = getNodeList(soccerStatNode.getChildNodes(), TEAM);
        Node team1Node = teamNodes.get(0);
        Node team2Node = teamNodes.get(1);
        Node team1Statistics = getNode(team1Node.getChildNodes(), STATISTICS);
        Node team2Statistics = getNode(team2Node.getChildNodes(), STATISTICS);
        if (team1Statistics == null || team2Statistics == null) {
            return null;
        }
        int team1Goals = Integer.parseInt(team1Statistics.getAttributes().getNamedItem("goals").getNodeValue());
        int team2Goals = Integer.parseInt(team2Statistics.getAttributes().getNamedItem("goals").getNodeValue());

        String team1Id = team1Node.getAttributes().getNamedItem("id").getNodeValue();
        String team2Id = team2Node.getAttributes().getNamedItem("id").getNodeValue();

        Result result = new Result(this.matchUrl);
        if (team1Id.equals(TEAM_ID)) {
            result.mentality = team1Node.getAttributes().getNamedItem("playstyle").getNodeValue();
            result.pressing = team1Node.getAttributes().getNamedItem("aggression").getNodeValue();
            result.opponentColour = "czarny";
            result.colour = "żółty";
            result.result = getResult(team1Goals - team2Goals);
            result.opponentId = team2Id;
        } else {
            result.mentality = team2Node.getAttributes().getNamedItem("playstyle").getNodeValue();
            result.pressing = team2Node.getAttributes().getNamedItem("aggression").getNodeValue();
            result.opponentColour = "żółty";
            result.colour = "czarny";
            result.result = getResult(team2Goals - team1Goals);
            result.opponentId = team1Id;
        }
        Result.Team homeTeam = new Result.Team();
        Result.Team awayTeam = new Result.Team();
        homeTeam.name = team1Node.getAttributes().getNamedItem("name").getNodeValue();
        homeTeam.goals = team1Goals;
        homeTeam.shots = team1Statistics.getAttributes().getNamedItem("shotsOnGoal").getNodeValue();
        awayTeam.name = team2Node.getAttributes().getNamedItem("name").getNodeValue();
        awayTeam.goals = team2Goals;
        awayTeam.shots = team2Statistics.getAttributes().getNamedItem("shotsOnGoal").getNodeValue();
        result.homeTeam = homeTeam;
        result.awayTeam = awayTeam;
        return result;
    }

    private String getResult(int result) {
        if (result > 0) {
            return "wygrana";
        } else if (result < 0) {
            return "przegrana";
        } else {
            return "remis";
        }
    }

    private void loadMatchResult() throws IOException, InterruptedException {
        JSONObject jsonObject = openUrl(this.checkMatchUrl);
        if (jsonObject.get("response").equals("ok")) {
            return;
        } else {
            TimeUnit.SECONDS.sleep(1);
            loadMatchResult();
        }
    }
}
