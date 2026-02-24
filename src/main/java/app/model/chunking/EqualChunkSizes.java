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

public class EqualChunkSizes extends ChunkingStrategy {
    private final int targetChunkSize;

    public EqualChunkSizes(int targetChunkSize, Logger chunkingStrategyLogger) {
        super(chunkingStrategyLogger);
        this.targetChunkSize = targetChunkSize;
    }

    @Override
    public void generateChunks() throws TaskException {
        for (Document document : documents) {
            int numWords = document.getNumWords();
            if (numWords <= 0)
                throw new TaskException("One of the documents contained 0 words");

            if (numWords <= targetChunkSize) {
                createChunk(document, 1);
                continue;
            }

            int smallerNumberOfChunks = Math.floorDiv(numWords, targetChunkSize);
            int largerNumberOfChunks = smallerNumberOfChunks + 1;

            double smallerChunkSize = (double) numWords / (double) smallerNumberOfChunks;
            double largerChunkSize = (double) numWords / (double) largerNumberOfChunks;

            double smallerDifference = Math.abs(smallerChunkSize - (double) targetChunkSize);
            double largerDifference = Math.abs(largerChunkSize - (double) targetChunkSize);

            int numChunks;

            if (smallerDifference <= largerDifference) {
                numChunks = smallerNumberOfChunks;
            } else {
                numChunks = largerNumberOfChunks;
            }

            createChunk(document, numChunks);
        }
    }

    @Override
    public String getName() {
        return "Equal Chunk Sizes";
    }

    @Override
    public DocumentDataPage getDocumentDataPage() {
        return new EqualChunkSizesDataPage(docIdToNumChunks, docIdToChunkSize);
    }

    @Override
    public ChunkDataPage getChunkDataPage() {
        return new ChunkDataPage() {
            @Override
            public List<Cell> generateExtraChunkDetails(Chunk chunk) {
                return List.of(
                        new Cell(((ContinuousChunk) chunk).getStartIndex() + 1),
                        new Cell(((ContinuousChunk) chunk).getEndIndex() + 1));
            }

            @Override
            public List<Cell> generateExtraLabels() {
                return List.of(
                        new LabelCell(LabelConstant.START_INDEX),
                        new LabelCell(LabelConstant.END_INDEX));
            }

            @Override
            public void addRest() {

            }
        };
    }

    @Override
    public EvaluationDataPage getMetaDataPage(int standardizedCountFactor) {
        return new EvaluationDataPage(standardizedCountFactor) {
            @Override
            public void addRest() {
                addRow(new LabelCell(LabelConstant.CHUNKING_STRATEGY), new Cell("Equal Chunk Sizes"));
                addRow(new LabelCell(LabelConstant.TARGET_CHUNK_SIZE), new Cell(targetChunkSize));
            }
        };
    }

    public static class EqualChunkSizesDataPage extends DocumentDataPage {
        private final Map<Integer, Integer> docIdToNumChunks;
        private final Map<Integer, Integer> docIdToChunkSize;

        public EqualChunkSizesDataPage(Map<Integer, Integer> docIdToNumChunks, Map<Integer, Integer> docIdToChunkSize) {
            this.docIdToNumChunks = docIdToNumChunks;
            this.docIdToChunkSize = docIdToChunkSize;
        }

        @Override
        public List<Cell> generateExtraDocumentDetails(Document document) {
            int numChunksForDoc = docIdToNumChunks.getOrDefault(document.getId(), 1);
            int offset = (document.getNumWords() % numChunksForDoc == 0 ? 0 : 1);
            return List.of(
                    (docIdToNumChunks.containsKey(document.getId()) ? new Cell(docIdToNumChunks.get(document.getId()))
                            : new Cell("Document was skipped or otherwise presented an issue")),
                    (docIdToChunkSize.containsKey(document.getId()) ? new Cell(docIdToChunkSize.get(document.getId()))
                            : new Cell("Document was skipped or otherwise presented an issue")),
                    (docIdToChunkSize.containsKey(document.getId())
                            ? new Cell(docIdToChunkSize.get(document.getId()) + offset)
                            : new Cell("Document was skipped or otherwise presented an issue")));
        }

        @Override
        public List<Cell> generateExtraLabels() {
            return List.of(
                    new LabelCell(LabelConstant.NUMBER_OF_CHUNKS),
                    new LabelCell(LabelConstant.MIN_CHUNK_SIZE),
                    new LabelCell(LabelConstant.MAX_CHUNK_SIZE));
        }

        @Override
        public void addRest() {

        }
    }
}
