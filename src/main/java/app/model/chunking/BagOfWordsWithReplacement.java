package app.model.chunking;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import app.model.export.formating.page.generators.ChunkDataPage;
import app.model.export.formating.page.generators.DocumentDataPage;
import app.model.export.formating.page.generators.EvaluationDataPage;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.chunk.BagOfWordsChunk;
import utils.objects.chunk.Chunk;

import java.util.Collections;
import java.util.List;

public class BagOfWordsWithReplacement extends ChunkingStrategy
{
    protected final int numberOfChunks;
    protected final int chunksSize;

    public BagOfWordsWithReplacement(Logger chunkingStrategyLogger, int numberOfChunks, int chunksSize)
    {
        super(chunkingStrategyLogger);
        this.numberOfChunks = numberOfChunks;
        this.chunksSize = chunksSize;
    }

    @Override
    protected void generateChunks() throws TaskException
    {
        for (Document document : documents)
        {
            if (document.getNumWords() <= 0)
                warningHandler.handleWarning("Document was skipped because it had less than 1 word");
            for (int chunkId = 1; chunkId <= numberOfChunks; ++chunkId)
            {
                BagOfWordsChunk chunk = new BagOfWordsChunk(
                        chunkId,
                        document.getId(),
                        document.getFileName(),
                        chunksSize,
                        document.getNumWords()
                );

                output.add(chunk);
            }
        }
    }

    @Override
    public String getName()
    {
        return "Bag of Words (with replacement)";
    }

    @Override
    public DocumentDataPage getDocumentDataPage()
    {
        return new BagOfWordsWithReplacementChunkDataPage(numberOfChunks, chunksSize);
    }

    @Override
    public ChunkDataPage getChunkDataPage()
    {
        return new ChunkDataPage()
        {
            @Override
            public List<Cell> generateExtraChunkDetails(Chunk chunk)
            {
                return Collections.emptyList();
            }

            @Override
            public List<Cell> generateExtraLabels()
            {
                return Collections.emptyList();
            }

            @Override
            public void addRest()
            {

            }
        };
    }

    @Override
    public EvaluationDataPage getMetaDataPage(int standardizedCountFactor)
    {
        return new EvaluationDataPage(standardizedCountFactor)
        {
            @Override
            public void addRest()
            {
                addRow(new LabelCell(LabelConstant.CHUNKING_STRATEGY), new Cell("Bag of Words (with replacement)"));
                addRow(new LabelCell(LabelConstant.NUMBER_OF_CHUNKS_PER_DOCUMENT), new Cell(numberOfChunks));
                addRow(new LabelCell(LabelConstant.CHUNK_SIZE), new Cell(chunksSize));
            }
        };
    }

    public static class BagOfWordsWithReplacementChunkDataPage extends DocumentDataPage
    {
        private final int numChunks;
        private final int chunkSize;

        public BagOfWordsWithReplacementChunkDataPage(int numChunks, int chunkSize)
        {
            this.numChunks = numChunks;
            this.chunkSize = chunkSize;
        }

        @Override
        public List<Cell> generateExtraDocumentDetails(Document document)
        {
            if (document.getNumWords() <= 0)
            {
                return List.of(
                        new Cell("Document skipped"),
                        new Cell("Document skipped")
                );
            }
            return List.of(
                    new Cell(numChunks),
                    new Cell(chunkSize)
            );
        }

        @Override
        public List<Cell> generateExtraLabels()
        {
            return List.of(
                    new LabelCell(LabelConstant.NUMBER_OF_CHUNKS),
                    new LabelCell(LabelConstant.CHUNK_SIZE)
            );
        }

        @Override
        public void addRest()
        {

        }
    }
}
