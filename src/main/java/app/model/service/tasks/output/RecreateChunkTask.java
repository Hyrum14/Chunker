package app.model.service.tasks.output;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.OccurrenceDao;
import app.model.service.EvaluationService;
import app.model.service.tasks.base.Task;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.chunk.Chunk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecreateChunkTask extends Task<Chunk>
{
    protected final DelimiterDao delimiterDao;
    protected final OccurrenceDao occurrenceDao;
    private final Chunk chunk;
    private final File output;

    public RecreateChunkTask(EvaluationService.RecreateChunkEvaluationHandler recreateChunkEvaluationHandler, Logger logger, Chunk chunk, DelimiterDao delimiterDao, OccurrenceDao occurrenceDao, File output)
    {
        super(recreateChunkEvaluationHandler, logger);
        this.delimiterDao = delimiterDao;
        this.occurrenceDao = occurrenceDao;
        this.chunk = chunk;
        this.output = output;
    }


    @Override
    protected boolean evaluate() throws TaskException
    {
        setResult(chunk);
        try (FileWriter writer = new FileWriter(output))
        {
            chunk.recreateChunk(writer, delimiterDao, occurrenceDao);
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new TaskException("Unable to write to the file: " + output + " " + e.getMessage());
        }


        return true;
    }


}
