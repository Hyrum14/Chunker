package app.model.parsing;

import utils.logging.DummyLogger;
import utils.logging.LogLevel;
import utils.logging.Logger;
import utils.objects.Delimiter;
import utils.objects.FunctionPhrase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PhraseParser
{
    private final PhraseStorage phraseStorage;
    Map<FunctionPhrase, Integer> phraseLedger;
    Logger logger;
    private int index;

    public PhraseParser(Set<FunctionPhrase> possiblePhrases, PhraseStorage phraseStorage, Logger logger)
    {
        this.phraseStorage = phraseStorage;
        this.phraseLedger = new HashMap<>();
        this.logger = Objects.requireNonNullElseGet(logger, DummyLogger::new);

        if (possiblePhrases != null)
        {
            for (FunctionPhrase phrase : possiblePhrases)
            {
                this.phraseLedger.put(phrase, 0);
            }
        }
        this.index = 0;
    }

    public void parseNextWord(String word, Delimiter nextDelimiter)
    {
        for (Map.Entry<FunctionPhrase, Integer> candidatePhrase : phraseLedger.entrySet())
        {
            if (
                    candidatePhrase.getValue() < (candidatePhrase.getKey().size() - 1) && // not currently the last item in the entry
                            nextDelimiter.invalidatesPhrase(logger) // check if the delimiter splits a phrase in half
            )
            {
                candidatePhrase.setValue(0);
            } else if (candidatePhrase.getKey().toList().get(candidatePhrase.getValue()).equals(word))
            {
                candidatePhrase.setValue(candidatePhrase.getValue() + 1);
            } else
            {
                candidatePhrase.setValue(0);
            }
        }
        flushCompletedPhrases();
        index++;

    }

    private Set<FunctionPhrase> flushCompletedPhrases()
    {
        Set<FunctionPhrase> completedPhrases = phraseLedger.entrySet().stream()
                .filter(entry -> entry.getKey().toList().size() == entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        for (FunctionPhrase phrase : completedPhrases)
        {
            // yellow makes mellow
            // 5 - 3
            int phraseStart = index - (phrase.size() - 1);
            logger.log(LogLevel.TRACE, String.format("index = %d, phrase size = %d, phraseStart = %d", index, phrase.size(), phraseStart));

            phraseStorage.storePhrase(phrase, phraseStart);
            phraseLedger.put(phrase, 0); // replace the base phrase in the code with the new phrase
        }
        // phrase: one two three
        // len: 3

        // input:
        // one two three
        // 2 - (3-1) => 0

        // input:
        // zero one two three
        // 3 - (3-1) => 1

        return completedPhrases;
    }

    public Map<FunctionPhrase, Integer> getPhraseLedger()
    {
        return phraseLedger;
    }

    public interface PhraseStorage
    {
        void storePhrase(FunctionPhrase phrase, int index);
    }


    // example for use:

    // Ledger
    // count : phrase
    // 0 : and it came to pass
    // 0 : for
    // 0 : about it


    // File
    // and about it

    // word by word
    // i0 : and

    // 1 : and it came to pass
    // 0 : for
    // 0 : about it

    // word by word
    // i1 : about

    // 0 : and it came to pass
    // 0 : for
    // 1 : about it


    // word by word
    // i2 : it

    // 0 : and it came to pass
    // 0 : for
    // 2 : about it

    // create an occurrence of the phrase at index (2 - (2 - 1))
    // occ: pos 1 "about it"


    // 0 : and it came to pass
    // 0 : for
    // 0 : about it
}