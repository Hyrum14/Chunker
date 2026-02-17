package utils;

import app.model.dao.DaoFactory;
import app.model.service.executor.ThreadPoolExecutor;
import utils.logging.DummyLogger;
import utils.logging.FileLogger;
import utils.logging.LogLevel;
import utils.logging.Logger;
import utils.managers.UtilityManagerFactory;
import utils.managers.UtilityManagerFactoryDevelopment;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

public class UtilityManager
{
    private static UtilityManagerFactory factory = null;

    private UtilityManager()
    {

    }

    private static void setFactory()
    {
        factory = new UtilityManagerFactoryDevelopment();
    }

    public static DaoFactory getDaoFactory()
    {
        if (factory == null)
            setFactory();
        return factory.getDaoFactory();
    }


    public static ThreadPoolExecutor getExecutor()
    {
        if (factory == null)
            setFactory();
        return factory.getExecutor();
    }

    public static Logger getLogger()
    {
        if (factory == null)
            setFactory();
        return factory.getLogger();
    }

    public static Logger getLogger(Set<LogLevel> enabled)
    {
        if (factory == null)
            setFactory();
        return factory.getLogger(enabled);
    }

    public static Logger getCacheLogger()
    {
        try
        {
            return new FileLogger("logs/Cache_Log.txt");
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return new DummyLogger();
        }
    }

    public static LocalDateTime toLocaLDateTime(Instant instant)
    {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
