package app.model.parsing;

import java.text.Normalizer;

public class RootConverter
{

    private static final String STANDARD_HYPHEN = "";
    private static final String STANDARD_QUOTE = "'";


    public static String convert(String original)
    {
        return convertSymbols(stripDiacritics(toLowerCase(original)));
    }

    public static String toLowerCase(String original)
    {
        return original.toLowerCase();
    }

    public static String stripDiacritics(String original)
    {
        return Normalizer.normalize(original, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static String convertSymbols(String original)
    {
        StringBuilder converted = new StringBuilder();


        for (int i = 0; i < original.length(); ++i)
        {
            char c = original.toCharArray()[i];
            int cp = Character.codePointAt(new char[]{c}, 0);

            if (CharacterCategorizer.HYPHENS.contains(cp))
            {
                if (i != 0 && i != (original.length() - 1))
                    converted.append(STANDARD_HYPHEN);
            } else if (CharacterCategorizer.QUOTES.contains(cp))
            {
                if (i != 0 && i != (original.length() - 1))
                    converted.append(STANDARD_QUOTE);
            } else
            {
                converted.append(c);
            }
        }

        return converted.toString();
    }
}