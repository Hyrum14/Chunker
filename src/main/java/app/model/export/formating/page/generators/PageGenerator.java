package app.model.export.formating.page.generators;

import app.model.export.formating.InvertedPage;
import app.model.export.formating.Page;
import app.model.export.formating.Row;
import app.model.export.formating.cell.Cell;
import app.view.View;
import utils.objects.Evaluation;

import java.util.List;
import java.util.Objects;

public abstract class PageGenerator
{
    public static final String RESULT_PAGE = "Results";
    public static final String EVAL_DATA_PAGE = "Evaluation_Details";
    public static final String DOC_DATA_PAGE = "Document_Details";
    public static final String CHUNK_DATA_PAGE = "Chunk_Details";
    protected static final String RAW_DATA_PAGE = "Raw_Data";
    protected static final String RAW_FUNCTION_WORDS_DATA_PAGE = "Raw_Function_Words";
    protected static final String FUNCTION_WORD_TOTAL_DATA_PAGE = "Function_Words_Total";
    protected static final String SPECIAL_PHRASES_DATA_PAGE = "Special_Phrase_Counts";
    protected static final String STANDARDIZED_COUNT_PAGE = "Standardized_Counts";
    private final boolean inverted;
    private final String name;
    private final boolean auto_size;
    private final int col_width;
    private Page output;
    private int nextRowIndex = 0;

    public PageGenerator(String name, boolean inverted, boolean auto_size, int col_width)
    {
        this.auto_size = auto_size;
        this.col_width = col_width;
        this.name = name + (inverted ? "_Transposed" : "");
        this.inverted = inverted;
    }

    public String getName()
    {
        return name;
    }

    protected abstract void fill_page(Evaluation evaluation);

    public Page generate(Evaluation evaluation, View view)
    {
        if (isInverted())
            output = new InvertedPage(getName(), auto_size, col_width);
        else
            output = new Page(getName(), auto_size, col_width);

        fill_page(evaluation);
        view.showMessage(String.format("Page %s is computed", getName()));
        return output;
    }

    protected void addRow(List<Cell> list)
    {
        Row row = new Row();
        row.putCell(list);
        output.putRow(nextRowIndex, row);
        nextRowIndex++;
    }

    protected void addRow(Cell first, Cell second)
    {
        Row row = new Row();
        row.putCell(first, 0);
        row.putCell(second, 1);
        output.putRow(nextRowIndex, row);
        nextRowIndex++;
    }

    protected void skipRow()
    {
        nextRowIndex++;
    }


    public boolean isInverted()
    {
        return inverted;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageGenerator generator = (PageGenerator) o;
        return Objects.equals(name, generator.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}
