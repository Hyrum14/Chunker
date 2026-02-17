package utils.objects;

import java.util.Objects;

public class Word
{
    private String root;
    private int documentId;
    private int chunkId;
    private int numOccurrences;

    public Word(String root, int documentId, int numOccurrences)
    {
        this.root = root;
        this.documentId = documentId;
        this.numOccurrences = numOccurrences;
        chunkId = 0;
    }

    public Word(String root, int documentId, int chunkId, int numOccurrences)
    {
        this.root = root;
        this.documentId = documentId;
        this.chunkId = chunkId;
        this.numOccurrences = numOccurrences;
    }

    public Word addOccurrence()
    {
        numOccurrences++;
        return this;
    }

    public String getRoot()
    {
        return root;
    }

    public void setRoot(String root)
    {
        this.root = root;
    }

    public int getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(int documentId)
    {
        this.documentId = documentId;
    }

    public int getChunkId()
    {
        return chunkId;
    }

    public void setChunkId(int chunkId)
    {
        this.chunkId = chunkId;
    }

    public int getNumOccurrences()
    {
        return numOccurrences;
    }

    public void setNumOccurrences(int numOccurrences)
    {
        this.numOccurrences = numOccurrences;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return documentId == word.documentId && chunkId == word.chunkId && numOccurrences == word.numOccurrences && Objects.equals(root, word.root);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(root, documentId, chunkId, numOccurrences);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Word{");
        sb.append("root='").append(root).append('\'');
        sb.append(", documentId=").append(documentId);
        sb.append(", chunkId=").append(chunkId);
        sb.append(", numOccurrences=").append(numOccurrences);
        sb.append('}');
        return sb.toString();
    }
}
