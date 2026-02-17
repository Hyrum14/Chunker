package utils.logging;

import utils.containers.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Logger
{
    protected final Map<String, Long> traceIds = new ConcurrentHashMap<>();
    protected final AtomicLong traceIdCounter = new AtomicLong(0);
    protected Set<LogLevel> enabled;
    protected Map<StackTraceElement, String> callers;

    protected Logger(Set<LogLevel> enabled)
    {
        this.enabled = enabled;
        if (this.enabled == null)
            this.enabled = Logger.defaultEnabled();
        callers = new HashMap<>();
    }

    public static String logLevelToString(LogLevel level)
    {
        return switch (level)
                {
                    case TRACE -> "TRACE";
                    case DEBUG -> "DEBUG";
                    case ERROR -> "ERROR";
                    case WARNING -> "WARNING";
                    default -> "STATE NOT DEFINED YET";
                };
    }

    public static Set<LogLevel> defaultEnabled()
    {
        Set<LogLevel> output = new HashSet<>();
        output.add(LogLevel.WARNING);
        output.add(LogLevel.ERROR);
        return output;
    }

    public static Set<LogLevel> allEnabled()
    {
        Set<LogLevel> output = new HashSet<>();
        output.add(LogLevel.WARNING);
        output.add(LogLevel.ERROR);
        output.add(LogLevel.TRACE);
        output.add(LogLevel.DEBUG);
        return output;
    }

    public static Set<LogLevel> noneEnabled()
    {
        return new HashSet<>();
    }

    public abstract void log(LogLevel level, String message);

    public boolean isEnabled(LogLevel level)
    {
        return enabled.contains(level);
    }

    public abstract void setEnabled(LogLevel level, boolean isEnabled);

    public abstract void setEnabled(Set<LogLevel> enabled);

    public abstract void print(LogLevel level, String message);

    public abstract void println(LogLevel level);

    public abstract void trace(boolean isEntering, Pair<String, Object>[] values);

    public abstract void logItem(String name, Object item);

    public abstract <T> void logCollection(String name, Collection<T> items);
}
