package app.model.export.formating;

import app.model.export.formating.cell.Cell;
import utils.objects.Coordinate;

public class InvertedPage extends Page
{
    public InvertedPage(String name, boolean autoSize, int colWidth)
    {
        super(name, autoSize, colWidth);
    }

    @Override
    public Cell getCell(Coordinate coordinate) throws IndexOutOfBoundsException
    {
        Coordinate invertedCoordinate = new Coordinate(coordinate.getColIndex(), coordinate.getRowIndex());
        return super.getCell(invertedCoordinate);
    }

    @Override
    public int getMaxCol()
    {
        return super.getMaxRow();
    }

    @Override
    public int getMaxRow()
    {
        return super.getMaxCol();
    }
}

