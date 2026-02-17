package app.model.parsing;

public class StringDocumentProvider implements DocumentParser.DocumentProvider
{
    private String source;
    private int index;

    public StringDocumentProvider(String source)
    {
        this.source = source;
        this.index = 0;
    }

    @Override
    public boolean end()
    {
        return index >= source.length();
    }

    @Override
    public int peek()
    {
        if (end())
        {
            throw new IndexOutOfBoundsException("No more characters");
        }
        return source.codePointAt(index);
    }

    @Override
    public void advance()
    {
        if (end())
        {
            throw new IndexOutOfBoundsException("No more characters");
        }
        index++;
    }
}
