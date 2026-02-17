package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import app.view.gui.GuiView;
import utils.objects.Evaluation;

public abstract class EvaluationDataPage extends PageGenerator
{
    private final int standardizedCountFactor;

    public EvaluationDataPage(int standardizedCountFactor)
    {
        super(PageGenerator.EVAL_DATA_PAGE, false, true, 0);
        this.standardizedCountFactor = standardizedCountFactor;
    }

    @Override
    protected void fill_page(Evaluation evaluation)
    {
        addRow(new LabelCell(LabelConstant.TEXT_CHUNKER), new Cell("VERSION " + GuiView.VERSION));
        addRow(new LabelCell(LabelConstant.EVALUATION_NAME), new Cell(evaluation.getName()));
        addRow(new LabelCell(LabelConstant.STANDARDIZED_COUNT_FACTOR), new Cell(standardizedCountFactor));
        addRow(new LabelCell(LabelConstant.DATE), new Cell(evaluation.getTime(), false, true));
        addRow(new LabelCell(LabelConstant.TIME), new Cell(evaluation.getTime(), true, false));
        skipRow();

        addRest(); // this adds the details from the chunking strategy
        skipRow();

        addRow(new LabelCell(LabelConstant.NUMBER_OF_DOCS), new Cell(evaluation.getDocumentIds().size()));
        addRow(new LabelCell(LabelConstant.NUMBER_OF_CHUNKS), new Cell(evaluation.getChunks().size()));
        skipRow();

        addRow(new LabelCell(LabelConstant.NUMBER_OF_WORDS), new Cell(evaluation.getTotalCountOfAllWords()));
        addRow(new LabelCell(LabelConstant.NUMBER_OF_UNIQUE_WORDS), new Cell(evaluation.getWordCountManager().getPossibleItems().size()));
        skipRow();

        addRow(new LabelCell(LabelConstant.NUMBER_OF_FUNCTION_WORDS_CHECKED), new Cell(evaluation.getPhraseCountManager().getPossibleItems().size()));
        addRow(new LabelCell(LabelConstant.NUMBER_OF_SPECIAL_PHRASES_CHECKED), new Cell(evaluation.getSpecialPhraseCountManager().getPossibleItems().size()));
        addRow(new LabelCell(LabelConstant.NUMBER_OF_PART_OF_SPEECH_CATEGORIES_CHECKED), new Cell(evaluation.getPartOfSpeechCountManager().getPossibleItems().size()));
        skipRow();

        addRow(new LabelCell(LabelConstant.NUMBER_OF_FUNCTION_WORDS_OCCURRENCES), new Cell(evaluation.getPhraseCountManager().getTotalByItem().getTotal()));
        addRow(new LabelCell(LabelConstant.NUMBER_OF_SPECIAL_PHRASES_OCCURRENCES), new Cell(evaluation.getSpecialPhraseCountManager().getTotalByItem().getTotal()));
        skipRow();

    }

    public abstract void addRest();
}
