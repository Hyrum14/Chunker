package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class SpecialPhrasesDataPage extends PageGenerator
{
    public SpecialPhrasesDataPage()
    {
        super(PageGenerator.SPECIAL_PHRASES_DATA_PAGE, false, false, 10);
    }


    @Override
    public void fill_page(Evaluation evaluation)
    {
        List<Cell> labels = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.PART_OF_SPEECH),
                new LabelCell(LabelConstant.PHRASE),
                new LabelCell(LabelConstant.SUM_ACROSS_ALL_CHUNKS)
        ));

        for (Chunk chunk : evaluation.getChunks())
        {
            labels.add(new Cell(chunk.getChunkName()));
        }
        addRow(labels);

        List<Cell> total = new ArrayList<>(List.of(
                new Cell(),
                new Cell("Chunk Size"),
                new Cell(evaluation.getWordCountManager().getTotalByChunkName().getTotal())
        ));
        for (Chunk chunk : evaluation.getChunks())
        {
            total.add(new Cell(evaluation.getWordCountManager().getTotalByChunkName().get(chunk.getChunkName())));
        }
        addRow(total);


        List<Cell> currRow;
        for (String partOfSpeech : evaluation.getSpecialPhraseCountManager().getPossibleItems())
        {
            currRow = new ArrayList<>(List.of(
                    new Cell(),
                    new Cell(partOfSpeech),
                    new Cell(evaluation.getSpecialPhraseCountManager().getTotalByItem().get(partOfSpeech))
            ));
            for (Chunk chunk : evaluation.getChunks())
            {
                currRow.add(
                        new Cell(
                                evaluation.getSpecialPhraseCountManager().getCount(chunk).get(partOfSpeech)
                        )
                );
            }
//            view.showMessage(String.format("Page %s: %s", getName(), partOfSpeech));
            addRow(currRow);
        }
//        view.showMessage(String.format("Page %s complete", getName()));

    }


}
