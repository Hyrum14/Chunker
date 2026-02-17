package utils.logging;

import java.io.PrintStream;
import java.util.Set;

public class ConsoleLogger extends PrintStreamLogger
{
    public ConsoleLogger()
    {
        super();
    }

    public ConsoleLogger(Set<LogLevel> enabled)
    {
        super(enabled);
    }

    @Override
    public PrintStream getStream()
    {
        return System.out;
    }
}
