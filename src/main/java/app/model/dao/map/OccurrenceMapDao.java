package app.model.dao.map;

import app.model.dao.interfaces.OccurrenceDao;
import utils.containers.Pair;
import utils.objects.CountResult;
import utils.objects.FunctionPhrase;
import utils.objects.PhraseOccurrence;
import utils.objects.Token;
import utils.objects.chunk.Chunk;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OccurrenceMapDao implements OccurrenceDao
{
    Random random = new Random(Instant.now().getEpochSecond());
    Map<Pair<Integer, String>, Set<Token>> wordData = new ConcurrentHashMap<>();
    Map<Pair<Integer, Integer>, Token> wordDataSorted = new ConcurrentHashMap<>();
    Map<Pair<Integer, Integer>, PhraseOccurrence> phraseDataSorted = new ConcurrentHashMap<>();
    Map<Pair<Integer, FunctionPhrase>, Set<PhraseOccurrence>> phraseData = new ConcurrentHashMap<>();
    Map<Integer, Set<FunctionPhrase>> possiblePhrases = new ConcurrentHashMap<>();
    Map<Integer, Set<String>> possibleWords = new ConcurrentHashMap<>();

    @Override
    public synchronized void clear(Integer documentId)
    {
        for (Pair<Integer, String> item : wordData.keySet())
        {
            if (item.getFirst().equals(documentId))
                wordData.remove(item);
        }
        for (Pair<Integer, Integer> item : wordDataSorted.keySet())
        {
            if (item.getFirst().equals(documentId))
                wordDataSorted.remove(item);
        }
        for (Pair<Integer, Integer> item : phraseDataSorted.keySet())
        {
            if (item.getFirst().equals(documentId))
                phraseDataSorted.remove(item);
        }
        for (Pair<Integer, FunctionPhrase> item : phraseData.keySet())
        {
            if (item.getFirst().equals(documentId))
                phraseData.remove(item);
        }
        for (Integer item : possiblePhrases.keySet())
        {
            if (item.equals(documentId))
                possiblePhrases.remove(item);
        }
        for (Integer item : possibleWords.keySet())
        {
            if (item.equals(documentId))
                possibleWords.remove(item);
        }
    }

    @Override
    public synchronized int pickRandomInt(int documentSize)
    {
        return random.nextInt(documentSize);
    }

    @Override
    public synchronized void add(Token toAdd)
    {

        wordData.putIfAbsent(new Pair<>(toAdd.getDocumentId(), toAdd.getRoot()), ConcurrentHashMap.newKeySet());
        wordData.get(new Pair<>(toAdd.getDocumentId(), toAdd.getRoot())).add(toAdd);

        wordDataSorted.put(new Pair<>(toAdd.getDocumentId(), toAdd.getIndex()), toAdd);

        possibleWords.putIfAbsent(toAdd.getDocumentId(), ConcurrentHashMap.newKeySet());
        possibleWords.get(toAdd.getDocumentId()).add(toAdd.getRoot());

    }

    @Override
    public Token getOccurrence(int documentId, int index)
    {
        return wordDataSorted.get(new Pair<>(documentId, index));
    }

    @Override
    public PhraseOccurrence getPhraseOccurrence(int documentId, int index)
    {
        return phraseDataSorted.get(new Pair<>(documentId, index));
    }

    @Override
    public CountResult<String> countWords(Chunk chunk)
    {
        CountResult<String> result = new CountResult<>();
        for (String root : possibleWords.get(chunk.getDocumentId()))
        {
            wordData.getOrDefault(new Pair<>(chunk.getDocumentId(), root), ConcurrentHashMap.newKeySet())
                    .forEach(occurrence ->
                    {
                        if (chunk.isInChunk(occurrence))
                            result.add(occurrence.getRoot(), chunk.getNumOccurrences(occurrence));
                    });
        }
        return result;
    }

    @Override
    public Collection<Token> getTable()
    {
        Set<Token> output = ConcurrentHashMap.newKeySet();
        for (Set<Token> tokens : wordData.values())
            output.addAll(tokens);
        return output;
    }

    @Override
    public void addPhraseOccurrence(PhraseOccurrence toAdd)
    {
        synchronized (phraseData)
        {
            phraseData.putIfAbsent(new Pair<>(toAdd.getDocumentId(), toAdd.getPhrase()), ConcurrentHashMap.newKeySet());
            phraseData.get(new Pair<>(toAdd.getDocumentId(), toAdd.getPhrase())).add(toAdd);
        }
        phraseDataSorted.put(new Pair<>(toAdd.getDocumentId(), toAdd.getIndex()), toAdd);
        synchronized (possiblePhrases)
        {
            possiblePhrases.putIfAbsent(toAdd.getDocumentId(), ConcurrentHashMap.newKeySet());
            possiblePhrases.get(toAdd.getDocumentId()).add(toAdd.getPhrase());
        }
    }

    @Override
    public CountResult<FunctionPhrase> countFunctionPhrases(Chunk chunk)
    {
        CountResult<FunctionPhrase> result = new CountResult<>();
        for (FunctionPhrase phrase : possiblePhrases.get(chunk.getDocumentId()))
        {
            phraseData.getOrDefault(new Pair<>(chunk.getDocumentId(), phrase), ConcurrentHashMap.newKeySet())
                    .forEach(phraseOccurrence ->
                    {
                        if (chunk.isInChunk(phraseOccurrence))
                            result.add(phraseOccurrence.getPhrase(), chunk.getNumOccurrences(phraseOccurrence));
                    });
        }
        return result;
    }

    @Override
    public Set<FunctionPhrase> getPossiblePhrases(List<Integer> documents)
    {
        Set<FunctionPhrase> output = new HashSet<>();
        if (documents == null)
            throw new NullPointerException("BEEEEES");
        documents.forEach(doc ->
        {
            if (doc != null && possiblePhrases.containsKey(doc))
                output.addAll(possiblePhrases.get(doc));
        });
        return output;
    }


    @Override
    public void addPossiblePhrases(Set<FunctionPhrase> phrases, Integer docId)
    {
        possiblePhrases.putIfAbsent(docId, new HashSet<>());
        possiblePhrases.get(docId).addAll(phrases);
    }

    @Override
    public void addPossibleWords(Set<String> words, Integer docId)
    {
        possibleWords.putIfAbsent(docId, new HashSet<>());
        possibleWords.get(docId).addAll(words);
    }


}