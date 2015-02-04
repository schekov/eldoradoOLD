package part2.test.java.task1.util;

import org.junit.Test;
import part2.main.java.task1.model.Customer;
import part2.main.java.task1.util.XMLParser;

public class XMLParserTest {

    @Test
    public void testParseXML() throws Exception {

        XMLParser xmlParser = new XMLParser();
        XMLParser.SAXHandler saxHandler;
        saxHandler =  xmlParser.parseXML();

        for (Customer cust : saxHandler.getCustomers()) {
            System.out.println(cust.toString());
        }
    }
}