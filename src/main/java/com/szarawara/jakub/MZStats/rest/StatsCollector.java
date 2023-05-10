package com.szarawara.jakub.MZStats.rest;

import com.szarawara.jakub.MZStats.data.MatchId;
import com.szarawara.jakub.MZStats.excel.ExcelGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
public class StatsCollector {

    @PostMapping(path = "/stats")
    public void getStats(@RequestBody String body) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        ExcelGenerator excelGenerator = new ExcelGenerator();
        excelGenerator.generateExcel(MatchId.getMatchIdsFromBody(body));
    }
}
