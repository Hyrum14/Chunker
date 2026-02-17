package test.helpers;

import app.model.parsing.DocumentParser;
import app.model.parsing.DocumentParsingException;
import app.model.parsing.RootConverter;
import app.model.parsing.StringDocumentProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentParserTestFramework
{

    protected DocumentParser parser;
    protected TestDocumentStorage storage;

    private void beforeAll()
    {
        parser = new DocumentParser();
        storage = new TestDocumentStorage();
    }

    protected void testParsing(String input, List<String> expectedWords, List<String> expectedDelimiters)
    {
        try
        {
            beforeAll();
            parser.parse(new StringDocumentProvider(input), storage);
            assertEquals(expectedWords, storage.words);
            assertEquals(expectedDelimiters, storage.delimiters);
        } catch (DocumentParsingException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    protected void testParsingException(String input)
    {
        beforeAll();
        assertThrows(DocumentParsingException.class, () -> parser.parse(new StringDocumentProvider(input), storage));
    }

    public static class TestDocumentStorage implements DocumentParser.DocumentStorage
    {
        List<String> words = new ArrayList<>();
        List<String> delimiters = new ArrayList<>();

        @Override
        public void storeOccurrence(String contents)
        {
            System.out.printf("Occurrence %d: [%s] => [%s]%n", words.size(), contents, RootConverter.convert(contents));

            words.add(RootConverter.convert(contents));
        }

        @Override
        public void storeDelimiter(String contents)
        {
            System.out.printf("Delimiter %d: [%s]%n", delimiters.size(), contents);
            delimiters.add(contents);
        }
    }
}
