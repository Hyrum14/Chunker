package app.model.parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDocumentProvider implements DocumentParser.DocumentProvider
{
    private BufferedReader reader;
    private int nextChar;

    public FileDocumentProvider(String filename) throws IOException
    {
        this.reader = new BufferedReader(new FileReader(filename));
        this.nextChar = reader.read();
        printChar();
    }

    public FileDocumentProvider(BufferedReader reader) throws IOException
    {
        this.reader = reader;
        this.nextChar = reader.read();
        printChar();
    }

    @Override
    public boolean end()
    {
        return nextChar == -1;
    }

    @Override
    public int peek()
    {
        if (end())
        {
            throw new IndexOutOfBoundsException("No more characters");
        }
        return nextChar;
    }

    @Override
    public void advance() throws IndexOutOfBoundsException
    {
        try
        {
            if (end())
            {
                throw new IndexOutOfBoundsException("No more characters");
            }
            nextChar = reader.read();
            printChar();
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    private void printChar()
    {
//        if (!end())
//            System.out.printf("%c [UTF+%04x]%n", nextChar, nextChar);
    }
}

