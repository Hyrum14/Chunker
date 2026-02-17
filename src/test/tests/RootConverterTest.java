package test.tests;

import app.model.parsing.RootConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RootConverterTest
{
    @Test
    public void testToLowerCase()
    {
        String original = "HELLO World";
        String expected = "hello world";
        String actual = RootConverter.toLowerCase(original);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripDiacritics()
    {
        String original = "Héllo Wörld";
        String expected = "Hello World";
        String actual = RootConverter.stripDiacritics(original);
        assertEquals(expected, actual);
    }

    @Test
    public void testConvertSymbolsHyphen()
    {
        String original = new String(new int[]{0x2010}, 0, 1); // HYPHEN
        String expected = "";
        String actual = RootConverter.convertSymbols(original);
        assertEquals(expected, actual);
    }

    @Test
    public void testConvertSymbolsQuote()
    {
        String original = new String(new int[]{0x0060}, 0, 1); // GRAVE ACCENT
        String expected = "";
        String actual = RootConverter.convertSymbols(original);
        assertEquals(expected, actual);
    }

    @Test
    public void testConvert()
    {
        String original = "Héllo Wörld" + new String(new int[]{0x2010, 0x0060}, 0, 2); // HYPHEN and GRAVE ACCENT
        String expected = "hello world";
        String actual = RootConverter.convert(original);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripDiacriticsWithCommonCharacters()
    {
        assertEquals("cliche", RootConverter.stripDiacritics("cliché"));
        assertEquals("resume", RootConverter.stripDiacritics("résumé"));
        assertEquals("naive", RootConverter.stripDiacritics("naïve"));
        assertEquals("facade", RootConverter.stripDiacritics("façade"));
        assertEquals("protege", RootConverter.stripDiacritics("protégé"));
        assertEquals("expose", RootConverter.stripDiacritics("exposé"));
        assertEquals("role", RootConverter.stripDiacritics("rôle"));
    }

    @Test
    public void testStripDiacriticsWithSingleCharacters()
    {
        assertEquals("a", RootConverter.stripDiacritics("á"));
        assertEquals("e", RootConverter.stripDiacritics("ē"));
        assertEquals("i", RootConverter.stripDiacritics("ï"));
        assertEquals("o", RootConverter.stripDiacritics("ö"));
        assertEquals("u", RootConverter.stripDiacritics("ü"));
    }

    @Test
    public void testStripDiacriticsWithEmptyString()
    {
        assertEquals("", RootConverter.stripDiacritics(""));
    }

    @Test
    public void testStripDiacriticsWithUnicodeCharacters()
    {
        assertEquals("u", RootConverter.stripDiacritics("\u00FC")); // ü
        assertEquals("o", RootConverter.stripDiacritics("\u00F6")); // ö
        assertEquals("a", RootConverter.stripDiacritics("\u00E1")); // á
    }

    @Test
    public void testStripDiacriticsWithLongString()
    {
        String longString = "cliché".repeat(10000);
        String expectedLongString = "cliche".repeat(10000);
        assertEquals(expectedLongString, RootConverter.stripDiacritics(longString));
    }

    @Test
    public void testStripDiacriticsWithCombiningDiacriticalMarks()
    {
        for (int i = 0x0300; i <= 0x036F; i++)
        {
            String input = "a" + new String(Character.toChars(i));
            assertEquals("a", RootConverter.stripDiacritics(input));
        }
    }
}
