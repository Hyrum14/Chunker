package app.model.service;

import app.model.cache.Cache;
import app.model.service.tasks.base.TaskHandler;
import app.model.service.tasks.evaluation.CountChunkTask;
import app.model.service.tasks.evaluation.GenerateChunksTask;
import app.model.service.tasks.output.RecreateChunkTask;
import app.model.service.tasks.output.SaveEvaluationTask;
import app.presenter.observers.base.SimpleServiceObserver;
import app.presenter.observers.evaluation.CountChunksObserver;
import app.presenter.observers.evaluation.RecreateChunksObserver;
import app.model.chunking.ChunkingStrategy;
import app.model.export.formating.strategies.OutputStrategy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.UtilityManager;
import utils.logging.Logger;
import utils.objects.ChunkCountResult;
import utils.objects.Evaluation;
import utils.objects.chunk.Chunk;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EvaluationService
{
    Logger logger;

    public EvaluationService(Logger logger)
    {
        this.logger = logger;
    }


    public void generateChunks(SimpleServiceObserver<Set<Chunk>> observer, List<Integer> documentIds, ChunkingStrategy strategy)
    {
        GenerateChunksTask task = new GenerateChunksTask(
                new GenerateChunksHandler(
                        observer,
                        logger
                ),
                logger,
                documentIds,
                strategy,
                Cache.getInstance().getFactory().getDocumentDao()
        );
        UtilityManager.getExecutor().execute(task);
    }

    public void countChunks(CountChunksObserver observer, Set<Chunk> chunks)
    {
        for (Chunk chunk : chunks)
        {
            CountChunkTask task = new CountChunkTask(
                    new CountChunkHandler(
                            observer.createMemberObserver(),
                            logger
                    ),
                    logger,
                    chunk,
                    Cache.getInstance().getFactory().getOccurrenceDao()
            );
            UtilityManager.getExecutor().execute(task);
        }
    }


    public void saveEvaluation(SimpleServiceObserver<String> observer, Evaluation evaluation, OutputStrategy strategy, File selectedFile, XSSFWorkbook workbook)
    {
        SaveEvaluationTask task = new SaveEvaluationTask(
                new SaveEvaluationHandler(observer, logger),
                logger,
                evaluation,
                strategy,
                selectedFile,
                workbook,
                Cache.getInstance().getFactory().getOccurrenceDao());
        UtilityManager.getExecutor().execute(task);
    }

    public void recreateChunks(RecreateChunksObserver recreateChunksObserver, Map<Chunk, File> chunkFiles)
    {
        for (Chunk chunk : chunkFiles.keySet())
        {
//            System.out.println(chunk);
            RecreateChunkTask task = new RecreateChunkTask(
                    new RecreateChunkEvaluationHandler(
                            recreateChunksObserver.createMemberObserver(),
                            logger
                    ),
                    logger,
                    chunk,
                    Cache.getInstance().getFactory().getDelimiterDao(),
                    Cache.getInstance().getFactory().getOccurrenceDao(),
                    chunkFiles.get(chunk));

            UtilityManager.getExecutor().execute(task);
        }
    }


    public static class SaveEvaluationHandler extends TaskHandler<String>
    {

        public SaveEvaluationHandler(SimpleServiceObserver<String> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return Evaluation.SAVE_EVALUATION;
        }

    }

    public static class RecreateChunkEvaluationHandler extends TaskHandler<Chunk>
    {
        public RecreateChunkEvaluationHandler(SimpleServiceObserver<Chunk> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return Evaluation.RECREATE_CHUNKS;
        }
    }

    public static class GenerateChunksHandler extends TaskHandler<Set<Chunk>>
    {
        public GenerateChunksHandler(SimpleServiceObserver<Set<Chunk>> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return Evaluation.GENERATE_CHUNKS;
        }
    }

    public static class CountChunkHandler extends TaskHandler<ChunkCountResult>
    {
        public CountChunkHandler(SimpleServiceObserver<ChunkCountResult> observer, Logger logger)
        {
            super(observer, logger);
        }

        @Override
        protected String getName()
        {
            return Evaluation.COUNT_CHUNK;
        }
    }


}
