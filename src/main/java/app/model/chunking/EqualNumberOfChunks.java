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
import utils.objects.chunk.Chunk;
import utils.objects.chunk.ContinuousChunk;

import java.util.List;
import java.util.Map;

public class EqualNumberOfChunks extends ChunkingStrategy
{
    private final int numberOfChunks;

    public EqualNumberOfChunks(int numberOfChunks, Logger chunkingStrategyLogger)
    {
        super(chunkingStrategyLogger);
        this.numberOfChunks = numberOfChunks;
    }

    @Override
    public void generateChunks() throws TaskException
    {

        for (Document document : documents)
        {

            if (document.getNumWords() < numberOfChunks)
            {
                warningHandler.handleWarning(String.format("(Strategy: %s) Skipped Document %s: { DocSize: %d < %d (number of chunks) }",
                        getName(),
                        document.getFileName(),
                        document.getNumWords(),
                        numberOfChunks));
                continue;
            }

            createChunk(document, numberOfChunks);
        }
    }

    @Override
    public String getName()
    {
        return "Equal Number of Chunks N = " + numberOfChunks;
    }

    @Override
    public DocumentDataPage getDocumentDataPage()
    {
        return new EqualNumChunksDataPage(docIdToNumChunks, docIdToChunkSize);
    }

    @Override
    public ChunkDataPage getChunkDataPage()
    {
        return new ChunkDataPage()
        {
            @Override
            public List<Cell> generateExtraChunkDetails(Chunk chunk)
            {
                return List.of(
                        new Cell(((ContinuousChunk) chunk).getStartIndex() + 1),
                        new Cell(((ContinuousChunk) chunk).getEndIndex() + 1)
                );
            }

            @Override
            public List<Cell> generateExtraLabels()
            {
                return List.of(
                        new LabelCell(LabelConstant.START_INDEX),
                        new LabelCell(LabelConstant.END_INDEX)
                );
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
                addRow(new LabelCell(LabelConstant.CHUNKING_STRATEGY), new Cell("Equal Number of Chunks"));
                addRow(new LabelCell(LabelConstant.NUMBER_OF_CHUNKS_PER_DOCUMENT), new Cell(numberOfChunks));
            }
        };
    }

    public static class EqualNumChunksDataPage extends DocumentDataPage
    {
        private final Map<Integer, Integer> docIdToNumChunks;
        private final Map<Integer, Integer> docIdToChunkSize;

        public EqualNumChunksDataPage(Map<Integer, Integer> docIdToNumChunks, Map<Integer, Integer> docIdToChunkSize)
        {
            this.docIdToNumChunks = docIdToNumChunks;
            this.docIdToChunkSize = docIdToChunkSize;
        }

        @Override
        public List<Cell> generateExtraDocumentDetails(Document document)
        {
            int offset = (document.getNumWords() % docIdToChunkSize.getOrDefault(document.getId(), -1) == 0 ? 0 : 1);
            return List.of(
                    (docIdToNumChunks.containsKey(document.getId()) ?
                            new Cell(docIdToNumChunks.get(document.getId())) :
                            new Cell("Document was skipped or otherwise presented an issue")),
                    (docIdToChunkSize.containsKey(document.getId()) ?
                            new Cell(docIdToChunkSize.get(document.getId())) :
                            new Cell("Document was skipped or otherwise presented an issue")),
                    (docIdToChunkSize.containsKey(document.getId()) ?
                            new Cell(docIdToChunkSize.get(document.getId()) + offset) :
                            new Cell("Document was skipped or otherwise presented an issue"))
            );
        }

        @Override
        public List<Cell> generateExtraLabels()
        {
            return List.of(
                    new LabelCell(LabelConstant.NUMBER_OF_CHUNKS),
                    new LabelCell(LabelConstant.MIN_CHUNK_SIZE),
                    new LabelCell(LabelConstant.MAX_CHUNK_SIZE)
            );
        }

        @Override
        public void addRest()
        {

        }
    }

}
