package app.presenter;

import app.model.chunking.ChunkingStrategy;
import app.model.export.formating.page.generators.*;
import app.model.export.formating.strategies.XlxsOutputStrategy;
import app.model.service.DocumentService;
import app.model.service.EvaluationService;
import app.presenter.observers.document.AddDocumentsObserver;
import app.presenter.observers.document.AddPhraseListObserver;
import app.presenter.observers.evaluation.*;
import app.view.View;
import app.view.gui.dialog.SaveDialog;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.logging.DummyLogger;
import utils.logging.LogLevel;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.*;


public class MainPresenter implements Presenter
{

    private final Logger serviceLogger;
    private final View view;

    public MainPresenter(View view)
    {
        this.view = view;
        this.serviceLogger = new DummyLogger();
    }

    public MainPresenter(View view, Logger serviceLogger)
    {
        this.view = view;
        this.serviceLogger = serviceLogger;
    }

    public static boolean isValidUTF8File(File file) throws IOException
    {
        return file.isFile();
//        return UniversalDetector.detectCharset(file).equalsIgnoreCase("UTF-8");
    }

    @Override
    public void addDocuments(Set<File> files, Set<FunctionPhrase> functionPhraseSet, boolean forceUpdate)
    {
        Set<Document> documents = new HashSet<>();
        for (File file : files)
        {
            try
            {
                if (!isValidUTF8File(file))
                {
                    throw new IOException(String.format("File: %s is not encoded in UTF-8", file.getName()));
                }
                BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                Instant lastModifiedTime = attributes.lastModifiedTime().toInstant();
                Document document = new Document(file.getName(), file.getAbsolutePath(), file.getParentFile().getName(), 0, lastModifiedTime);
                documents.add(document);
            } catch (IOException ex)
            {
                ex.printStackTrace();
                serviceLogger.log(LogLevel.ERROR, ex.getMessage());
                view.showWarning("Document failed: " + ex.getMessage());
            }
        }
        AddDocumentsObserver observer = new AddDocumentsObserver(view, documents.size());

        new DocumentService(serviceLogger).addDocuments(
                observer,
                documents,
                functionPhraseSet,
                forceUpdate
        );
    }

    @Override
    public void generateChunks(Evaluation evaluation, List<Integer> documentIds, ChunkingStrategy strategy)
    {
        new EvaluationService(serviceLogger).generateChunks(
                new GenerateChunksObserver(view, new EvaluationReportingObserver(view), evaluation),
                documentIds,
                strategy
        );
    }

    @Override
    public void countChunks(Evaluation evaluation, Set<Chunk> chunks)
    {
        new EvaluationService(serviceLogger).countChunks(
                new CountChunksObserver(view, chunks.size(), new EvaluationReportingObserver(view), evaluation),
                chunks
        );
    }


    @Override
    public void saveEvaluation(Evaluation evaluation, XSSFWorkbook workbook, File selectedFile, int countFactor, List<PageGenerator> pages) throws IOException
    {
        if (!selectedFile.exists())
            selectedFile.createNewFile();
        new EvaluationService(serviceLogger).saveEvaluation(
                new SaveEvaluationObserver(view),
                evaluation,
                new XlxsOutputStrategy(
                        (pages != null ?
                                pages :
                                Arrays.asList(
                                        evaluation.getChunkingStrategy().getMetaDataPage(countFactor),
                                        new RawDataPage(),
                                        new RawFunctionWordDataPage(),
                                        new AggregateFunctionWordDataPage(),
                                        new SpecialPhrasesDataPage(),
                                        new StandardizedCountPage(countFactor),
                                        new InvertedStandardizedCountPage(countFactor),
                                        evaluation.getChunkingStrategy().getDocumentDataPage(),
                                        evaluation.getChunkingStrategy().getChunkDataPage()
                                )),
                        workbook,
                        view
                ),
                selectedFile,
                workbook
        );
    }

    @Override
    public void closeApplication()
    {
        view.closeApplication();
    }

    @Override
    public void recreateChunks(Evaluation evaluation, File directory)
    {
        if (directory == null || !directory.exists())
        {
            view.showError("Selected directory does not exist");
            return;
        }

        boolean overwriteAll = false;
        Map<Chunk, File> chunkFiles = new HashMap<>();
        for (Chunk chunk : evaluation.getChunks())
        {
            File file = new File(directory, chunk.getChunkName() + ".txt");
            if (overwriteAll)
            {
                chunkFiles.put(chunk, file);
                continue;
            }
            SaveDialog saveDialog = new SaveDialog();
            file = saveDialog.showSaveDialog(null, file, ".txt");
            if (saveDialog.isCanceled())
            {
                view.showMessage("Canceled");
                return;
            }
            if (saveDialog.isOverwriteAll())
                overwriteAll = true;
            chunkFiles.put(chunk, file);
        }
        new EvaluationService(serviceLogger).recreateChunks(
                new RecreateChunksObserver(view, evaluation.getChunks().size()),
                chunkFiles
        );


    }

    @Override
    public void addPhraseList(File file)
    {
        if (file == null || !file.exists())
        {
            view.showError("Selected file does not exist");
            return;
        }
        if (file.isDirectory())
        {
            view.showError("Must select spreadsheet files ONLY");
            return;
        }
        new DocumentService(serviceLogger).addPhraseList(new AddPhraseListObserver(view), file);
    }


}
