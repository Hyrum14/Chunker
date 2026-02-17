package app.model.parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CharacterCategorizer
{
    public static final Set<Integer> HYPHENS = new HashSet<>();
    public static final Set<Integer> QUOTES = new HashSet<>();
    public static final Set<Integer> WHITESPACES = new HashSet<>();
    public static Map<Integer, CharacterCategoryType> specialCases;

    static
    {
        // Hyphen characters
        HYPHENS.add(0x002D); // HYPHEN-MINUS
        HYPHENS.add(0x00AD); // SOFT HYPHEN
        HYPHENS.add(0x2010); // HYPHEN
        HYPHENS.add(0x2011); // NON-BREAKING HYPHEN
        HYPHENS.add(0x2012); // FIGURE DASH
        HYPHENS.add(0x2013); // EN DASH
        HYPHENS.add(0x2014); // EM DASH
        HYPHENS.add(0x207B); // SUPERSCRIPT MINUS
        HYPHENS.add(0x208B); // SUBSCRIPT MINUS
        HYPHENS.add(0x2212); // MINUS SIGN
        HYPHENS.add(0x301C); // WAVE DASH
        HYPHENS.add(0x3030); // WAVY DASH

        // Quote characters
        QUOTES.add(0x0060); // GRAVE ACCENT
        QUOTES.add(0x02CB); // MODIFIER LETTER GRAVE ACCENT
        QUOTES.add(0x2035); // REVERSED PRIME
        QUOTES.add(0x2036); // REVERSED DOUBLE PRIME
        QUOTES.add(0x2039); // SINGLE LEFT-POINTING ANGLE QUOTATION MARK
        QUOTES.add(0x203A); // SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
        QUOTES.add(0x300C); // LEFT CORNER BRACKET
        QUOTES.add(0x300D); // RIGHT CORNER BRACKET
        QUOTES.add(0x300E); // LEFT WHITE CORNER BRACKET
        QUOTES.add(0x300F); // RIGHT WHITE CORNER BRACKET
        QUOTES.add(0x301D); // REVERSED DOUBLE PRIME QUOTATION MARK
        QUOTES.add(0x301E); // DOUBLE PRIME QUOTATION MARK
        QUOTES.add(0x301F); // LOW DOUBLE PRIME QUOTATION MARK
        QUOTES.add(0x0022); // DOUBLE QUOTATION MARK
        QUOTES.add(0x0027); // Single Apostrophe
        QUOTES.add(0x2018); // LEFT SINGLE QUOTATION MARK
        QUOTES.add(0x2019); // RIGHT SINGLE QUOTATION MARK
        QUOTES.add(0x201C); // LEFT DOUBLE QUOTATION MARK
        QUOTES.add(0x201D); // RIGHT DOUBLE QUOTATION MARK
        QUOTES.add(0x201A); // SINGLE LOW-9 QUOTATION MARK
        QUOTES.add(0x2032); // PRIME
        QUOTES.add(0x201E); // DOUBLE LOW-9 QUOTATION MARK
        QUOTES.add(0x2033); // DOUBLE PRIME
        QUOTES.add(0x3003); // DITTO MARK

        WHITESPACES.add(0x0009); // CHARACTER TABULATION
        WHITESPACES.add(0x000A); // LINE FEED
        WHITESPACES.add(0x000B); // LINE TABULATION
        WHITESPACES.add(0x000C); // FORM FEED
        WHITESPACES.add(0x000D); // CARRIAGE RETURN
        WHITESPACES.add(0x0020); // SPACE
        WHITESPACES.add(0x0085); // NEXT LINE
        WHITESPACES.add(0x00A0); // NO-BREAK SPACE
        WHITESPACES.add(0x1680); // OGHAM SPACE MARK
        WHITESPACES.add(0x2000); // EN QUAD
        WHITESPACES.add(0x2001); // EM QUAD
        WHITESPACES.add(0x2002); // EN SPACE
        WHITESPACES.add(0x2003); // EM SPACE
        WHITESPACES.add(0x2004); // THREE-PER-EM SPACE
        WHITESPACES.add(0x2005); // FOUR-PER-EM SPACE
        WHITESPACES.add(0x2006); // SIX-PER-EM SPACE
        WHITESPACES.add(0x2007); // FIGURE SPACE
        WHITESPACES.add(0x2008); // PUNCTUATION SPACE
        WHITESPACES.add(0x2009); // THIN SPACE
        WHITESPACES.add(0x200A); // HAIR SPACE
        WHITESPACES.add(0x2028); // LINE SEPARATOR
        WHITESPACES.add(0x2029); // PARAGRAPH SEPARATOR
        WHITESPACES.add(0x202F); // NARROW NO-BREAK SPACE
        WHITESPACES.add(0x205F); // MEDIUM MATHEMATICAL SPACE
        WHITESPACES.add(0x3000); // IDEOGRAPHIC SPACE

        specialCases = new HashMap<>();
        for (Integer x : QUOTES)
            specialCases.put(x, CharacterCategoryType.CONTEXT_LETTER);
        for (Integer x : HYPHENS)
            specialCases.put(x, CharacterCategoryType.CONTEXT_LETTER);
        for (Integer x : WHITESPACES)
            specialCases.put(x, CharacterCategoryType.SPLIT);

        specialCases.put(0x0023, CharacterCategoryType.INVALID); // NUMBER SIGN (#)
        specialCases.put(0x0040, CharacterCategoryType.INVALID); // AT SYMBOL (@)
    }


    public static boolean isInvalid(int codePoint)
    {
        if (specialCases.containsKey(codePoint))
            return specialCases.get(codePoint).isInvalid();

        int type = Character.getType(codePoint);

        return type == Character.OTHER_NUMBER ||
                type == Character.LETTER_NUMBER ||
                type == Character.DECIMAL_DIGIT_NUMBER ||
                type == Character.CURRENCY_SYMBOL ||
                type == Character.MODIFIER_SYMBOL ||
                type == Character.MATH_SYMBOL ||
                type == Character.OTHER_SYMBOL ||
                type == Character.CONTROL ||
                type == Character.UNASSIGNED ||
                type == Character.PRIVATE_USE ||
                type == Character.SURROGATE ||
                type == Character.COMBINING_SPACING_MARK ||
                type == Character.ENCLOSING_MARK;
    }

    public static boolean isSplit(int codePoint)
    {
        if (specialCases.containsKey(codePoint))
            return specialCases.get(codePoint).isSplit();

        int type = Character.getType(codePoint);

        return type == Character.SPACE_SEPARATOR ||
                type == Character.LINE_SEPARATOR ||
                type == Character.PARAGRAPH_SEPARATOR ||
                type == Character.OTHER_PUNCTUATION ||
                type == Character.START_PUNCTUATION ||
                type == Character.END_PUNCTUATION ||
                type == Character.FORMAT;
    }

    public static boolean isLetter(int codePoint)
    {
        if (specialCases.containsKey(codePoint))
            return specialCases.get(codePoint).isLetter();

        int type = Character.getType(codePoint);

        return (type == Character.UPPERCASE_LETTER ||
                type == Character.LOWERCASE_LETTER ||
                type == Character.TITLECASE_LETTER ||
                type == Character.MODIFIER_LETTER ||
                type == Character.OTHER_LETTER);
    }

    public static boolean isContextLetter(int codePoint)
    {
        if (specialCases.containsKey(codePoint))
            return specialCases.get(codePoint).isContextLetter();

        int type = Character.getType(codePoint);

        return type == Character.CONNECTOR_PUNCTUATION ||
                type == Character.DASH_PUNCTUATION ||
                type == Character.INITIAL_QUOTE_PUNCTUATION ||
                type == Character.FINAL_QUOTE_PUNCTUATION ||
                type == Character.NON_SPACING_MARK;
    }

    public enum CharacterCategoryType
    {
        INVALID("INVALID"),
        SPLIT("SPLIT"),
        LETTER("LETTER"),
        CONTEXT_LETTER("CONTEXT_LETTER");

        private final String value;

        CharacterCategoryType(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }

        public boolean isInvalid()
        {
            return value.equals(INVALID.value);
        }

        public boolean isSplit()
        {
            return value.equals(SPLIT.value);
        }

        public boolean isLetter()
        {
            return value.equals(LETTER.value);
        }

        public boolean isContextLetter()
        {
            return value.equals(CONTEXT_LETTER.value);
        }
    }
}
