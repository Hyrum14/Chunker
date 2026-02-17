package app.model.service.tasks.evaluation;

import app.model.dao.interfaces.DocumentDao;
import app.model.service.tasks.base.Task;
import app.model.service.tasks.base.TaskHandler;
import app.model.chunking.ChunkingStrategy;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.chunk.Chunk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateChunksTask extends Task<Set<Chunk>>
{

    private DocumentDao documentDao;
    private List<Integer> documentIds;
    private ChunkingStrategy strategy;

    public GenerateChunksTask(TaskHandler<Set<Chunk>> handler, Logger logger, List<Integer> documentIds, ChunkingStrategy strategy, DocumentDao documentDao)
    {
        super(handler, logger);
        this.documentDao = documentDao;
        this.documentIds = documentIds;
        this.strategy = strategy;
    }

    @Override
    protected boolean evaluate() throws TaskException
    {
        List<Document> documents = documentDao.get(documentIds);

        List<Chunk> chunks = strategy.generate(documents, message -> handler.handleWarning(message));

        setResult(new HashSet<>(chunks));
        return chunks != null && !chunks.isEmpty();
    }
}
