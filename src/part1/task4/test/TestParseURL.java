package part1.task4.test;

import part1.task4.SimplePageParser;

import java.io.IOException;

public class TestParseURL {

    private static final String TEST_LINK = "https://www.oracle.com/java/index.html";

    public static void main(String[] args) throws IOException {
        SimplePageParser pageParser = new SimplePageParser(TEST_LINK);
        pageParser.parsePage();

    }
}

