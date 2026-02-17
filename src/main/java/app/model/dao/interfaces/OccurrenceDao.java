package app.model.dao.interfaces;

import utils.objects.CountResult;
import utils.objects.FunctionPhrase;
import utils.objects.PhraseOccurrence;
import utils.objects.Token;
import utils.objects.chunk.Chunk;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OccurrenceDao
{
    void add(Token toAdd);

    Token getOccurrence(int documentId, int index);

    PhraseOccurrence getPhraseOccurrence(int documentId, int index);

    CountResult<String> countWords(Chunk chunk);

    Collection<Token> getTable();

    void addPhraseOccurrence(PhraseOccurrence toAdd);

    CountResult<FunctionPhrase> countFunctionPhrases(Chunk chunk);

    Set<FunctionPhrase> getPossiblePhrases(List<Integer> documents);


    void addPossiblePhrases(Set<FunctionPhrase> phrases, Integer docId);

    void addPossibleWords(Set<String> words, Integer docId);

    void clear(Integer documentId);

    int pickRandomInt(int documentSize);
}