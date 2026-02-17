package utils.logging;

import utils.containers.Pair;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Set;

public abstract class PrintStreamLogger extends Logger
{
    public PrintStreamLogger(Set<LogLevel> enabled)
    {
        super(enabled);
    }

    public PrintStreamLogger()
    {
        super(Logger.defaultEnabled());
    }

    @Override
    public void log(LogLevel level, String message)
    {
        getStream().printf("[%s] %s %n", level.toString(), message);
    }

    @Override
    public void setEnabled(LogLevel level, boolean isEnabled)
    {
        if (isEnabled)
            enabled.add(level);
        else
            enabled.remove(level);
    }

    @Override
    public void setEnabled(Set<LogLevel> enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public void print(LogLevel level, String message)
    {
        if (!isEnabled(level))
            return;

        getStream().print(message);
    }

    @Override
    public void println(LogLevel level)
    {
        if (!isEnabled(level))
            return;
        getStream().println();
    }

    @Override
    @SafeVarargs
    public final void trace(boolean isEntering, Pair<String, Object>... values)
    {
        if (!isEnabled(LogLevel.TRACE))
        {
            return;
        }

        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        String methodIdentifier = className + "::" + methodName;

        String status;
        long traceId;

        if (isEntering)
        {
            traceId = traceIdCounter.incrementAndGet();
            traceIds.put(methodIdentifier, traceId);
            status = "Entering";
        } else
        {
            traceId = traceIds.getOrDefault(methodIdentifier, -1L);
            traceIds.remove(methodIdentifier);
            status = "Exiting";
        }

        String message = String.format("%s %s (Trace ID: %d)", status, methodIdentifier, traceId);
        log(LogLevel.TRACE, message);


        if (values != null)
        {
            status = (isEntering ? "Arguments" : "Results");
            log(LogLevel.TRACE, String.format("Begin %s", status));
            for (Pair<String, Object> value : values)
            {
                logItem(value.getFirst(), value.getSecond());
            }
            log(LogLevel.TRACE, String.format("End %s", status));
        }
    }

    public void logItem(String name, Object item)
    {
        if (item != null && item.getClass().isAssignableFrom(Collection.class))
        {
            logCollection(name, (Collection<?>) item);
            return;
        }

        if (item == null)
        {
            String message = String.format("%s: { (null) }", name);
            log(LogLevel.VALUE, message);
            return;
        }

        String message = String.format("%s::%s: { %s }", item.getClass(), name, item);
        log(LogLevel.VALUE, message);
    }

    @Override
    public <T> void logCollection(String name, Collection<T> items)
    {
        Integer index = 0;
        for (T item : items)
        {
            String message = String.format("%s(%d)", name, index);
            logItem(message, item);
            index++;
        }
    }

    public abstract PrintStream getStream();
}
