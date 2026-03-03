package app.model.service.tasks.document;

import app.model.dao.interfaces.DocumentDao;
import app.model.service.tasks.base.Task;
import app.model.service.tasks.base.TaskHandler;
import utils.logging.Logger;
import utils.objects.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Instant;
import java.util.Optional;

public abstract class ValidateDocumentTask extends Task<Document> {
    public final String DOCUMENT_ALREADY_EXISTS_IN_DATABASE = "The requested document (%s) already exists in the database";
    public final String DOCUMENT_NOT_FOUND = "The document requested could not be found or opened";

    protected Document document;
    protected DocumentDao documentDao;
    protected Integer documentId = null;
    protected BufferedReader reader = null;
    protected boolean forceUpdate;
    protected boolean isForceUpdate;

    public ValidateDocumentTask(TaskHandler<Document> observer, Logger logger, Document document, boolean forceUpdate,
            DocumentDao documentDao) {
        super(observer, logger);
        this.document = document;
        this.documentDao = documentDao;
        this.forceUpdate = forceUpdate;
    }

    public boolean validate() {
        try {
            File file = new File(document.getAbsolutePath());

            if (!file.exists() || !file.canRead()) {
                handler.handleFailure(document, DOCUMENT_NOT_FOUND);
                return false;
            }

            Optional<Integer> documentIdOption = documentDao.containsDocument(document);
            if (documentIdOption.isPresent()) {
                if (!forceUpdate && !documentDao.shouldUpdate(documentIdOption.get())) {
                    handler.handleFailure(document,
                            String.format(DOCUMENT_ALREADY_EXISTS_IN_DATABASE, document.getFileName()));
                    return false;
                }
                isForceUpdate = true;
            } else {
                documentIdOption = Optional.of(documentDao.reserveId());
                isForceUpdate = false;
            }

            document.setId(documentIdOption.get());
            document.setDateAdded(Instant.now());
            documentDao.add(document);

            documentId = documentIdOption.get();
            // open file with explicit UTF-8 charset to avoid platform defaults
            reader = new BufferedReader(new java.io.InputStreamReader(
                    new java.io.FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            handler.handleException(document, ex);
            return false;
        }

        return true;
    }
}
