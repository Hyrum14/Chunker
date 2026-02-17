package app.model.export.formating;

import app.model.export.formating.cell.CellStyleFactory;
import app.model.export.formating.page.generators.PageGenerator;
import app.view.View;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;

public class OutputStrategy
{
    protected final View messagePanel;
    protected List<Page> pages;
    protected List<PageGenerator> pageGenerators;

    public OutputStrategy(List<PageGenerator> pageGenerators, View messagePanel)
    {
        this.pageGenerators = pageGenerators;
        this.messagePanel = messagePanel; 
    }

    public void generate(Evaluation evaluation, XSSFWorkbook workbook, CellStyleFactory cellStyleFactory)
    {
        pages = new ArrayList<>();
        messagePanel.showMessage("Preparing pages for exporting ...");
        for (PageGenerator generator : pageGenerators)
        {
            generator.generate(evaluation, messagePanel);
            messagePanel.showMessage(String.format("%s page is ready to export", generator.getName()));
        }
        messagePanel.showMessage("Writing data to file ...");
    }


}
