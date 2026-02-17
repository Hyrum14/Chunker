package app.model.service.tasks.document;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.DocumentDao;
import app.model.dao.interfaces.OccurrenceDao;
import app.model.service.tasks.base.TaskHandler;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.FunctionPhrase;

import java.util.Set;

public class AddDocumentTask extends ParseDocumentTask
{
    public AddDocumentTask(TaskHandler<Document> observer, Logger logger, Document document, boolean forceUpdate, Set<FunctionPhrase> possiblePhrases, DocumentDao documentDao, OccurrenceDao occurrenceDao, DelimiterDao delimiterDao)
    {
        super(
                observer, logger, document,
                forceUpdate, possiblePhrases,
                documentDao, occurrenceDao,
                delimiterDao
        );
    }

    @Override
    protected boolean evaluate()
    {
        if (!validate())
            return false;
        return parse();
    }
}




