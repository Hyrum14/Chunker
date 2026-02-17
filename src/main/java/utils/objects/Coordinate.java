package utils.objects;

import java.util.Objects;

public class Coordinate
{
    private int row;
    private int col;


    public Coordinate(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRowIndex()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColIndex()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Coordinate{");
        sb.append("row=").append(row);
        sb.append(", col=").append(col);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(row, col);
    }
}
