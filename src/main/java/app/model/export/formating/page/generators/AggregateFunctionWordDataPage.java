package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class AggregateFunctionWordDataPage extends PageGenerator
{
    public AggregateFunctionWordDataPage()
    {
        super(PageGenerator.FUNCTION_WORD_TOTAL_DATA_PAGE, false, false, 10);
    }


    @Override
    public void fill_page(Evaluation evaluation)
    {
        List<Cell> labels = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.PART_OF_SPEECH),
                new LabelCell(LabelConstant.AGGREGATE_WORD),
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
        for (String partOfSpeech : evaluation.getPartOfSpeechCountManager().getPossibleItems())
        {
            currRow = new ArrayList<>(List.of(
                    new Cell(""),
                    new Cell(partOfSpeech),
                    new Cell(evaluation.getPartOfSpeechCountManager().getTotalByItem().get(partOfSpeech))
            ));
            for (Chunk chunk : evaluation.getChunks())
            {
                currRow.add(
                        new Cell(
                                evaluation.getPartOfSpeechCountManager().getCount(chunk).get(partOfSpeech)
                        )
                );
            }
//            view.showMessage(String.format("Page %s: %s", getName(), partOfSpeech));
            addRow(currRow);
        }

        List<Cell> total1 = new ArrayList<>(List.of(
                new Cell(""),
                new Cell("Total Function Words"),
                new Cell(evaluation.getPhraseCountManager().getTotalByChunkName().getTotal())
        ));
        for (Chunk chunk : evaluation.getChunks())
        {
            total1.add(new Cell(evaluation.getPartOfSpeechCountManager().getTotalByChunkName().get(chunk.getChunkName())));
        }
        addRow(total1);

//        view.showMessage(String.format("Page %s complete", getName()));
    }


}
