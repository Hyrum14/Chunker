package utils.objects.chunk;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.OccurrenceDao;
import utils.exeptions.TaskException;
import utils.objects.PhraseOccurrence;
import utils.objects.Token;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagOfWordsChunk extends Chunk
{
    private final int documentSize;
    Map<Token, Integer> occurrences;
    Map<Integer, Integer> indecencies;
    List<Token> tokenList;
    int count = 0;


    public BagOfWordsChunk(int chunkId, int documentId, String documentName, int chunkSize, int documentSize)
    {
        super(chunkId, documentId, documentName, chunkSize);
        occurrences = new HashMap<>();
        indecencies = new HashMap<>();
        tokenList = new ArrayList<>();
        this.documentSize = documentSize;
    }


    @Override
    public String toStringVerbose()
    {
        final StringBuilder sb = new StringBuilder("BagOfWordsChunk{");
        sb.append(super.toString());
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean isInChunk(Token token)
    {
        if (token.getDocumentId() != getDocumentId())
            return false;
//        System.out.println("x");
        return occurrences.containsKey(token);
    }

    @Override
    public boolean isInChunk(PhraseOccurrence phraseOccurrence)
    {
        if (phraseOccurrence.getDocumentId() != getDocumentId())
            return false;
        if (phraseOccurrence.getPhrase().size() > 1 || phraseOccurrence.getPhrase().size() == 0)
            return false;
//        System.out.println(phraseOccurrence + " found count = " + indecencies.get(phraseOccurrence.getIndex()));
        return indecencies.containsKey(phraseOccurrence.getIndex());
    }


    @Override
    public void recreateChunk(FileWriter writer, DelimiterDao delimiterDao, OccurrenceDao occurrenceDao) throws IOException
    {
        String sep = "";
        for (Token token : tokenList)
        {
            writer.write(sep);
            writer.write(token.getContents());
            sep = " ";
        }
    }

    @Override
    public void generateExtraData(OccurrenceDao occurrenceDao) throws TaskException
    {
        for (int wordId = 0; wordId < getSize(); ++wordId)
        {
            add(
                    occurrenceDao.getOccurrence( 
                            getDocumentId(),
                            occurrenceDao.pickRandomInt(documentSize)
                    )
            );
        }
//        System.out.println("-------------------");
    }

    @Override
    public int getNumOccurrences(Token token)
    {
        if (!isInChunk(token))
            return 0;
//        System.out.println(getChunkName() + " occ " + occurrence.getRoot() + " count= " + occurrences.get(occurrence));
        return occurrences.get(token);
    }

    @Override
    public int getNumOccurrences(PhraseOccurrence phraseOccurrence)
    {
        if (!isInChunk(phraseOccurrence))
            return 0;
//        System.out.println(getChunkName() + " phrase-occ " + phraseOccurrence.getPhrase().toString() + " count= " + indecencies.get(phraseOccurrence.getIndex()));
        return indecencies.get(phraseOccurrence.getIndex());
    }

    protected void add(Token token) throws TaskException
    {
        if (isFull())
            throw new TaskException("Attempted to add a word to a BagOfWordsChunk that was already full");
        if (token.getDocumentId() != getDocumentId())
            throw new TaskException("Occurrence did not match expected document id");
//        System.out.println(getChunkName() + " new key = " + (occurrences.getOrDefault(occurrence, 0) + 1));
        occurrences.put(token, occurrences.getOrDefault(token, 0) + 1);
        indecencies.put(token.getIndex(), indecencies.getOrDefault(token.getIndex(), 0) + 1);
        tokenList.add(token);
        count += 1;
    }

    public boolean isFull()
    {
        return (count == getSize());
    }
}
