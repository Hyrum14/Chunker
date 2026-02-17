package app.model.service;

import app.model.cache.Cache;
import app.model.service.tasks.base.TaskHandler;
import app.model.service.tasks.document.AddDocumentTask;
import app.model.service.tasks.document.AddPhraseListTask;
import app.presenter.observers.base.SimpleServiceObserver;
import app.presenter.observers.document.AddDocumentsObserver;
import app.presenter.observers.document.AddPhraseListObserver;
import utils.UtilityManager;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.FunctionPhrase;

import java.io.File;
import java.util.Set;

public class DocumentService
{
    Logger logger;

    public DocumentService(Logger logger)
    {
        this.logger = logger;
    }


    public void addDocuments(AddDocumentsObserver observer, Set<Document> documents, Set<FunctionPhrase> possiblePhrases, boolean forceUpdate)
    {
        for (Document document : documents)
        {
            AddDocumentTask task = new AddDocumentTask(
                    new AddDocumentHandler(
                            observer.createMemberObserver(),
                            logger
                    ),
                    logger,
                    document,
                    forceUpdate,
                    possiblePhrases,
                    Cache.getInstance().getFactory().getDocumentDao(),
                    Cache.getInstance().getFactory().getOccurrenceDao(),
                    Cache.getInstance().getFactory().getDelimiterDao()
            );

            UtilityManager.getExecutor().execute(task);
        }
    }

    public void addPhraseList(AddPhraseListObserver observer, File file)
    {
        AddPhraseListTask task = new AddPhraseListTask(
                new AddPhraseListHandler(
                        observer,
                        logger
                ),
                logger,
                file
        );
        UtilityManager.getExecutor().execute(task);
    }


    public static class AddDocumentHandler extends TaskHandler<Document>
    {
        public AddDocumentHandler(SimpleServiceObserver<Document> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return Document.DOCUMENT;
        }
    }

    public static class AddPhraseListHandler extends TaskHandler<Set<FunctionPhrase>>
    {

        public AddPhraseListHandler(SimpleServiceObserver<Set<FunctionPhrase>> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return FunctionPhrase.FUNCTION_PHRASE;
        }
    }
}
