package app.model.dao;

import app.model.dao.interfaces.DelimiterDao;
import app.model.dao.interfaces.DocumentDao;
import app.model.dao.interfaces.OccurrenceDao;
import utils.logging.Logger;

import java.io.PrintStream;

public abstract class DaoFactory
{
    protected DelimiterDao delimiterDao = null;
    protected DocumentDao documentDao = null;
    protected OccurrenceDao occurrenceDao = null;
    protected Logger logger;

    public DelimiterDao getDelimiterDao()
    {
        return delimiterDao;
    }

    public DocumentDao getDocumentDao()
    {
        return documentDao;
    }

    public OccurrenceDao getOccurrenceDao()
    {
        return occurrenceDao;
    }

    public abstract void printDelimiterTable(PrintStream output);

    public abstract void printDocumentTable(PrintStream output);

    public abstract void printOccurrenceTable(PrintStream output);


    public void printAllTables(PrintStream output)
    {
        printDocumentTable(output);
        output.println();
        printDelimiterTable(output);
        output.println();
        printOccurrenceTable(output);
        output.println();
    }

    public abstract void clear();
}
