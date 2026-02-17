package app.model.export.formating.strategies;

import app.model.export.formating.Page;
import app.model.export.formating.page.generators.PageGenerator;
import app.view.View;
import utils.objects.Evaluation;

import java.util.ArrayList;
import java.util.List;

public abstract class OutputStrategy
{
    protected final View messagePanel;
    protected List<Page> pages;
    protected List<PageGenerator> pageGenerators;

    public OutputStrategy(List<PageGenerator> pageGenerators, View messagePanel)
    {
        this.pageGenerators = pageGenerators;
        this.messagePanel = messagePanel;
    }

    public void generate(Evaluation evaluation)
    {
        pages = new ArrayList<>();
        messagePanel.showMessage("Building pages ...");
        for (PageGenerator generator : pageGenerators)
        {
            pages.add(generator.generate(evaluation, messagePanel));
        }
        messagePanel.showMessage("Preparing pages for exporting ...");
        for (Page page : pages)
        {
            messagePanel.showMessage(String.format("Preparing %s page (%d total cells) ...", page.getName(), page.getMaxRow() * page.getMaxCol()));
            producePageOutput(page);
            messagePanel.showMessage(String.format("%s page is ready to export", page.getName()));
        }
        messagePanel.showMessage("Writing data to file ...");

    }


    public abstract void producePageOutput(Page page);

}
