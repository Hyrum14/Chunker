package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Document;
import utils.objects.Evaluation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public abstract class DocumentDataPage extends PageGenerator
{
    public DocumentDataPage()
    {
        super(DOC_DATA_PAGE, false, true, 0);
    }

    @Override
    protected void fill_page(Evaluation evaluation)
    {
        List<Cell> row = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.FILE_NAME),
                new LabelCell(LabelConstant.PARENT_DIRECTORY),
                new LabelCell(LabelConstant.NUMBER_OF_WORDS)
        ));
        row.addAll(generateExtraLabels());
        row.addAll(List.of(
                new LabelCell(LabelConstant.DATE_LAST_CHANGED),
                new LabelCell(LabelConstant.TIME_LAST_CHANGED),
                new LabelCell(LabelConstant.DATE_ADDED),
                new LabelCell(LabelConstant.TIME_ADDED),
                new LabelCell(LabelConstant.PATH),
                new LabelCell(LabelConstant.DOCUMENT_ID)
        ));
        addRow(row);
        for (Document document : evaluation.getDocuments())
        {
            row = new ArrayList<>(List.of(
                    new Cell(document.getFileName()),
                    new Cell(document.getParentDirectory()),
                    new Cell(document.getNumWords())
            ));
            row.addAll(generateExtraDocumentDetails(document));
            row.addAll(List.of(
                    new Cell(LocalDateTime.ofInstant(document.getDateLastChanged(), ZoneId.systemDefault()), false, true),
                    new Cell(LocalDateTime.ofInstant(document.getDateLastChanged(), ZoneId.systemDefault()), true, false),
                    new Cell(LocalDateTime.ofInstant(document.getDateAdded(), ZoneId.systemDefault()), false, true),
                    new Cell(LocalDateTime.ofInstant(document.getDateAdded(), ZoneId.systemDefault()), true, false),
                    new Cell(document.getAbsolutePath()),
                    new Cell(document.getId())
            ));
            addRow(row);
        }
        addRest();
    }

    public abstract List<Cell> generateExtraDocumentDetails(Document document);

    public abstract List<Cell> generateExtraLabels();

    public abstract void addRest();
}
