package utils.managers;

import app.model.dao.DaoFactory;
import app.model.service.executor.ThreadPoolExecutor;
import utils.logging.LogLevel;
import utils.logging.Logger;

import java.util.Set;

public interface UtilityManagerFactory
{
    DaoFactory getDaoFactory();


    ThreadPoolExecutor getExecutor();

    Logger getLogger();

    Logger getLogger(Set<LogLevel> enabled);
}
