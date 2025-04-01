package utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class HtmlUtils {

    /**
     * Extracts text content from nodes that match the given XPath expression.
     *
     * @param htmlContent     The HTML content as a string.
     * @param xpathExpression The XPath expression to select nodes.
     * @return A list of text contents for each matched node.
     */
    public static List<String> extractTextByXpath(String htmlContent, String xpathExpression) {
        List<String> result = new ArrayList<>();
        try {
            String xhtml = convertHtmlToXhtml(htmlContent);
            org.w3c.dom.Document w3cDoc = createW3cDocumentFromXhtml(xhtml);
            result = evaluateXpathAndExtractText(w3cDoc, xpathExpression);
        } catch (Exception e) {
            log.error("Error extracting text by XPath: {}", e.getMessage());
        }

        return result;
    }

    /**
     * Converts the given HTML content into a well-formed XHTML string using Jsoup.
     *
     * @param htmlContent The HTML content as a string.
     * @return A well-formed XHTML representation of the given HTML.
     */
    private static String convertHtmlToXhtml(String htmlContent) {
        Document jsoupDoc = Jsoup.parse(htmlContent);
        jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return jsoupDoc.outerHtml();
    }

    /**
     * Creates a W3C Document from the given XHTML string.
     *
     * @param html A well-formed XHTML string.
     * @return A W3C Document parsed from the given XHTML content.
     * @throws Exception If an error occurs while parsing the XHTML.
     */
    private static org.w3c.dom.Document createW3cDocumentFromXhtml(String html) throws Exception {
        String fixedHtml = html.replace("&nbsp;", "\u00A0");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(fixedHtml)));
    }

    /**
     * Evaluates the specified XPath expression on the given W3C Document and extracts text content.
     *
     * @param w3cDoc          The W3C Document to be queried.
     * @param xpathExpression The XPath expression used to select nodes.
     * @return A list of trimmed text contents from the nodes that match the XPath.
     * @throws Exception If an error occurs during XPath evaluation.
     */
    private static List<String> evaluateXpathAndExtractText(org.w3c.dom.Document w3cDoc, String xpathExpression) throws Exception {
        List<String> result = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(xpathExpression);

        NodeList nodeList = (NodeList) expr.evaluate(w3cDoc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            result.add(node.getTextContent().trim());
        }
        return result;
    }

    /**
     * Extracts text content from the first <div> element with the specified class name.
     *
     * @param htmlContent the HTML content as a string
     * @param className   the class name of the <div> element to extract text from
     * @return the extracted text content, or null if the element is not found
     */
    public static List<String> extractTextByClass(String htmlContent, String className) {
        List<String> textContents = new ArrayList<>();
        try {
            Document document = Jsoup.parse(htmlContent);
            Elements elements = document.getElementsByClass(className);
            elements.forEach(element -> textContents.add(element.text()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return textContents;
    }

    /**
     * Extracts all text contents of elements with the given className, but only inside a element with a specific id.
     *
     * @param htmlContent the entire HTML content
     * @param className   the class to look for
     * @param divId       the id of the that contains the elements
     * @return a list of extracted text contents
     */
    public static List<String> extractTextByClassAndId(String htmlContent, String className, String divId) {
        try {
            Document document = Jsoup.parse(htmlContent);
            Element container = document.getElementById(divId);

            if (container != null) {
                return extractTextByClass(container.outerHtml(), className);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }
}


