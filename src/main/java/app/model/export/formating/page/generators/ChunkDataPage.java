package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public abstract class ChunkDataPage extends PageGenerator
{
    public ChunkDataPage()
    {
        super(CHUNK_DATA_PAGE, false, true, 0);
    }

    @Override
    protected void fill_page(Evaluation evaluation)
    {

        List<Cell> row = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.CHUNK_NAME),
                new LabelCell(LabelConstant.CHUNK_ID),
                new LabelCell(LabelConstant.DOCUMENT_NAME),
                new LabelCell(LabelConstant.DOCUMENT_ID),
                new LabelCell(LabelConstant.SIZE)
        ));
        row.addAll(generateExtraLabels());

        addRow(row);
        for (Chunk chunk : evaluation.getChunks())
        {
            row = new ArrayList<>(List.of(
                    new Cell(chunk.getChunkName()),
                    new Cell(chunk.getChunkId()),
                    new Cell(chunk.getDocumentName()),
                    new Cell(chunk.getDocumentId()),
                    new Cell(chunk.getSize())

            ));
            row.addAll(generateExtraChunkDetails(chunk));
            addRow(row);
        }
        addRest();
//        view.showMessage(String.format("Page %s complete", getName()));
    }

    public abstract List<Cell> generateExtraChunkDetails(Chunk chunk);

    public abstract List<Cell> generateExtraLabels();

    public abstract void addRest();
}
