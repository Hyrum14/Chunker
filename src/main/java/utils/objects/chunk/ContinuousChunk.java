package utils.objects.chunk;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.OccurrenceDao;
import utils.exeptions.TaskException;
import utils.objects.PhraseOccurrence;
import utils.objects.Token;

import java.io.FileWriter;
import java.io.IOException;

public class ContinuousChunk extends Chunk
{
    protected final int startIndex;
    protected final int endIndex;

    public ContinuousChunk(int chunkId, int documentId, String documentName, int startIndex, int endIndex)
    {
        super(chunkId, documentId, documentName, (endIndex - startIndex + 1));
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }


    @Override
    public String toStringVerbose()
    {
        final StringBuilder sb = new StringBuilder("ContinuousChunk{");
        sb.append("documentName='").append(getDocumentName()).append('\'');
        sb.append(", chunkId=").append(getChunkId());
        sb.append(", documentId=").append(getDocumentId());
        sb.append(", startIndex=").append(startIndex);
        sb.append(", endIndex=").append(endIndex);
        sb.append(", size=").append(getSize());
        sb.append(", chunkName='").append(getChunkName()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean isInChunk(Token token)
    {
        return (token.getDocumentId() == getDocumentId()) && isInRange(token.getIndex());
    }

    @Override
    public boolean isInChunk(PhraseOccurrence phraseOccurrence)
    {
        if (getDocumentId() != phraseOccurrence.getDocumentId())
            return false;

        if (!isInRange(phraseOccurrence.getIndex()))
            return false;

        if (phraseOccurrence.getPhrase().size() == 1)
            return true;

        return isInRange(phraseOccurrence.getIndex() + phraseOccurrence.getPhrase().size() - 1);
    }


    public boolean isInRange(int index)
    {
        return index <= endIndex && index >= startIndex;
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public int getEndIndex()
    {
        return endIndex;
    }

    @Override
    public void recreateChunk(FileWriter writer, DelimiterDao delimiterDao, OccurrenceDao occurrenceDao) throws IOException
    {
        for (int i = getStartIndex(); i <= getEndIndex(); ++i)
        {
            writer.write(delimiterDao.get(i, getDocumentId()).getContents());
            writer.write(occurrenceDao.getOccurrence(getDocumentId(), i).getContents());
        }
        writer.write(delimiterDao.get(getEndIndex(), getDocumentId()).getContents());
    }

    @Override
    public void generateExtraData(OccurrenceDao occurrenceDao) throws TaskException
    {
        // no extra data needed
    }

    @Override
    public int getNumOccurrences(Token token)
    {
        if (!isInChunk(token))
            return 0;
        return 1;
    }

    @Override
    public int getNumOccurrences(PhraseOccurrence phraseOccurrence)
    {
        if (!isInChunk(phraseOccurrence))
            return 0;
        return 1;
    }
}
