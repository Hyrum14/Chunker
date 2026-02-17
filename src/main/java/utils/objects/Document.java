package utils.objects;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Document implements Comparable<Document>
{
    public final static String DOCUMENT = "DOCUMENT";
    public final static String ID = "DOCUMENT_ID";
    public final static String NAME = "DOCUMENT_NAME";
    public final static String PATH = "DOCUMENT_PATH";
    public final static String SIZE = "DOCUMENT_SIZE";
    public static final String STREAM = "DOCUMENT_STREAM";
    public Instant dateAdded;
    List<Comparator<Document>> comparators = new ArrayList<>();
    private int id;
    private String fileName;
    private String absolutePath;
    private String parentDirectory;
    private int numWords;
    private Instant dateLastChanged;

    public Document(String fileName, String absolutePath, String parentDirectory, int numWords, Instant dateLastChanged)
    {
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.numWords = numWords;
        this.dateLastChanged = dateLastChanged;
        this.parentDirectory = parentDirectory;
    }

    public Document(int id, String fileName, String absolutePath, String parentDirectory, int numWords, Instant dateAdded, Instant dateLastChanged)
    {
        this.id = id;
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.numWords = numWords;
        this.dateAdded = dateAdded;
        this.dateLastChanged = dateLastChanged;
        this.parentDirectory = parentDirectory;
    }

    public Instant getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(Instant dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public void addWord()
    {
        numWords++;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getAbsolutePath()
    {
//        System.out.println("Path: " + absolutePath);
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath)
    {
        this.absolutePath = absolutePath;
    }

    public String getParentDirectory()
    {
        return parentDirectory;
    }

    public void setParentDirectory(String parentDirectory)
    {
        this.parentDirectory = parentDirectory;
    }

    public int getNumWords()
    {
        return numWords;
    }

    public void setNumWords(int numWords)
    {
        this.numWords = numWords;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id == document.id && Objects.equals(fileName, document.fileName) && Objects.equals(absolutePath, document.absolutePath) && Objects.equals(parentDirectory, document.parentDirectory);
    }

    @Override
    public int hashCode()
    {
        int hash = Objects.hash(id, fileName, absolutePath, parentDirectory);

//        System.out.printf("%d, %s, %s, %s%n", id, fileName, absolutePath, parentDirectory);
        return hash;
    }

    @Override
    public String toString()
    {
        return fileName;
    }

    public String getSummary()
    {
        return String.format("%s[id=%d]", fileName, id);
    }

    public Instant getDateLastChanged()
    {
        return Instant.ofEpochSecond(new File(absolutePath).lastModified() / 1000);
    }


    int compareByNumber(Document o)
    {
        if (comparators.isEmpty())
        {
            comparators.add(Comparator.comparingInt(doc -> doc.numWords));
            comparators.add(Comparator.comparing(doc -> doc.fileName));
            comparators.add(Comparator.comparingInt(Document::getId));
            comparators.add(Comparator.comparing(doc -> doc.parentDirectory));
            comparators.add(Comparator.comparing(doc -> doc.dateLastChanged));
        }

        for (Comparator<Document> comparator : comparators)
        {
            int result = comparator.compare(this, o);
            if (result != 0)
            {
                return result;
            }
        }

        return 0;
    }

    int compareByFile(Document o)
    {
        if (comparators.isEmpty())
        {
            comparators.add(Comparator.comparingInt(doc -> doc.numWords));
            comparators.add(Comparator.comparing(doc -> doc.fileName));
            comparators.add(Comparator.comparingInt(Document::getId));
            comparators.add(Comparator.comparing(doc -> doc.parentDirectory));
            comparators.add(Comparator.comparing(doc -> doc.dateLastChanged));
        }

        for (Comparator<Document> comparator : comparators)
        {
            int result = comparator.compare(this, o);
            if (result != 0)
            {
                return result;
            }
        }

        return 0;
    }

    @Override
    public int compareTo(Document o)
    {
        return compareByNumber(o);
    }

}
