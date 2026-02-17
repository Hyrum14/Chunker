package app.model.export.formating;

import app.model.export.formating.cell.Cell;

import java.util.*;
import java.util.function.Consumer;

public class Row implements Iterable<Cell>
{
    private final Map<Integer, Cell> cells = new HashMap<>();
    private int maxCol = 0;

    public Row()
    {
    }


    public Cell getCell(int colIndex) throws IndexOutOfBoundsException
    {
        if (colIndex > getMaxCol())
            putCell(new Cell(), colIndex);

        if (!cells.containsKey(colIndex))
            putCell(new Cell(), colIndex);

        return cells.get(colIndex);
    }

    public int getMaxCol()
    {
        return maxCol;
    }

    public void putCell(Cell cell, int colIndex)
    {
        maxCol = Integer.max(maxCol, colIndex);
        cells.put(colIndex, cell);
    }

    public void putCell(Cell cell)
    {
        putCell(cell, maxCol);
        maxCol += 1;
    }


    public void putCell(Map<Integer, Cell> toAdd)
    {
        for (Integer index : toAdd.keySet())
        {
            putCell(toAdd);
        }
    }

    public void putCell(List<Cell> cells)
    {
        for (int colIndex = 0; colIndex < cells.size(); ++colIndex)
        {
            putCell(cells.get(colIndex), colIndex);
        }
    }

    public void putCell(List<Cell> cells, int offset)
    {
        for (int colIndex = 0; colIndex < cells.size(); ++colIndex)
        {
            putCell(cells.get(colIndex), colIndex + offset);
        }
    }

    @Override
    public Iterator<Cell> iterator()
    {
        return new Iterator<Cell>()
        {
            int currentIndex = 0;

            @Override
            public boolean hasNext()
            {
                return currentIndex <= maxCol;
            }

            @Override
            public Cell next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                Cell cell = getCell(currentIndex);
                currentIndex++;
                return cell;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Cell> action)
    {
        for (Cell cell : this)
        {
            action.accept(cell);
        }
    }

    @Override
    public Spliterator<Cell> spliterator()
    {
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED);
    }

}


