package utils.objects;

import app.model.cache.Cache;
import app.model.chunking.ChunkingStrategy;
import utils.objects.chunk.Chunk;

import java.time.LocalDateTime;
import java.util.*;

public class Evaluation implements Comparable<Evaluation>
{
    public static final String SAVE_EVALUATION = "SAVE_EVALUATION";
    public static final String RECREATE_CHUNKS = "RECREATE_CHUNKS";
    public static final String GENERATE_CHUNKS = "GENERATE_CHUNKS";
    public static final String COUNT_CHUNK = "COUNT_CHUNK";
    private final String name;
    private final ChunkingStrategy chunkingStrategy;
    LocalDateTime time;
    List<Comparator<Evaluation>> comparators = new ArrayList<>();
    private CountManager<String> wordCountManager;
    private CountManager<FunctionPhrase> phraseCountManager;
    private CountManager<String> partOfSpeechCountManager;
    private CountManager<String> specialPhraseCountManager;
    private State state;
    private Set<Chunk> chunks;
    private List<Integer> documentIds;
    private Set<Document> documents;

    public Evaluation(String name, ChunkingStrategy chunkingStrategy, List<Integer> documentIds)
    {
        chunks = new TreeSet<>();
        this.name = name;
        this.chunkingStrategy = chunkingStrategy;
        this.state = State.CREATED;
        this.documentIds = documentIds;
        documents = new TreeSet<>(Comparator.comparing(Document::getFileName));
        documents.addAll(Cache.getInstance().getFactory().getDocumentDao().get(documentIds.stream().toList()));
        this.wordCountManager = new CountManager<>();
        this.phraseCountManager = new CountManager<>();
        this.partOfSpeechCountManager = new CountManager<>();
        this.specialPhraseCountManager = new CountManager<>();
    }

    public int getNumChunks()
    {
        return chunks.size();
    }

    public int getNumDocuments()
    {
        return documents.size();
    }

    public int getTotalCountOfAllWords()
    {
        return wordCountManager.getTotalByItem().getTotal();
    }

    public CountManager<String> getWordCountManager()
    {
        return wordCountManager;
    }

    public CountManager<FunctionPhrase> getPhraseCountManager()
    {
        return phraseCountManager;
    }

    public CountManager<String> getPartOfSpeechCountManager()
    {
        return partOfSpeechCountManager;
    }

    public CountManager<String> getSpecialPhraseCountManager()
    {
        return specialPhraseCountManager;
    }

    public List<Integer> getDocumentIds()
    {
        return documentIds;
    }

    public List<Document> getDocuments()
    {
        return documents.stream().toList();
    }

    public Set<Chunk> getChunks()
    {
        return chunks;
    }

    public void setChunks(Set<Chunk> chunks)
    {
        if (chunks == null)
            return;
        this.chunks.addAll(chunks);
        state = State.GENERATED_CHUNKS;
    }

    public void setComplete()
    {
        time = LocalDateTime.now();
        state = State.COMPLETE;
    }

    public String getName()
    {
        return name;
    }

    public ChunkingStrategy getChunkingStrategy()
    {
        return chunkingStrategy;
    }

    public State getState()
    {
        return state;
    }

    public void setFailed()
    {
        state = State.FAILED;
    }

    public LocalDateTime getTime()
    {
        return time;
    }

    public synchronized void addCountResult(ChunkCountResult result)
    {
        wordCountManager.add(result.getChunk(), result.getWordCountManager());
        phraseCountManager.add(result.getChunk(), result.getPhraseCountManager());
        partOfSpeechCountManager.add(result.getChunk(), result.getPartOfSpeechCountManager());
        specialPhraseCountManager.add(result.getChunk(), result.getSpecialPhraseCountManager());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return Objects.equals(name, that.name) && Objects.equals(chunkingStrategy, that.chunkingStrategy) && Objects.equals(time, that.time) && Objects.equals(wordCountManager, that.wordCountManager) && Objects.equals(phraseCountManager, that.phraseCountManager) && Objects.equals(partOfSpeechCountManager, that.partOfSpeechCountManager) && Objects.equals(specialPhraseCountManager, that.specialPhraseCountManager) && state == that.state && Objects.equals(chunks, that.chunks) && Objects.equals(documentIds, that.documentIds) && Objects.equals(documents, that.documents);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, chunkingStrategy, time, wordCountManager, phraseCountManager, partOfSpeechCountManager, specialPhraseCountManager, state, chunks, documentIds, documents);
    }

    @Override
    public int compareTo(Evaluation o)
    {
        if (comparators.isEmpty())
        {
            comparators.add(Comparator.comparing(Evaluation::getTime));
            comparators.add(Comparator.comparing(Evaluation::getName));
            comparators.add(Comparator.comparing(Evaluation::getTotalCountOfAllWords));
            comparators.add(Comparator.comparing(Evaluation::getNumDocuments));
            comparators.add(Comparator.comparing(Evaluation::getNumChunks));
        }

        for (Comparator<Evaluation> comparator : comparators)
        {
            int result = comparator.compare(this, o);
            if (result != 0)
            {
                return result;
            }
        }
        return 0; // they are the same
    }


    public enum State
    {
        CREATED("CREATED"),
        GENERATED_CHUNKS("GENERATED_CHUNKS"),
        COMPLETE("COMPLETE"), FAILED("FAILED");

        final private String name;

        State(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
