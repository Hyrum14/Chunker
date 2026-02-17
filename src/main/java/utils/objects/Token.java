package utils.objects;

import java.util.Objects;

public class Token
{
    private String contents;
    private String root;
    private int documentId;
    private int index;

    public Token(String contents, String root, int documentId, int index)
    {
        this.contents = contents;
        this.root = root;
        this.documentId = documentId;
        this.index = index;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public String getRoot()
    {
        return root;
    }

    public void setRoot(String root)
    {
        this.root = root;
    }

    public int getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(int documentId)
    {
        this.documentId = documentId;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token that = (Token) o;
        return documentId == that.documentId && index == that.index && Objects.equals(root, that.root);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(contents, root, documentId, index);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Occurrence{");
        sb.append(", contents='").append(contents).append('\'');
        sb.append(", root='").append(root).append('\'');
        sb.append(", documentId=").append(documentId);
        sb.append(", index=").append(index);
        sb.append('}');
        return sb.toString();
    }
}
