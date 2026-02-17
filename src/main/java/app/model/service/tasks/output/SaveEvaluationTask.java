package app.model.service.tasks.output;

import app.model.dao.interfaces.OccurrenceDao;
import app.model.export.formating.strategies.OutputStrategy;
import app.model.service.tasks.base.Task;
import app.model.service.tasks.base.TaskHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.logging.Logger;
import utils.objects.Evaluation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveEvaluationTask extends Task<String>
{
    protected final File selectedFile;
    protected final XSSFWorkbook workbook;
    Evaluation evaluation;
    OutputStrategy strategy;
    OccurrenceDao occurrenceDao;

    public SaveEvaluationTask(TaskHandler<String> handler, Logger logger, Evaluation evaluation, OutputStrategy strategy, File selectedFile, XSSFWorkbook workbook, OccurrenceDao occurrenceDao)
    {
        super(handler, logger);
        this.evaluation = evaluation;
        this.strategy = strategy;
        this.selectedFile = selectedFile;
        this.workbook = workbook;
        this.occurrenceDao = occurrenceDao;
    }

    @Override
    protected boolean evaluate()
    {
        occurrenceDao.getPossiblePhrases(evaluation.getDocumentIds()).forEach(possiblePhrase ->
        {
            if (possiblePhrase.hasPartOfSpeech())
            {
                evaluation.getPhraseCountManager().addPossibleItem(possiblePhrase);
                evaluation.getPartOfSpeechCountManager().addPossibleItem(possiblePhrase.getPartOfSpeech());
            } else
            {
                evaluation.getSpecialPhraseCountManager().addPossibleItem(possiblePhrase.getPhraseString());
            }
        });

        setResult(evaluation.getName());
        strategy.generate(evaluation);
        setResult(evaluation.getName());
        try
        {
            FileOutputStream fos = new FileOutputStream(selectedFile);
            final long[] byteCount = {0};
            workbook.write(new OutputStream()
            {
                @Override
                public void write(int b) throws IOException
                {
                    byteCount[0]++;
                    if (byteCount[0] % 100000 == 0)
                    {
                        sendProgressUpdate(String.format("Number of bytes written so far: %d", byteCount[0]));
                    }
                    fos.write(b);
                }
            });

            sendProgressUpdate(String.format("Wrote %d total bytes", byteCount[0]));
            workbook.close();
            fos.close();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            handler.handleException(evaluation.getName(), ex);
            return false;
        }

        return true;
    }
}
