package com.szarawara.jakub.MZStats.excel;

import com.szarawara.jakub.MZStats.data.MatchResult;
import com.szarawara.jakub.MZStats.data.Result;
import com.szarawara.jakub.MZStats.data.Value;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

    private ExcelDataBuilder excelDataBuilder;

    public void generateExcel(List<String> matchesId) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        this.excelDataBuilder = new ExcelDataBuilder();
        for (String matchId : matchesId) {
            fillData(matchId);
        }
        excelDataBuilder.addPictures();
        excelDataBuilder.drawPictures();
        excelDataBuilder.autoSizeFirstColumn();
        saveExcel();
    }

    private void fillData(String matchId) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        excelDataBuilder.createRow();
        String url = "https://www.managerzone.com/dynimg/pitch.php?match_id=" + matchId;
        excelDataBuilder.saveImage(url);
        MatchResult matchResult = new MatchResult(matchId);
        Result result = matchResult.getMatch();
        Value value = new Value(result.opponentId);
        String opponentValue = value.getTeamValue();
        excelDataBuilder.writeResultFull(result.homeTeam, result.awayTeam);
        excelDataBuilder.writeResult(result.result);
        excelDataBuilder.writeOpponentValue(opponentValue);
        excelDataBuilder.writeColour(result.colour);
        excelDataBuilder.writeOpponentColour(result.opponentColour);
        excelDataBuilder.writeMatchLink(result.matchUrl);
        excelDataBuilder.writeTactic(result.mentality, result.pressing);
    }

    private void saveExcel() throws IOException {
        try (FileOutputStream saveExcel = new FileOutputStream("target/test.xlsx")) {
            excelDataBuilder.getWorkbook().write(saveExcel);
        }
    }
}
