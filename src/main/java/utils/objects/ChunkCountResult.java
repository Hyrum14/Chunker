package utils.objects;

import utils.objects.chunk.Chunk;

import java.util.Objects;

public class ChunkCountResult
{
    private CountResult<String> wordCountManager;
    private CountResult<FunctionPhrase> phraseCountManager = new CountResult<>();
    private CountResult<String> partOfSpeechCountManager = new CountResult<>();
    private CountResult<String> specialPhraseCountManager = new CountResult<>();
    private Chunk chunk;


    public ChunkCountResult(CountResult<String> wordCountManager, CountResult<FunctionPhrase> allPhrases, Chunk chunk)
    {
        this.wordCountManager = wordCountManager;
        this.chunk = chunk;
        for (FunctionPhrase phrase : allPhrases.getKeys())
        {
            if (phrase.hasPartOfSpeech())
            {
                partOfSpeechCountManager.add(phrase.partOfSpeech, allPhrases.get(phrase));
                phraseCountManager.add(phrase, allPhrases.get(phrase)); // note
            } else
            {
                specialPhraseCountManager.add(phrase.getPhraseString(), allPhrases.get(phrase));
            }
        }
    }

    public CountResult<String> getWordCountManager()
    {
        return wordCountManager;
    }

    public CountResult<FunctionPhrase> getPhraseCountManager()
    {
        return phraseCountManager;
    }

    public CountResult<String> getPartOfSpeechCountManager()
    {
        return partOfSpeechCountManager;
    }

    public CountResult<String> getSpecialPhraseCountManager()
    {
        return specialPhraseCountManager;
    }

    public Chunk getChunk()
    {
        return chunk;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCountResult that = (ChunkCountResult) o;
        return Objects.equals(wordCountManager, that.wordCountManager) && Objects.equals(phraseCountManager, that.phraseCountManager) && Objects.equals(partOfSpeechCountManager, that.partOfSpeechCountManager) && Objects.equals(specialPhraseCountManager, that.specialPhraseCountManager) && Objects.equals(chunk, that.chunk);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(wordCountManager, phraseCountManager, partOfSpeechCountManager, specialPhraseCountManager, chunk);
    }

    @Override
    public String toString()
    {
        return chunk.getChunkName();
    }
}
