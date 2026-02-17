package app.model.dao;

import app.model.dao.map.DelimiterMapDao;
import app.model.dao.map.DocumentMapDao;
import app.model.dao.map.OccurrenceMapDao;
import utils.logging.Logger;
import utils.objects.Delimiter;
import utils.objects.Document;
import utils.objects.Token;

import java.io.PrintStream;
import java.util.Collection;

public class MapDaoFactory extends DaoFactory
{
    public MapDaoFactory(Logger logger)
    {
        this.logger = logger;
        delimiterDao = new DelimiterMapDao();
        documentDao = new DocumentMapDao();
        occurrenceDao = new OccurrenceMapDao();
    }

    @Override
    public void printDelimiterTable(PrintStream output)
    {
        output.println("Delimiter Table");
        Collection<Delimiter> table = delimiterDao.getTable();
        for (Delimiter item : table)
        {
            output.println(item);
        }
    }

    @Override
    public void printDocumentTable(PrintStream output)
    {
        output.println("Document Table");
        Collection<Document> table = documentDao.getTable();
        for (Document item : table)
        {
            output.println(item);
        }
    }


    @Override
    public void printOccurrenceTable(PrintStream output)
    {
        output.println("Occurrence Table");
        Collection<Token> table = occurrenceDao.getTable();
        for (Token item : table)
        {
            output.println(item);
        }
    }


    @Override
    public void clear()
    {
        delimiterDao = new DelimiterMapDao();
        documentDao = new DocumentMapDao();
        occurrenceDao = new OccurrenceMapDao();
    }
}
