package utils.objects;

import app.model.parsing.CharacterCategorizer;
import utils.logging.DummyLogger;
import utils.logging.LogLevel;
import utils.logging.Logger;

import java.util.Objects;

public class Delimiter
{
    private int index;
    private int documentId;
    private String contents;
    private Boolean invalid = null;

    public Delimiter(int index, int documentId, String contents)
    {
        this.index = index;
        this.documentId = documentId;
        this.contents = contents;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(int documentId)
    {
        this.documentId = documentId;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delimiter delimiter = (Delimiter) o;
        return index == delimiter.index && documentId == delimiter.documentId && Objects.equals(contents, delimiter.contents);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index, documentId, contents);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("WordDelimiter{");
        sb.append("index=").append(index);
        sb.append(", documentId=").append(documentId);
        sb.append(", contents='").append(contents).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean invalidatesPhrase(Logger logger)
    {
        if (logger == null)
        {
            logger = new DummyLogger();
        }

        if (invalid != null)
            return invalid;

        for (int i = 0; i < contents.length(); ++i)
        {
            logger.log(LogLevel.TRACE, String.format("'%c'[U+%04d] is whitespace = %b", contents.charAt(i), contents.codePointAt(i), CharacterCategorizer.WHITESPACES.contains(contents.codePointAt(i))));
            if (!CharacterCategorizer.WHITESPACES.contains(contents.codePointAt(i)))
            {
                invalid = true;
                break;
            }
        }

        if (invalid == null)
            invalid = false;

        return invalid;
    }

}