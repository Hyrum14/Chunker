package app.model.export.formating;

import app.model.export.formating.cell.Cell;
import utils.objects.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class Page
{
    private final String name;
    private final boolean autoSize;
    private final int colWidth;
    Map<Integer, Row> rows;
    private int maxRow;
    private int maxCol;

    public Page(String name, boolean autoSize, int colWidth)
    {
        this.name = name;
        this.autoSize = autoSize;
        this.colWidth = colWidth;
        rows = new HashMap<>();
        maxRow = 0;
        maxCol = 0;
    }

    public String getName()
    {
        return name;
    }

    private Row getRow(int rowIndex) throws IndexOutOfBoundsException
    {
        if (rowIndex > maxRow)
            throw new IndexOutOfBoundsException(rowIndex);
        if (rows.get(rowIndex) == null)
            putRow(rowIndex, new Row());
        return rows.get(rowIndex);
    }

    public Cell getCell(Coordinate coordinate) throws IndexOutOfBoundsException
    {
        if (!rows.containsKey(coordinate.getRowIndex()))
            putRow(coordinate.getRowIndex(), new Row());

        return rows.get(coordinate.getRowIndex()).getCell(coordinate.getColIndex());
    }

    public void putRow(Row row)
    {
        maxRow++;
        putRow(maxRow, row);
        maxCol = Integer.max(row.getMaxCol(), maxCol);
    }

    public void putRow(int rowIndex, Row row)
    {
        maxRow = Integer.max(rowIndex, maxRow);
        maxCol = Integer.max(row.getMaxCol(), maxCol);
        rows.put(rowIndex, row);
    }

    public int getMaxRow()
    {
        return maxRow;
    }

    public int getMaxCol()
    {
        return maxCol;
    }

    public boolean isAutoSize()
    {
        return autoSize;
    }

    public int getColWidth()
    {
        return colWidth;
    }
}
