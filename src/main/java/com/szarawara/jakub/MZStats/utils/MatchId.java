package com.szarawara.jakub.MZStats.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.szarawara.jakub.MZStats.utils.XmlUtils.*;

public class MatchId {

    private static final String MATCH_LIST = "ManagerZone_MatchList";
    private static final String MATCH = "Match";

    private static final String MATCH_URL = "http://www.managerzone.com/xml/team_matchlist.php?sport_id=1&match_status=1&limit=100&team_id=";

    public static List<String> getMatchIdsFromBody(String data) {
        List<String> matchIds = new ArrayList<>();
        Pattern pattern = Pattern.compile("mid=(.*?)&tid", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            matchIds.add(matcher.group(1));
        }
        return matchIds;
    }

    public static List<String> getMatchIdsFromMZUrl(String matchId) throws ParserConfigurationException, IOException, SAXException {
        String url = MATCH_URL + matchId;
        Document xml = getXml(url);
        Node soccerStatNode = getNode(xml.getChildNodes(), MATCH_LIST);
        if (soccerStatNode == null) {
            return null;
        }
        List<Node> teamNodes = getNodeList(soccerStatNode.getChildNodes(), MATCH);
        return teamNodes.stream().map(node -> node.getAttributes().getNamedItem("id").getNodeValue()).collect(Collectors.toList());
    }
}
