package com.szarawara.jakub.MZStats.excel;

import com.szarawara.jakub.MZStats.data.Result;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataBuilder {

    private Workbook workbook;
    private Sheet sheet;
//    private List<Row> rows = new ArrayList<>();
    private List<byte[]> images = new ArrayList<>();

    private List<Integer> imagesInt = new ArrayList<>();
    private XSSFDrawing drawing;
    private Row row;
    private int iterator = 0;

    public ExcelDataBuilder() {
        this.workbook = new XSSFWorkbook();
        this.sheet = this.workbook.createSheet();
    }

    public void createRow() {
        this.row = sheet.createRow(iterator++);
        this.row.setHeightInPoints(149.4f);
    }

    public void saveImage(String url) throws IOException {
        InputStream inputStream = getImage(url);
        images.add(IOUtils.toByteArray(inputStream));
    }

    private InputStream getImage(String url) throws IOException {
        return new URL(url).openStream();
    }

    public void writeResultFull(Result.Team homeTeam, Result.Team awayTeam) {
        String result = homeTeam.name + " - " + awayTeam.name + " " + homeTeam.goals + ":" + awayTeam.goals
                + " (" + homeTeam.shots + ":" + awayTeam.shots + ")";
        row.createCell(2).setCellValue(result);
    }

    public void writeResult(String result) {
        row.createCell(3).setCellValue(result);
    }

    public void writeColour(String colour) {
        row.createCell(6).setCellValue(colour);
    }

    public void writeOpponentColour(String colour) {
        row.createCell(7).setCellValue(colour);
    }

    public void writeOpponentValue(String value) {
        row.createCell(9).setCellValue(value);
    }

    public void writeMatchLink(String url) {
        row.createCell(10).setCellValue(url);
    }

    public void writeTactic(String mentality, String pressing) {
        row.createCell(11).setCellValue(mentality);
        row.createCell(12).setCellValue(pressing);
    }

    public void addPictures() {
        images.forEach(image -> {
            imagesInt.add(workbook.addPicture(image, Workbook.PICTURE_TYPE_PNG));
        });
        this.drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
    }

    public void drawPictures() {
        for (int i = 0; i < imagesInt.size(); i++) {
            XSSFClientAnchor clientAnchor = new XSSFClientAnchor();
            clientAnchor.setCol1(0);
            clientAnchor.setCol2(1);
            clientAnchor.setRow1(i);
            clientAnchor.setRow2(i + 1);
            drawing.createPicture(clientAnchor, imagesInt.get(i));
        }
    }

    public void autoSizeFirstColumn() {
        sheet.setColumnWidth(0, 5149);
    }

    public Workbook getWorkbook() {
        return workbook;
    }
}
