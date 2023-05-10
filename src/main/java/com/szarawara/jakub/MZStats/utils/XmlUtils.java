package com.szarawara.jakub.MZStats.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

    public static JSONObject openUrl(String url) throws IOException {
        String json = IOUtils.toString(URI.create(url), StandardCharsets.UTF_8);
        return new JSONObject(json);
    }

    public static Document getXml(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }

    public static Node getNode(NodeList nodeList, String nodeName) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName)) {
                return nodeList.item(i);
            }
        }
        return null;
    }

    public static Node getNodeById(NodeList nodeList, String nodeName, String id) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName) && nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(id)) {
                return nodeList.item(i);
            }
        }
        return null;
    }

    public static List<Node> getNodeList(NodeList nodeList, String nodeName) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(nodeName)) {
                nodes.add(nodeList.item(i));
            }
        }
        return nodes;
    }
}
