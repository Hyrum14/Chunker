package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class RawFunctionWordDataPage extends PageGenerator {

    public RawFunctionWordDataPage() {
        super(PageGenerator.RAW_FUNCTION_WORDS_DATA_PAGE, false, false, 10);
    }

    @Override
    public void fill_page(Evaluation evaluation) {
        List<Cell> labels = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.PART_OF_SPEECH),
                new LabelCell(LabelConstant.WORD),
                new LabelCell(LabelConstant.SUM_ACROSS_ALL_CHUNKS)));

        for (Chunk chunk : evaluation.getChunks()) {
            labels.add(new Cell(chunk.getChunkName()));
        }

        addRow(labels);

        List<Cell> total = new ArrayList<>(List.of(
                new Cell(),
                new Cell("Chunk Size"),
                new Cell(evaluation.getWordCountManager().getTotalByChunkName().getTotal())));
        for (Chunk chunk : evaluation.getChunks()) {
            total.add(new Cell(evaluation.getWordCountManager().getTotalByChunkName().get(chunk.getChunkName())));
        }
        addRow(total);

        List<Cell> currRow;
        for (FunctionPhrase phrase : evaluation.getPhraseCountManager().getPossibleItems()) {
            if (phrase.getPartOfSpeech().equals(FunctionPhrase.EMPTY_PART_OF_SPEECH))
                continue;

            currRow = new ArrayList<>(List.of(
                    new Cell(phrase.getPartOfSpeech()),
                    new Cell(phrase.toString()),
                    new Cell(evaluation.getPhraseCountManager().getTotalByItem().get(phrase))));
            for (Chunk chunk : evaluation.getChunks()) {
                currRow.add(
                        new Cell(
                                evaluation.getPhraseCountManager().getCount(chunk).get(phrase)));
            }
            // view.showMessage(String.format("Page %s: %s", getName(), phrase));
            addRow(currRow);
        }
    }

}
