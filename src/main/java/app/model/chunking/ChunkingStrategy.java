package app.model.chunking;

import app.model.export.formating.page.generators.ChunkDataPage;
import app.model.export.formating.page.generators.DocumentDataPage;
import app.model.export.formating.page.generators.EvaluationDataPage;
import utils.exeptions.TaskException;
import utils.logging.LogLevel;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.chunk.Chunk;
import utils.objects.chunk.ContinuousChunk;

import java.util.*;

public abstract class ChunkingStrategy
{
    protected final Logger chunkingStrategyLogger;
    protected final Map<Integer, Integer> docIdToNumChunks;
    protected final Map<Integer, Integer> docIdToChunkSize;
    protected List<Chunk> output;
    protected List<Document> documents;
    protected WarningHandler warningHandler;

    protected ChunkingStrategy(Logger chunkingStrategyLogger)
    {
        this.chunkingStrategyLogger = chunkingStrategyLogger;
        docIdToNumChunks = new HashMap<>();
        docIdToChunkSize = new HashMap<>();
    }

    public List<Chunk> generate(List<Document> documents, WarningHandler warningHandler) throws TaskException
    {
        if (warningHandler == null)
        {
            throw new TaskException("Provided warning handler reference was null");
        }

        if (documents == null || documents.isEmpty())
        {
            throw new TaskException(String.format("(Strategy: %s) must provide at least 1 document to perform an evaluation", getName()));
        }
        output = new ArrayList<>();
        this.documents = documents;
        this.warningHandler = warningHandler;

        generateChunks();

        if (output == null || output.isEmpty())
        {
            throw new TaskException(String.format("(Strategy: %s) No chunks produced", getName()));
        }
        return output;
    }

    protected abstract void generateChunks() throws TaskException;

    public List<Integer> getChunkSizes(int docId, int totalWords, int numChunks)
    {
        List<Integer> chunkSizes = new ArrayList<>();
        int minChunkSize = totalWords / numChunks;
//        int remainder = totalWords % minChunkSize;
        int remainder = totalWords % numChunks;

        // 7003 words
        // 10 chunks
        // minCSize = 700
        // rem 3
        Set<Integer> chunksRequireExtraWord = new HashSet<>();
        docIdToChunkSize.put(docId, minChunkSize);
        docIdToNumChunks.put(docId, numChunks);
        while (remainder > 0)
        {
            int candidate = new Random().nextInt(numChunks);
            if (chunksRequireExtraWord.contains(candidate))
                continue;
            chunksRequireExtraWord.add(candidate);
            remainder -= 1;
        }

        for (int i = 0; i < numChunks; i++)
        {
            chunkSizes.add(minChunkSize + (chunksRequireExtraWord.contains(i) ? 1 : 0));
        }

        return chunkSizes;
    }

    protected void createChunk(Document document, int numChunks)
    {
        int chunkId = 1;
        List<Integer> chunkSizes = getChunkSizes(document.getId(), document.getNumWords(), numChunks);
        int startIndex = 0;
        for (int chunkSize : chunkSizes)
        {
            int endIndex = startIndex + chunkSize - 1; // subtract 1 to avoid out by one error

            Chunk result = new ContinuousChunk(
                    chunkId,
                    document.getId(),
                    document.getFileName(),
                    startIndex,
                    endIndex
            );
            output.add(result);
            chunkingStrategyLogger.log(LogLevel.TRACE, result.getChunkName());
            chunkingStrategyLogger.log(LogLevel.TRACE, result.toStringVerbose());
            startIndex = endIndex + 1; // increment startIndex to be one more than endIndex
            chunkId++;
        }
    }

    public abstract String getName();

    public abstract DocumentDataPage getDocumentDataPage();

    public abstract ChunkDataPage getChunkDataPage();

    public String toString()
    {
        return getName();
    }

    public abstract EvaluationDataPage getMetaDataPage(int standardizedCountFactor);


    public interface WarningHandler
    {
        void handleWarning(String message);
    }
}
