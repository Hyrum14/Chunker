package utils.managers;

import app.model.dao.DaoFactory;
import app.model.dao.MapDaoFactory;
import app.model.service.executor.ThreadPoolExecutor;
import utils.logging.ConsoleLogger;
import utils.logging.LogLevel;
import utils.logging.Logger;

import java.util.Set;

public class UtilityManagerFactoryDevelopment implements UtilityManagerFactory
{
    private DaoFactory daoFactory = null;
    private ThreadPoolExecutor executor = null;
    private Logger logger = null;

    public UtilityManagerFactoryDevelopment()
    {
    }

    @Override
    public DaoFactory getDaoFactory()
    {
        if (daoFactory == null)
            daoFactory = new MapDaoFactory(getLogger());
        return daoFactory;
    }


    @Override
    public ThreadPoolExecutor getExecutor()
    {
        if (executor == null)
            executor = ThreadPoolExecutor.getInstance();
        return executor;
    }

    @Override
    public Logger getLogger()
    {
        if (logger == null)
            logger = new ConsoleLogger();
        return logger;
    }

    @Override
    public Logger getLogger(Set<LogLevel> enabled)
    {
        if (logger == null)
            logger = new ConsoleLogger(enabled);

        logger.setEnabled(enabled);
        return logger;
    }

}
