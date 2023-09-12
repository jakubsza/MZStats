package com.szarawara.jakub.MZStats.rest;

import com.szarawara.jakub.MZStats.utils.MatchId;
import com.szarawara.jakub.MZStats.excel.ExcelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
public class StatsCollector {

    @Autowired
    private ExcelGenerator excelGenerator;

    @PostMapping(path = "/stats")
    public void getStats(@RequestBody String body) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        excelGenerator.generateExcel(MatchId.getMatchIdsFromBody(body));
    }
}
