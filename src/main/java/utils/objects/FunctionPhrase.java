package utils.objects;

import app.model.parsing.DocumentParser;
import app.model.parsing.DocumentParsingException;
import app.model.parsing.RootConverter;
import app.model.parsing.StringDocumentProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunctionPhrase implements Comparable<FunctionPhrase>
{
    public final static String EMPTY_PART_OF_SPEECH = "[EMPTY]";
    public static final String FUNCTION_PHRASE = "FUNCTION_PHRASE";
    // Part of speech
    String partOfSpeech;
    List<String> phrase;

    public FunctionPhrase(String partOfSpeech, List<String> phrase)
    {
        if (partOfSpeech == null || partOfSpeech.equals(""))
            this.partOfSpeech = EMPTY_PART_OF_SPEECH;
        else
            this.partOfSpeech = partOfSpeech;
        this.phrase = phrase;
//        System.out.println(this);
    }

    public FunctionPhrase(String partOfSpeech, String phrase)
    {
        if (partOfSpeech == null || partOfSpeech.equals(""))
            this.partOfSpeech = EMPTY_PART_OF_SPEECH;
        else
            this.partOfSpeech = partOfSpeech;
        this.phrase = parse(phrase);
//        System.out.println(this);
    }

    public static List<String> parse(String phrase)
    {
        DocumentParser documentParser = new DocumentParser();
        List<String> output = new ArrayList<>();
        try
        {
            documentParser.parse(new StringDocumentProvider(phrase), new DocumentParser.DocumentStorage()
            {
                @Override
                public void storeDelimiter(String contents)
                {

                }

                @Override
                public void storeOccurrence(String contents)
                {
                    output.add(RootConverter.convert(contents));
                }
            });
        } catch (DocumentParsingException e)
        {
            e.printStackTrace();
        }
        return output;
    }

    public String getPartOfSpeech()
    {
        return partOfSpeech;
    }


    public List<String> toList()
    {
        return phrase;
    }

    public void setPhrase(List<String> phrase)
    {
        this.phrase = phrase;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPhrase that = (FunctionPhrase) o;
        return Objects.equals(partOfSpeech, that.partOfSpeech) && Objects.equals(phrase, that.phrase);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(partOfSpeech, phrase);
    }

    @Override
    public String toString()
    {
        return String.join(" ", phrase);
    }

    public String getPhraseString()
    {
        return toString();
    }

    @Override
    public int compareTo(FunctionPhrase o)
    {

        int result = this.toString().compareTo(o.toString());
        if (result == 0)
            result = this.partOfSpeech.compareTo(o.getPartOfSpeech());
        return result;
    }

    public int size()
    {
        return phrase.size();
    }

    public boolean hasPartOfSpeech()
    {
        return (partOfSpeech != null && !partOfSpeech.equals("") && !partOfSpeech.equals(EMPTY_PART_OF_SPEECH));
    }


    // countByChunk Per PartOfSpeech
    // total for all chunks for each function word and for each PartOfSpeech
//    find (FACT * (200 / 1000)) 2000

}
