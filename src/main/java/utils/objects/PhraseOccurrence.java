package utils.objects;

import java.util.Objects;

public class PhraseOccurrence
{
    private String contents;
    private FunctionPhrase phrase;
    private int documentId;
    private int index;

    public PhraseOccurrence(String contents, FunctionPhrase phrase, int documentId, int index)
    {
        this.contents = contents;
        this.documentId = documentId;
        this.index = index;
        this.phrase = phrase;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public FunctionPhrase getPhrase()
    {
        return phrase;
    }

    public void setPhrase(FunctionPhrase phrase)
    {
        this.phrase = phrase;
    }

    public int getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(int documentId)
    {
        this.documentId = documentId;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhraseOccurrence that = (PhraseOccurrence) o;
        return documentId == that.documentId && index == that.index && Objects.equals(phrase, that.phrase);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(contents, phrase, documentId, index);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("PhraseOccurrence{");
        sb.append("contents='").append(contents).append('\'');
        sb.append(", phrase=").append(phrase);
        sb.append(", documentId=").append(documentId);
        sb.append(", index=").append(index);
        sb.append('}');
        return sb.toString();
    }
}
