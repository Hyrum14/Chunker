package app.model.service.tasks.evaluation;

import app.model.dao.interfaces.OccurrenceDao;
import app.model.service.tasks.base.Task;
import app.model.service.tasks.base.TaskHandler;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.ChunkCountResult;
import utils.objects.chunk.Chunk;

public class CountChunkTask extends Task<ChunkCountResult>
{
    protected Chunk chunk;
    protected OccurrenceDao occurrenceDao;

    public CountChunkTask(TaskHandler<ChunkCountResult> handler, Logger logger, Chunk chunk, OccurrenceDao occurrenceDao)
    {
        super(handler, logger);
        this.chunk = chunk;
        this.occurrenceDao = occurrenceDao;
    }

    @Override
    protected boolean evaluate() throws TaskException
    {
        chunk.generateExtraData(occurrenceDao);
        ChunkCountResult countResult = new ChunkCountResult(
                occurrenceDao.countWords(chunk),
                occurrenceDao.countFunctionPhrases(chunk),
                chunk);
        setResult(countResult);
        return true;
    }
}
