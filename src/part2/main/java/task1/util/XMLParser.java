package part2.main.java.task1.util;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import part2.main.java.task1.model.Customer;
import part2.main.java.task1.model.Order;
import part2.main.java.task1.model.Position;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private static final String XSD_PATH = "resources/inputForm.xsd";
    private static final String XML_PATH = "resources/inputForm.xml";

    public SAXHandler parseXML() throws IOException, SAXException, ParserConfigurationException {
        return parseXML(XSD_PATH,XML_PATH);
    }

    public SAXHandler parseXML(String xsdPath,String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        if(XMLValidator.validateXMLSchema(xsdPath, xmlPath)){
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxParserFactory.newSAXParser();
        SAXHandler handler = new SAXHandler();
        parser.parse(new File(xmlPath),
                handler);
        return handler;
        }
        return null;
    }

    public static class SAXHandler extends DefaultHandler {

        private List<Customer> customers;
        private List<Order> orders;
        private List<Position> positions;
        private Customer cust = null;
        private Order order = null;
        private Position position = null;
        private String content = null;
        private boolean isOrder;
        private boolean isPosition;

        public List<Customer> getCustomers() {
            return customers;
        }

        public List<Order> getOrders() {
            return orders;
        }

        public List<Position> getPositions() {
            return positions;
        }

        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            switch (qName) {
                case "customers":
                    customers = new ArrayList<>();
                    break;
                case "customer":
                    cust = new Customer();
                    break;
                case "orders":
                    orders = new ArrayList<>();
                    break;
                case "order":
                    isOrder = true;
                    order = new Order();
                    break;
                case "positions":
                    positions = new ArrayList<>();
                    break;
                case "position":
                    isPosition = true;
                    position = new Position();
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName,
                               String qName) throws SAXException {
            switch (qName) {
                case "customer":
                    customers.add(cust);
                    break;
                case "id":
                    if (isPosition) {
                        position.id = Long.valueOf(content);

                    } else if (isOrder) {
                        order.id = Long.valueOf(content);
                    } else {
                        cust.id = Long.valueOf(content);
                    }
                    break;
                case "name":
                    cust.name = content;
                    break;
                case "order":
                    orders.add(order);
                    isOrder=false;
                    break;
                case "orders":
                    cust.orders = orders;
                    break;
                case "position":
                    positions.add(position);
                    isPosition = false;
                    break;
                case "positions":
                    order.positions = positions;
                    break;
                case "price":
                    position.setPrice(new BigDecimal(content));
                    break;
                case "count":
                    position.count = Long.valueOf(content);
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            content = String.copyValueOf(ch, start, length).trim();
        }
    }
}
