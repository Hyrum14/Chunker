package app.model.chunking;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import app.model.export.formating.page.generators.ChunkDataPage;
import app.model.export.formating.page.generators.DocumentDataPage;
import app.model.export.formating.page.generators.EvaluationDataPage;
import utils.exeptions.TaskException;
import utils.logging.LogLevel;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.chunk.Chunk;
import utils.objects.chunk.ContinuousChunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualVariance extends ChunkingStrategy {
    private int base_number_of_chunks;
    private int base_doc_id;
    private String base_doc_name;

    private Map<Integer, Double> docIdToVariance;
    private int baseNumberOfWords;

    public EqualVariance(int base_number_of_chunks, int base_doc_id, String base_doc_name,
            Logger chunkingStrategyLogger) {
        super(chunkingStrategyLogger);
        this.base_number_of_chunks = base_number_of_chunks;
        this.base_doc_id = base_doc_id;
        if (base_doc_id < 0 || base_doc_name == null)
            this.base_doc_name = "<smallest>";
        else
            this.base_doc_name = base_doc_name;
        docIdToVariance = new HashMap<>();

    }

    @Override
    protected void generateChunks() throws TaskException {
        if (base_number_of_chunks < 1)
            throw new TaskException("Base document requires at least 1 chunk");

        baseNumberOfWords = Integer.MAX_VALUE;
        if (base_doc_id < 0)
            documents.forEach(document -> {
                if (baseNumberOfWords > document.getNumWords()) {
                    baseNumberOfWords = document.getNumWords();
                    base_doc_name = document.getFileName();
                    base_doc_id = document.getId();
                }
            });
        else
            documents.forEach(document -> {
                if (document.getId() == base_doc_id && document.getId() > 0) {
                    baseNumberOfWords = document.getNumWords();
                    // base_doc_id = document.getId();
                    // base_doc_name = document.getFileName();
                }
            });

        if (baseNumberOfWords < base_number_of_chunks)
            throw new TaskException(
                    "Base document must have at least the same number of words as the base number of chunks");

        if (baseNumberOfWords == 1)
            throw new TaskException("Base document must have 2 or more words");

        for (Document document : documents) {

            int numChunks;
            if (document.getNumWords() < baseNumberOfWords) {
                numChunks = 1;
            } else if (document.getNumWords() == baseNumberOfWords) {
                numChunks = base_number_of_chunks;
            } else {
                numChunks = (int) calculateNumChunks(base_number_of_chunks, baseNumberOfWords, document.getNumWords(),
                        document.getFileName());
            }
            docIdToVariance.put(document.getId(), calculateVariance(numChunks, document.getNumWords()));
            chunkingStrategyLogger.log(LogLevel.TRACE, String.format("doc_%d split in %d chunks, variance: %f",
                    document.getId(), numChunks, docIdToVariance.get(document.getId())));

            createChunk(document, numChunks);
        }
    }

    public double calculateVariance(int numChunks, int totalWords) {

        if (totalWords <= 1) {
            // chunkingStrategyLogger.log(LogLevel.WARNING, String.format("total number of
            // words for document %s is <=1, only created 1 chunk for it", documentName));
            // warningHandler.handleWarning(String.format("total number of words for
            // document %s is <=1, only created 1 chunk for it", documentName));
            return 0;
        }
        double top = numChunks - 1;
        double bottom = totalWords - 1;
        chunkingStrategyLogger.log(LogLevel.VALUE, "Top: " + top);
        chunkingStrategyLogger.log(LogLevel.VALUE, "Bottom: " + bottom);
        return top / bottom;
    }

    public long calculateNumChunks(int baseNumberChunks, int baseTotalWords, int currentDocumentTotalWords,
            String documentName) {
        long result;
        if (baseNumberChunks == 1)
            result = 1;
        else {
            double resultDouble = (double) 1
                    + ((double) (currentDocumentTotalWords - 1) * calculateVariance(baseNumberChunks, baseTotalWords));
            result = Math.round(resultDouble);
        }
        return result;
    }

    @Override
    public String getName() {
        return ("Equal Variance");
    }

    @Override
    public DocumentDataPage getDocumentDataPage() {

        return new EqualVarianceEvaluationDataPage(docIdToVariance, docIdToNumChunks, docIdToChunkSize);
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
                addRow(new LabelCell(LabelConstant.CHUNKING_STRATEGY), new Cell("Equal Variance"));
                addRow(new LabelCell(LabelConstant.BASE_DOC_NAME), new Cell(base_doc_name));
                addRow(new LabelCell(LabelConstant.BASE_NUMBER_CHUNKS), new Cell(base_number_of_chunks));
                addRow(new LabelCell(LabelConstant.BASE_DOCUMENT_ID), new Cell(base_doc_id));
            }
        };
    }

    public static class EqualVarianceEvaluationDataPage extends DocumentDataPage {
        private final Map<Integer, Double> docIdToVariance;
        private final Map<Integer, Integer> docIdToNumChunks;
        private final Map<Integer, Integer> docIdToChunkSize;

        public EqualVarianceEvaluationDataPage(Map<Integer, Double> docIdToVariance,
                Map<Integer, Integer> docIdToNumChunks, Map<Integer, Integer> docIdToChunkSize) {
            this.docIdToVariance = docIdToVariance;
            this.docIdToNumChunks = docIdToNumChunks;
            this.docIdToChunkSize = docIdToChunkSize;
        }

        @Override
        public List<Cell> generateExtraDocumentDetails(Document document) {
            int numChunksForDoc = docIdToNumChunks.getOrDefault(document.getId(), 1);
            int offset = (document.getNumWords() % numChunksForDoc == 0 ? 0 : 1);
            return List.of(
                    (docIdToVariance.containsKey(document.getId()) ? new Cell(docIdToVariance.get(document.getId()))
                            : new Cell("Document was skipped or otherwise presented an issue")),
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
                    new LabelCell(LabelConstant.VARIANCE_FACTOR),
                    new LabelCell(LabelConstant.NUMBER_OF_CHUNKS),
                    new LabelCell(LabelConstant.MIN_CHUNK_SIZE),
                    new LabelCell(LabelConstant.MAX_CHUNK_SIZE));
        }

        @Override
        public void addRest() {

        }
    }
}
