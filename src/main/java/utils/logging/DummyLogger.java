package utils.logging;

import utils.containers.Pair;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DummyLogger extends Logger
{
    public DummyLogger()
    {
        super(new HashSet<>());
    }

    @Override
    public void log(LogLevel level, String message)
    {

    }

    @Override
    public boolean isEnabled(LogLevel level)
    {
        return false;
    }

    @Override
    public void setEnabled(LogLevel level, boolean isEnabled)
    {

    }

    @Override
    public void setEnabled(Set<LogLevel> enabled)
    {

    }

    @Override
    public void print(LogLevel level, String message)
    {

    }

    @Override
    public void println(LogLevel level)
    {

    }

    @SafeVarargs
    @Override
    public final void trace(boolean isEntering, Pair<String, Object>... values)
    {

    }

    @Override
    public void logItem(String name, Object item)
    {

    }

    @Override
    public <T> void logCollection(String name, Collection<T> items)
    {

    }
}
