package part2.test.java.task1.util;

import org.junit.Test;
import part2.main.java.task1.util.XMLValidator;

import static org.junit.Assert.*;

public class XMLValidatorTest {

    private static final String inputXML = "resources/inputForm.xml";
//    private static final String brokenXML = "resources/testBrokenInput.xml";
    private static final String schema = "resources/inputForm.xsd";

    @Test
    public void testValidateXMLSchema() throws Exception {
        boolean isValidInputXML = XMLValidator.validateXMLSchema(schema, inputXML);
//        boolean isValidBrokenXML = XMLValidator.validateXMLSchema(schema, brokenXML);

        assertEquals(true, isValidInputXML);
//        assertEquals(false, isValidBrokenXML);
    }
}