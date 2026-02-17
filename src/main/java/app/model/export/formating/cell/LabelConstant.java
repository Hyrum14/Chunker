package app.model.export.formating.cell;

public enum LabelConstant
{
    CHUNK_NAME("Chunk Name"),
    DOCUMENT_NAME("Document Name"),
    CHUNK_ID("Chunk ID"),
    DOCUMENT_ID("Document ID"),
    EVALUATION_NAME("Evaluation Name"),
    CHUNKING_STRATEGY("Chunking Strategy"),
    DATE("Date"),
    TIME("Time"),
    PATH("Path"),
    NUMBER_OF_WORDS("Number of words"),
    START_INDEX("Start Word"),
    END_INDEX("End Word"),
    SIZE("Size"),
    DATE_ADDED("Date Added"),
    TIME_ADDED("Time Added"),
    DOCUMENT_DESCRIPTION_HEADING("Document Descriptions"),
    CHUNK_DESCRIPTION_HEADING("Chunk Descriptions"),
    PARENT_DOCUMENT_NAME("Parent Document Name"),
    SUM("Sum of all Chunks"),
    PART_OF_SPEECH("Part of Speech"),
    SUM_ACROSS_ALL_CHUNKS("Total Occurrences in Corpus"),
    WORD("Word"),
    AGGREGATE_WORD("Function Word Total"),
    PHRASE("Phrase"),
    DATE_LAST_CHANGED("Date Last Changed"),
    CHUNK_SIZE("Chunk Size"),
    NUMBER_OF_CHUNKS("Number of Chunks"),
    VARIANCE_FACTOR("Variance Factor"),
    TIME_LAST_CHANGED("Time Last Changed"),
    PARENT_DIRECTORY("Parent Directory"),
    FILE_NAME("File Name"),
    NUMBER_OF_DOCS("Number of Documents"),
    NUMBER_OF_UNIQUE_WORDS("Number of Unique Words"),
    NUMBER_OF_FUNCTION_WORDS("Number of Function Words"),
    NUMBER_OF_SPECIAL_PHRASES("Number of Special Phrases"),
    NUMBER_OF_PART_OF_SPEECH_CATEGORIES("Number of Part of Speech Categories"),
    BASE_DOC_NAME("Base Document Name"),
    BASE_DOCUMENT_ID("Base Document ID"),
    BASE_NUMBER_CHUNKS("Base Number of Chunks"),
    STANDARDIZED_COUNT_FACTOR("Counts per XXXX Words"),
    MIN_CHUNK_SIZE("Min Chunk Size"),
    MAX_CHUNK_SIZE("Max Chunk Size"),
    NUMBER_OF_FUNCTION_WORDS_CHECKED("Number of Function Words Checked"),
    NUMBER_OF_SPECIAL_PHRASES_CHECKED("Number of Special Phrases Checked"),
    NUMBER_OF_PART_OF_SPEECH_CATEGORIES_CHECKED("Number of Part of Speech Categories"),
    NUMBER_OF_UNIQUE_WORDS_OCCURRENCES("Number of Unique Word Occurrences"),
    NUMBER_OF_FUNCTION_WORDS_OCCURRENCES("Number of Function Word Occurrences"),
    NUMBER_OF_SPECIAL_PHRASES_OCCURRENCES("Number of Special Phrase Occurrences"),
    NUMBER_OF_CHUNKS_PER_DOCUMENT("Number of Chunks Per Document"),
    CHUNK("Chunk"),
    TEXT_CHUNKER("Text Chunker"), TARGET_CHUNK_SIZE("Target Chunk Size");


    private final String displayName;

    LabelConstant(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
