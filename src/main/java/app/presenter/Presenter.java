package app.presenter;

import app.model.chunking.ChunkingStrategy;
import app.model.export.formating.page.generators.PageGenerator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Presenter
{
    void addDocuments(Set<File> documents, Set<FunctionPhrase> functionPhraseSet, boolean forceUpdate);

    void generateChunks(Evaluation evaluation, List<Integer> documentIds, ChunkingStrategy strategy);

    void countChunks(Evaluation evaluation, Set<Chunk> chunks);

    void saveEvaluation(Evaluation evaluation, XSSFWorkbook workbook, File selectedFile, int countFactor, List<PageGenerator> pages) throws IOException;

    void closeApplication();

    void recreateChunks(Evaluation evaluation, File directory);

    void addPhraseList(File file);
}
