package utils.objects.chunk;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.OccurrenceDao;
import utils.exeptions.TaskException;
import utils.objects.PhraseOccurrence;
import utils.objects.Token;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Chunk implements Comparable<Chunk>
{
    private final String documentName;
    private int chunkId;
    private int documentId;
    private int size;
    private String chunkName;

    protected Chunk(int chunkId, int documentId, String documentName, int size)
    {
        this.chunkId = chunkId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.size = size;
        chunkName = chunkNameFormatter(documentName, chunkId, documentId);
    }

    public static String chunkNameFormatter(String documentName, int chunkId, int docId)
    {
        String format = "%s_doc%04d_chunk%04d";
        return String.format(format, documentName, docId, chunkId);
    }

    public String getChunkName()
    {
        if (chunkName == null || chunkName.equals(""))
        {
            chunkName = chunkNameFormatter(documentName, chunkId, documentId);
        }
        return chunkName;
    }

    public abstract String toStringVerbose();

    public abstract boolean isInChunk(Token token);

    public abstract boolean isInChunk(PhraseOccurrence phraseOccurrence);

    public int getChunkId()
    {
        return chunkId;
    }

    public int getDocumentId()
    {
        return documentId;
    }

    public int getSize()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return getChunkName();
    }

    public String getDocumentName()
    {
        return documentName;
    }

    @Override
    public int compareTo(Chunk o)
    {
        return this.getChunkName().compareTo(o.getChunkName());
    }

    public abstract void recreateChunk(FileWriter writer, DelimiterDao delimiterDao, OccurrenceDao occurrenceDao) throws IOException;

    public abstract void generateExtraData(OccurrenceDao occurrenceDao) throws TaskException;

    public abstract int getNumOccurrences(Token token);

    public abstract int getNumOccurrences(PhraseOccurrence phraseOccurrence);
}

