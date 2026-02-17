package test.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.helpers.DocumentParserTestFramework;

import java.util.Collections;
import java.util.List;

class DocumentParserTest extends DocumentParserTestFramework
{
    String input;
    List<String> words;
    List<String> delimiters;

    @BeforeEach
    void setup()
    {
        input = "";
        words = Collections.emptyList();
        delimiters = Collections.emptyList();
    }

    @Test
    void testValidInput()
    {
        input = "abc.a.....b2..c";
        words = List.of("abc", "a", "c");
        delimiters = List.of("", ".", ".....b2..", "");
        testParsing(input, words, delimiters);
    }


    @Test
    void testNoWords()
    {
        input = "12!?.?.!";
        words = List.of();
        delimiters = List.of("12!?.?.!");
        testParsing(input, words, delimiters);
    }

    @Test
    void testLongWords()
    {
        input = "abcaaaaaaa!bbbbbbbbbb?cccccccccc.a1234";
        words = List.of("abcaaaaaaa", "bbbbbbbbbb", "cccccccccc");
        delimiters = List.of("", "!", "?", ".a1234");
        testParsing(input, words, delimiters);
    }

    @Test
    void testManyDelimiters()
    {
        input = "a!b?c.";
        words = List.of("a", "b", "c");
        delimiters = List.of("", "!", "?", ".");
        testParsing(input, words, delimiters);
    }

    @Test
    void testFewDelimiters()
    {
        input = "abc.def.ghi";
        words = List.of("abc", "def", "ghi");
        delimiters = List.of("", ".", ".", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseAllLowerCase()
    {
        input = "Word";
        words = List.of("word");
        delimiters = List.of("", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseNumbersDisregardAll()
    {
        input = "all4one";
        words = List.of();
        delimiters = List.of("all4one");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseNumbers()
    {
        input = "sam is all4one and 14all";
        words = List.of("sam", "is", "and");
        delimiters = List.of("", " ", " all4one ", " 14all");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseNumbersSpeltOut()
    {
        input = "we have five carrots carrots5";
        words = List.of("we", "have", "five", "carrots");
        delimiters = List.of("", " ", " ", " ", " carrots5");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseApostrophes()
    {
        input = "sam has ' forks";
        words = List.of("sam", "has", "forks");
        delimiters = List.of("", " ", " ' ", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseApostrophesPossessive()
    {
        input = "sam's forks";
        words = List.of("sam's", "forks");
        delimiters = List.of("", " ", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseApostrophesPossessiveRepeated()
    {
        input = "sam's sams sam\"s";
        words = List.of("sam's", "sams", "sam's");
        delimiters = List.of("", " ", " ", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseHyphen()
    {
        input = "one-hundred";
        words = List.of("onehundred");
        delimiters = List.of("", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseHyphenMultiple()
    {
        input = "one-hundred onehundred one hundred";
        words = List.of("onehundred", "onehundred", "one", "hundred");
        delimiters = List.of("", " ", " ", " ", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseHyphenLeadingTrailing()
    {
        input = "-one one-";
        words = List.of("one", "one");
        delimiters = List.of("-", " ", "-");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseInvalidatingSymbols()
    {
        input = "#Belief";
        words = List.of();
        delimiters = List.of("#Belief");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseEndStart()
    {
        input = "end.start";
        words = List.of("end", "start");
        delimiters = List.of("", ".", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseSentenceTerminatingPunctuation()
    {
        input = "it. came? to! pass@";
        words = List.of("it", "came", "to");
        delimiters = List.of("", ". ", "? ", "! pass@");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseLeadingSentenceTerminatingPunctuation()
    {
        input = ".it ?came !to @pass";
        words = List.of("it", "came", "to");
        delimiters = List.of(".", " ?", " !", " @pass");
        testParsing(input, words, delimiters);
    }

    @Test
    void testCaseHyphenInvalidating()
    {
        input = "7word wor7d word7";
        words = List.of();
        delimiters = List.of("7word wor7d word7");
        testParsing(input, words, delimiters);
    }

    @Test
    void testSams()
    {
        input = "sams' 'sams sams\" \"sams";
        words = List.of("sams", "sams", "sams", "sams");
        delimiters = List.of("", "' '", " ", "\" \"", "");
        testParsing(input, words, delimiters);
    }

    @Test
    void testDoubleSingleQuote()
    {
        input = "what 'what ''what '''what what what' what'' what'''";
        words = List.of("what", "what", "what", "what", "what", "what", "what", "what");
        delimiters = List.of("", " '", " ''", " '''", " ", " ", "' ", "'' ", "'''");
        testParsing(input, words, delimiters);
    }


}
