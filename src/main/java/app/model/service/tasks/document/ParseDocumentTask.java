package app.model.service.tasks.document;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.DocumentDao;
import app.model.dao.interfaces.OccurrenceDao;
import app.model.parsing.*;
import app.model.service.tasks.base.TaskHandler;
import utils.logging.Logger;
import utils.objects.*;

import java.io.IOException;
import java.util.Set;

public abstract class ParseDocumentTask extends ValidateDocumentTask
        implements DocumentParser.DocumentStorage, PhraseParser.PhraseStorage {
    private static final String DOCUMENT_NOT_VALIDATED = "The document was not validated, before it is parsed it MUST be validated";
    protected OccurrenceDao occurrenceDao;
    protected DelimiterDao delimiterDao;
    protected int index = 0;
    protected PhraseParser phraseParser;
    protected String prevWord = null;

    public ParseDocumentTask(TaskHandler<Document> observer, Logger logger, Document document, boolean forceUpdate,
            Set<FunctionPhrase> possiblePhrases, DocumentDao documentDao, OccurrenceDao occurrenceDao,
            DelimiterDao delimiterDao) {
        super(observer, logger, document, forceUpdate, documentDao);
        this.occurrenceDao = occurrenceDao;
        this.delimiterDao = delimiterDao;
        this.phraseParser = new PhraseParser(possiblePhrases, this, null);
    }

    protected boolean parse() {
        if (reader == null || documentId == null) {
            handler.handleFailure(document, DOCUMENT_NOT_VALIDATED);
            return false;
        }
        try {
            documentDao.clear(documentId);
            delimiterDao.clear(documentId);
            occurrenceDao.clear(documentId);
            new DocumentParser().parse(new FileDocumentProvider(reader), this);
            if (document.getNumWords() == 0) {
                handler.handleFailure(document,
                        String.format("Document %s failed, must have at least one word", document.getFileName()));
                return false;
            }
            documentDao.add(document);
            setResult(document);
            occurrenceDao.addPossiblePhrases(phraseParser.getPhraseLedger().keySet(), documentId);
        } catch (DocumentParsingException | IOException ex) {
            ex.printStackTrace();
            handler.handleException(document, ex);
            return false;
        }

        return true;
    }

    @Override
    public void storeDelimiter(String contents) {
        Delimiter delimiter = new Delimiter(
                index,
                documentId,
                contents);
        delimiterDao.add(delimiter);
        if (prevWord != null) {
            // I know this is ugly but at least it works now, blame Walter ~Hyrum Reynolds
            index--;
            phraseParser.parseNextWord(prevWord, delimiter);
            index++;
        }
    }

    @Override
    public void storeOccurrence(String contents) {
        String root = RootConverter.convert(contents);

        Token token = new Token(
                contents,
                root,
                documentId,
                index);

        occurrenceDao.add(token);

        document.addWord();
        prevWord = root;
        index++;
    }

    @Override
    public void storePhrase(FunctionPhrase phrase, int index) {
        occurrenceDao.addPhraseOccurrence(new PhraseOccurrence(
                phrase.getPartOfSpeech(),
                phrase,
                documentId,
                index));
    }
}
