package test.helpers;

import app.model.parsing.PhraseParser;
import utils.logging.PrintStreamLogger;
import utils.objects.Delimiter;
import utils.objects.FunctionPhrase;

import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhraseParserTestFramework
{

    public static FunctionPhrase BIRDS = new FunctionPhrase(null, "birds of a feather flock together");
    public static FunctionPhrase GREEN = new FunctionPhrase("", "green");
    public static FunctionPhrase BLUE = new FunctionPhrase("", "blue");
    public static FunctionPhrase YELLOW = new FunctionPhrase("", "yellow");
    public static FunctionPhrase RED = new FunctionPhrase("", "red");
    public static FunctionPhrase PARROT = new FunctionPhrase(null, "as dead as a parrot");
    public static FunctionPhrase CROW = new FunctionPhrase(null, "as black as a crow");
    public static FunctionPhrase CLOUD = new FunctionPhrase(null, "as white as a cloud");
    public static FunctionPhrase RAINBOW = new FunctionPhrase(null, "as colorful as a rainbow");

    public static FunctionPhrase ALPHA_BRAVO = new FunctionPhrase(null, "alpha bravo");
    public static FunctionPhrase BRAVO_ALPHA = new FunctionPhrase(null, "bravo alpha");

    public static FunctionPhrase ONE_TWO_THREE = new FunctionPhrase(null, "one two three");
    public static FunctionPhrase TWO_THREE_ONE = new FunctionPhrase(null, "two three one");
    public static FunctionPhrase THREE_ONE_TWO = new FunctionPhrase(null, "three one two");
    public static Set<FunctionPhrase> possiblePhrases = Set.of(BIRDS, GREEN, BLUE, YELLOW, RED, PARROT, CROW, CLOUD, RAINBOW, ALPHA_BRAVO, BRAVO_ALPHA, ONE_TWO_THREE, TWO_THREE_ONE, THREE_ONE_TWO);


    protected PhraseParser parser;
    protected TestPhraseStorage storage;

    private void beforeAll(Set<FunctionPhrase> possiblePhrases)
    {
        storage = new TestPhraseStorage();
        parser = new PhraseParser(possiblePhrases, storage, new PrintStreamLogger()
        {
            @Override
            public PrintStream getStream()
            {
                return System.out;
            }
        });
    }

    protected void testParsing(List<String> inputWords, List<String> delimiters, Map<FunctionPhrase, Set<Integer>> expectedPhrases)
    {
        beforeAll(possiblePhrases);
        assertEquals(inputWords.size(), delimiters.size(), "The test was not written correctly, the delimiters must exactly match the length of the input words");
        for (int i = 0; i < inputWords.size(); ++i)
        {
            parser.parseNextWord(inputWords.get(i), new Delimiter(0, 0, delimiters.get(i)));
        }
        assertEquals(expectedPhrases, storage.storedPhrases);


    }

    public static class TestPhraseStorage implements PhraseParser.PhraseStorage
    {
        Map<FunctionPhrase, Set<Integer>> storedPhrases = new HashMap<>();

        @Override
        public void storePhrase(FunctionPhrase phrase, int index)
        {
            Set<Integer> toAdd = storedPhrases.getOrDefault(phrase, new HashSet<>());
            assertTrue(toAdd.add(index), "Same phrase was added twice at the same index");
            storedPhrases.put(phrase, toAdd);
        }

        public void clear()
        {
            storedPhrases = new HashMap<>();
        }
    }
}

