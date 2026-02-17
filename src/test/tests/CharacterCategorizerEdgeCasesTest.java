package test.tests;

import app.model.parsing.CharacterCategorizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharacterCategorizerEdgeCasesTest
{
    @Test
    public void simpleTestIsInvalid()
    {
        assertTrue(CharacterCategorizer.isInvalid('3'));  // numeric
        assertTrue(CharacterCategorizer.isInvalid("💩".codePointAt(0)));  // emoji
    }

    @Test
    public void simpleTestIsSplit()
    {
        assertTrue(CharacterCategorizer.isSplit('.'));  // sentence-ending punctuation
        assertTrue(CharacterCategorizer.isSplit(' '));  // whitespace
    }

    @Test
    public void simpleTestIsLetter()
    {
        assertTrue(CharacterCategorizer.isLetter('a'));  // English letter
        assertTrue(CharacterCategorizer.isLetter('é'));  // English letter with diacritic
    }

    @Test
    public void simpleTestIsContextLetter()
    {
        assertTrue(CharacterCategorizer.isContextLetter(0x0027)); // APOSTROPHE
    }

    @Test
    public void testQuotes()
    {
        assertTrue(CharacterCategorizer.isContextLetter(0x0027)); // APOSTROPHE
        assertTrue(CharacterCategorizer.isContextLetter(0x2018)); // LEFT SINGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x2019)); // RIGHT SINGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x201A)); // SINGLE LOW-9 QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x2032)); // PRIME
        assertTrue(CharacterCategorizer.isContextLetter(0x2039)); // SINGLE LEFT-POINTING ANGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x203A)); // SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x300C)); // LEFT CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300D)); // RIGHT CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300E)); // LEFT WHITE CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300F)); // RIGHT WHITE CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x301D)); // REVERSED DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301E)); // DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301F)); // LOW DOUBLE PRIME QUOTATION MARK

        assertTrue(CharacterCategorizer.isContextLetter(0x0022)); // QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x201C)); // LEFT DOUBLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x201D)); // RIGHT DOUBLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x201E)); // DOUBLE LOW-9 QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x2033)); // DOUBLE PRIME
        assertTrue(CharacterCategorizer.isContextLetter(0x2036)); // REVERSED DOUBLE PRIME
        assertTrue(CharacterCategorizer.isContextLetter(0x3003)); // DITTO MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301D)); // REVERSED DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301E)); // DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301F)); // LOW DOUBLE PRIME QUOTATION MARK
    }

    @Test
    public void testHyphens()
    {
        assertTrue(CharacterCategorizer.isContextLetter(0x002D)); // HYPHEN-MINUS
        assertTrue(CharacterCategorizer.isContextLetter(0x00AD)); // SOFT HYPHEN
        assertTrue(CharacterCategorizer.isContextLetter(0x2010)); // HYPHEN
        assertTrue(CharacterCategorizer.isContextLetter(0x2011)); // NON-BREAKING HYPHEN
        assertTrue(CharacterCategorizer.isContextLetter(0x2012)); // FIGURE DASH
        assertTrue(CharacterCategorizer.isContextLetter(0x2013)); // EN DASH
        assertTrue(CharacterCategorizer.isContextLetter(0x2014)); // EM DASH
        assertTrue(CharacterCategorizer.isContextLetter(0x207B)); // SUPERSCRIPT MINUS
        assertTrue(CharacterCategorizer.isContextLetter(0x208B)); // SUBSCRIPT MINUS
        assertTrue(CharacterCategorizer.isContextLetter(0x2212)); // MINUS SIGN
        assertTrue(CharacterCategorizer.isContextLetter(0x301C)); // WAVE DASH
        assertTrue(CharacterCategorizer.isContextLetter(0x3030)); // WAVY DASH
    }

    @Test
    public void testBacktick()
    {
        assertTrue(CharacterCategorizer.isContextLetter(0x0060)); // GRAVE ACCENT
        assertTrue(CharacterCategorizer.isContextLetter(0x02CB)); // MODIFIER LETTER GRAVE ACCENT
        assertTrue(CharacterCategorizer.isContextLetter(0x2035)); // REVERSED PRIME
        assertTrue(CharacterCategorizer.isContextLetter(0x2036)); // REVERSED DOUBLE PRIME
        assertTrue(CharacterCategorizer.isContextLetter(0x2039)); // SINGLE LEFT-POINTING ANGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x203A)); // SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x300C)); // LEFT CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300D)); // RIGHT CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300E)); // LEFT WHITE CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x300F)); // RIGHT WHITE CORNER BRACKET
        assertTrue(CharacterCategorizer.isContextLetter(0x301D)); // REVERSED DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301E)); // DOUBLE PRIME QUOTATION MARK
        assertTrue(CharacterCategorizer.isContextLetter(0x301F)); // LOW DOUBLE PRIME QUOTATION MARK
    }

    @Test
    public void testAllWhitespaceCharacters()
    {
        int[] whitespaceChars = {
                0x0009, // CHARACTER TABULATION
                0x000A, // LINE FEED
                0x000B, // LINE TABULATION
                0x000C, // FORM FEED
                0x000D, // CARRIAGE RETURN
                0x0020, // SPACE
                0x0085, // NEXT LINE
                0x00A0, // NO-BREAK SPACE
                0x1680, // OGHAM SPACE MARK
                0x2000, // EN QUAD
                0x2001, // EM QUAD
                0x2002, // EN SPACE
                0x2003, // EM SPACE
                0x2004, // THREE-PER-EM SPACE
                0x2005, // FOUR-PER-EM SPACE
                0x2006, // SIX-PER-EM SPACE
                0x2007, // FIGURE SPACE
                0x2008, // PUNCTUATION SPACE
                0x2009, // THIN SPACE
                0x200A, // HAIR SPACE
                0x2028, // LINE SEPARATOR
                0x2029, // PARAGRAPH SEPARATOR
                0x202F, // NARROW NO-BREAK SPACE
                0x205F, // MEDIUM MATHEMATICAL SPACE
                0x3000  // IDEOGRAPHIC SPACE
        };

        for (int c : whitespaceChars)
        {
            System.out.printf("%c [UTF+%04x]%n", c, c);
            assertTrue(CharacterCategorizer.isSplit(c));
        }
    }
}
