package utils.logging;

public enum LogLevel
{
    TRACE("TRACE"),
    DEBUG("DEBUG"),
    ERROR("ERROR"),
    WARNING("WARNING"),
    VALUE("VALUE");

    private final String displayName;

    LogLevel(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}
