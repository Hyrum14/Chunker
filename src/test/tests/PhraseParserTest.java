package test.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.helpers.PhraseParserTestFramework;
import utils.objects.FunctionPhrase;

import java.util.*;

public class PhraseParserTest extends PhraseParserTestFramework
{
    public final static String VALID_DELIM = " ";
    public final static String INVALID_DELIM = ".";
    List<String> input;
    List<String> delimiters;
    Map<FunctionPhrase, Set<Integer>> expectedPhrases = new HashMap<>();

    @BeforeEach
    void setup()
    {
        input = Collections.emptyList();
        delimiters = Collections.emptyList();
        expectedPhrases.clear();
    }

    @Test
    public void phraseLenOne()
    {
        delimiters = List.of(VALID_DELIM);
        input = List.of("yellow");
        expectedPhrases = Map.of(
                YELLOW, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseLenOneWithBadDelimiters()
    {
        // this test checks to see that the bad delimiter does not fail a length 1 phrase
        delimiters = List.of(INVALID_DELIM);
        input = List.of("yellow");
        expectedPhrases = Map.of(
                YELLOW, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseLenOneInLongSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("the", "yellow", "submarine");
        expectedPhrases = Map.of(
                YELLOW, Set.of(1)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseLenOneInLongSentenceWithBadDelimiters()
    {
        delimiters = List.of(INVALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("the", "yellow", "submarine");
        expectedPhrases = Map.of(
                YELLOW, Set.of(1)
        );

        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, INVALID_DELIM, VALID_DELIM);
        input = List.of("the", "yellow", "submarine");
        expectedPhrases = Map.of(
                YELLOW, Set.of(1)
        );

        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, INVALID_DELIM);
        input = List.of("the", "yellow", "submarine");
        expectedPhrases = Map.of(
                YELLOW, Set.of(1)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    void phraseLenTwoInterrupted()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(INVALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo");
        expectedPhrases = Map.of(
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, INVALID_DELIM);
        input = List.of("alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseLenTwo()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo", "alpha");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0),
                BRAVO_ALPHA, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo", "alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0, 2),
                BRAVO_ALPHA, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseLenTwoWithBadDelimiters()
    {
        delimiters = List.of(INVALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo", "alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(2),
                BRAVO_ALPHA, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, INVALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo", "alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0, 2)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, INVALID_DELIM, VALID_DELIM);
        input = List.of("alpha", "bravo", "alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0),
                BRAVO_ALPHA, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, INVALID_DELIM);
        input = List.of("alpha", "bravo", "alpha", "bravo");
        expectedPhrases = Map.of(
                ALPHA_BRAVO, Set.of(0, 2),
                BRAVO_ALPHA, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);
    }


    @Test
    public void lenThreeOverlapping()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0)
        );
        testParsing(input, delimiters, expectedPhrases);


        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0),
                TWO_THREE_ONE, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0),
                TWO_THREE_ONE, Set.of(1),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);


        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0, 3),
                TWO_THREE_ONE, Set.of(1),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void lenThreeOverlappingWithBadDelimiters()
    {
        delimiters = List.of(INVALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(3),
                TWO_THREE_ONE, Set.of(1),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, INVALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(3),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, INVALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0, 3)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, INVALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0),
                TWO_THREE_ONE, Set.of(1)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, INVALID_DELIM, VALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0),
                TWO_THREE_ONE, Set.of(1),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);

        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, INVALID_DELIM);
        input = List.of("one", "two", "three", "one", "two", "three");
        expectedPhrases = Map.of(
                ONE_TWO_THREE, Set.of(0, 3),
                TWO_THREE_ONE, Set.of(1),
                THREE_ONE_TWO, Set.of(2)
        );
        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void multipleSamePhraseInSentence()
    {
        delimiters = List.of(VALID_DELIM, INVALID_DELIM, VALID_DELIM);
        input = List.of("green", "green", "green");
        expectedPhrases = Map.of(
                GREEN, Set.of(0, 1, 2)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void multipleDifferentPhraseInSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("as", "black", "as", "a", "crow", "as", "white", "as", "a", "cloud");
        expectedPhrases = Map.of(
                CROW, Set.of(0),
                CLOUD, Set.of(5)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void overlappingPhrasesInSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("as", "colorful", "as", "a", "rainbow", "blue");
        expectedPhrases = Map.of(
                RAINBOW, Set.of(0),
                BLUE, Set.of(5)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void noPhraseAtAll()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("elephant", "apple", "car");
        expectedPhrases = Map.of();

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseAtStartAndEnd()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("red", "and", "blue");
        expectedPhrases = Map.of(
                RED, Set.of(0),
                BLUE, Set.of(2)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseInMiddle()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("i", "like", "blue", "cars");
        expectedPhrases = Map.of(
                BLUE, Set.of(2)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void samePhraseRepeated()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("green", "green", "green", "green");
        expectedPhrases = Map.of(
                GREEN, Set.of(0, 1, 2, 3)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void multiplePhrasesRepeated()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("green", "blue", "green", "blue");
        expectedPhrases = Map.of(
                GREEN, Set.of(0, 2),
                BLUE, Set.of(1, 3)
        );

        testParsing(input, delimiters, expectedPhrases);
    }


    @Test
    public void multiplePhrasesInLongSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("the", "green", "parrot", "is", "as", "dead", "as", "a", "parrot");
        expectedPhrases = Map.of(
                GREEN, Set.of(1),
                PARROT, Set.of(4)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseAtEndOfSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("i", "see", "a", "red", "apple");
        expectedPhrases = Map.of(
                RED, Set.of(3)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void phraseInComplexSentence()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("the", "green", "apple", "is", "as", "dead", "as", "a", "parrot");
        expectedPhrases = Map.of(
                GREEN, Set.of(1),
                PARROT, Set.of(4)
        );

        testParsing(input, delimiters, expectedPhrases);
    }


    @Test
    public void overlappingPhrases()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("red", "yellow", "blue", "green");
        expectedPhrases = Map.of(
                RED, Set.of(0),
                YELLOW, Set.of(1),
                BLUE, Set.of(2),
                GREEN, Set.of(3)
        );

        testParsing(input, delimiters, expectedPhrases);
    }

    @Test
    public void samePhraseDifferentPositions()
    {
        delimiters = List.of(VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM, VALID_DELIM);
        input = List.of("blue", "sky", "blue", "ocean", "blue", "jeans");
        expectedPhrases = Map.of(
                BLUE, Set.of(0, 2, 4)
        );

        testParsing(input, delimiters, expectedPhrases);
    }
    

    @AfterEach
    void tearDown()
    {
    }
}

