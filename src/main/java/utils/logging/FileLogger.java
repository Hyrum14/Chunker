package utils.logging;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Set;

public class FileLogger extends PrintStreamLogger
{
    private final PrintStream output;

    public FileLogger(String fileName) throws FileNotFoundException
    {
        super(allEnabled());
        output = new PrintStream(fileName);
    }

    public FileLogger(Set<LogLevel> enabled, String fileName) throws FileNotFoundException
    {
        super(enabled);
        output = new PrintStream(fileName);
    }

    @Override
    public PrintStream getStream()
    {
        return output;
    }
}
