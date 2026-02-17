package app.presenter;

import app.model.chunking.ChunkingStrategy;
import app.model.export.formating.page.generators.PageGenerator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.containers.Pair;
import utils.logging.Logger;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class LoggingPresenter implements Presenter
{
    private final Presenter presenter;
    private final Logger logger;

    public LoggingPresenter(Logger logger, Presenter presenter)
    {
        this.logger = logger;
        this.presenter = presenter;
    }

    @Override
    public void addDocuments(Set<File> documents, Set<FunctionPhrase> functionPhraseSet, boolean forceUpdate)
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("documents", documents),
                        new Pair<>("functionPhraseSet", functionPhraseSet),
                        new Pair<>("forceUpdate", forceUpdate),
                }
        );
        presenter.addDocuments(documents, functionPhraseSet, forceUpdate);
        logger.trace(false, new Pair[]{});
    }

    @Override
    public void generateChunks(Evaluation evaluation, List<Integer> documentIds, ChunkingStrategy strategy)
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("evaluationDescription", evaluation),
                        new Pair<>("documentIds", documentIds),
                        new Pair<>("strategy", strategy),
                }
        );
        presenter.generateChunks(evaluation, documentIds, strategy);
        logger.trace(false, new Pair[]{});
    }

    @Override
    public void countChunks(Evaluation evaluation, Set<Chunk> chunks)
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("chunks", chunks),
                        new Pair<>("evaluationDescription", evaluation),
                }
        );
        presenter.countChunks(evaluation, chunks);
        logger.trace(false, new Pair[]{});
    }


    @Override
    public void saveEvaluation(Evaluation evaluation, XSSFWorkbook workbook, File selectedFile, int countFactor, List<PageGenerator> pages) throws IOException
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("evaluation", evaluation),
                        new Pair<>("workbook", workbook),
                        new Pair<>("selectedFile", selectedFile),
                        new Pair<>("countFactor", countFactor),
                        new Pair<>("pages", pages),
                }
        );
        presenter.saveEvaluation(evaluation, workbook, selectedFile, countFactor, pages);
        logger.trace(false, new Pair[]{});
    }


    @Override
    public void closeApplication()
    {
        logger.trace(true, null);
        presenter.closeApplication();
        logger.trace(false, null);
    }

    @Override
    public void recreateChunks(Evaluation evaluation, File directory)
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("evaluation", evaluation),
                        new Pair<>("directory", directory),
                }
        );
        presenter.recreateChunks(evaluation, directory);
        logger.trace(false, new Pair[]{});
    }

    @Override
    public void addPhraseList(File file)
    {
        logger.trace(
                true,
                new Pair[]{
                        new Pair<>("file", file)
                }
        );
        presenter.addPhraseList(file);
        logger.trace(false, new Pair[]{});
    }


}
