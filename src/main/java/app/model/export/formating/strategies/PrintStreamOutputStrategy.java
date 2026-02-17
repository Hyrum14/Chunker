package app.model.export.formating.strategies;

import app.model.export.formating.Page;
import app.model.export.formating.page.generators.PageGenerator;
import app.view.View;
import utils.objects.Coordinate;

import java.io.PrintStream;
import java.util.List;

public class PrintStreamOutputStrategy extends OutputStrategy
{
    PrintStream output;

    public PrintStreamOutputStrategy(List<PageGenerator> pageGenerators, PrintStream output, View view)
    {
        super(pageGenerators, view);
        this.output = output;
    }

    @Override
    public void producePageOutput(Page page)
    {
        output.println(page.getName());
        String delimiter;
        for (int rowIndex = 0; rowIndex < page.getMaxRow(); rowIndex++)
        {
            delimiter = "";
            for (int colIndex = 0; colIndex < page.getMaxCol(); colIndex++)
            {
                output.print(delimiter);
                output.print(page.getCell(new Coordinate(rowIndex, colIndex)).toString());
                delimiter = ",";
            }
            output.println();
        }
    }
}
