package com.szarawara.jakub.MZStats.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;

public class Value {

    private final static String MAIN_CONTENT_TABLE = "MainContent_Table0";

    private String url;
    public Value(String teamId) {
        this.url = "https://mzspy.com.pl/SpyResults.aspx?t=teamId&count=1&team1=" + teamId;
    }

    public String getTeamValue() {
        Document doc = null;
        try {
            doc = Jsoup.connect(this.url).get();
        } catch (IOException e) {
            return "MZspy does not get opponent value";
        }
        Element table = doc.getElementById(MAIN_CONTENT_TABLE);
        Elements tds = table.getElementsByTag("td");
        Optional<String> value = tds.stream().map(Element::ownText).filter(val -> val.contains("EUR")).findFirst();
        if (value.isEmpty()) {
            return "";
        }
        double valueDouble = Double.parseDouble(value.get().replace("EUR", "").replace(" ", ""));
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(valueDouble/1000000);
    }
}
