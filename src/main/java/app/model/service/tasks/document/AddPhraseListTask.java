package app.model.service.tasks.document;

import app.model.service.tasks.base.Task;
import app.model.service.tasks.base.TaskHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.FunctionPhrase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AddPhraseListTask extends Task<Set<FunctionPhrase>>
{
    protected final File file;
    protected Set<FunctionPhrase> phrases;

    public AddPhraseListTask(TaskHandler<Set<FunctionPhrase>> handler, Logger logger, File file)
    {
        super(handler, logger);
        this.file = file;
        phrases = new HashSet<>();
    }

    @Override
    protected boolean evaluate() throws TaskException
    {

        String filename = file.getName().toLowerCase();
        try
        {
            if (filename.endsWith(".csv"))
            {
                scan(file, phrases, ",");
            } else if (filename.endsWith(".tsv"))
            {
                scan(file, phrases, "\t");
            } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx"))
            {
                FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet)
                {
                    Cell cellPartOfSpeech = row.getCell(0); // assuming partOfSpeech is the first column
                    Cell cellPhrase = row.getCell(1); // assuming phrase is the second column
                    if (cellPhrase != null && !cellPhrase.getStringCellValue().isEmpty())
                    {
                        String partOfSpeech = (cellPartOfSpeech != null) ? cellPartOfSpeech.getStringCellValue() : "";
                        String phrase = cellPhrase.getStringCellValue();
                        phrases.add(new FunctionPhrase(partOfSpeech, phrase));
                    }
                }
                workbook.close();
                fis.close();
            }
            setResult(phrases);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            setResult(phrases);

            throw new TaskException(e.getMessage());
        }
    }

    void scan(File file, Set<FunctionPhrase> phrases, String delimiter) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
        {
            String[] line = scanner.nextLine().split(delimiter);
            if (line.length >= 1)
            {
                String partOfSpeech = (line.length >= 2) ? line[0].trim() : "";
                String phrase = line[1].trim();
                phrases.add(new FunctionPhrase(partOfSpeech, phrase));
            } else
            {
                System.err.println("Incomplete line found in CSV/TSV file.");
                handleWarning("Incomplete line found in spreadsheet file.");
            }
        }
    }
}
