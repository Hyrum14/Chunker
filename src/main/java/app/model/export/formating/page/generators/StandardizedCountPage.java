package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class StandardizedCountPage extends PageGenerator
{
    private final int standardizedCountFactor;

    public StandardizedCountPage(int standardizedCountFactor)
    {
        super(STANDARDIZED_COUNT_PAGE, false, false, 10);
        this.standardizedCountFactor = standardizedCountFactor;
    }

    @Override
    protected void fill_page(Evaluation evaluation)
    {
        List<Cell> labels = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.PART_OF_SPEECH),
                new LabelCell(LabelConstant.WORD),
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

        for (FunctionPhrase phrase : evaluation.getPhraseCountManager().getPossibleItems())
        {
            if (phrase.getPartOfSpeech().equals(FunctionPhrase.EMPTY_PART_OF_SPEECH))
                continue;

            int phrase_count = evaluation.getPhraseCountManager().getTotalByItem().get(phrase);
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(List.of(
                    new Cell(phrase.getPartOfSpeech()),
                    new Cell(phrase.toString()),
                    new Cell(CalcStandardizedCount(phrase_count, chunk_size))
            ));
            for (Chunk chunk : evaluation.getChunks())
            {
                phrase_count = evaluation.getPhraseCountManager().getCount(chunk).get(phrase);
                chunk_size = chunk.getSize();
                currRow.add(
                        new Cell(
                                CalcStandardizedCount(phrase_count, chunk_size)
                        )
                );
            }
            addRow(currRow);
        }

        for (String partOfSpeech : evaluation.getPartOfSpeechCountManager().getPossibleItems())
        {
            int phrase_count = evaluation.getPartOfSpeechCountManager().getTotalByItem().get(partOfSpeech);
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(
                    List.of(
                            new Cell(),
                            new Cell(partOfSpeech),
                            new Cell(
                                    CalcStandardizedCount(phrase_count, chunk_size)
                            )
                    )
            );

            for (Chunk chunk : evaluation.getChunks())
            {
                phrase_count = evaluation.getPartOfSpeechCountManager().getCount(chunk).get(partOfSpeech);
                chunk_size = chunk.getSize();
                currRow.add(
                        new Cell(
                                CalcStandardizedCount(phrase_count, chunk_size)
                        )
                );
            }

            addRow(currRow);
        }


        List<Cell> total1 = new ArrayList<>(List.of(
                new Cell(),
                new Cell("Total Function Words"),
                new Cell(CalcStandardizedCount(evaluation.getPhraseCountManager().getTotalByChunkName().getTotal(), evaluation.getWordCountManager().getTotalByItem().getTotal()))
        ));
        for (Chunk chunk : evaluation.getChunks())
        {
            int phrase_count = evaluation.getPartOfSpeechCountManager().getTotalByChunkName().get(chunk.getChunkName());
            int chunk_size = chunk.getSize();
            total1.add(new Cell(CalcStandardizedCount(phrase_count, chunk_size)));
        }
        addRow(total1);

        for (String partOfSpeech : evaluation.getSpecialPhraseCountManager().getPossibleItems())
        {
            int phrase_count = evaluation.getSpecialPhraseCountManager().getTotalByItem().get(partOfSpeech);
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(List.of(
                    new Cell(),
                    new Cell(partOfSpeech),
                    new Cell(
                            CalcStandardizedCount(phrase_count, chunk_size)
                    )
            ));
            for (Chunk chunk : evaluation.getChunks())
            {
                phrase_count = evaluation.getSpecialPhraseCountManager().getCount(chunk).get(partOfSpeech);
                chunk_size = chunk.getSize();
                currRow.add(
                        new Cell(
                                CalcStandardizedCount(phrase_count, chunk_size)
                        )
                );
            }
            addRow(currRow);
        }
    }

    public double CalcStandardizedCount(int numAppearances, int totalWordsInChunk)
    {
        return (((double) numAppearances) / ((double) totalWordsInChunk)) * standardizedCountFactor;
    }


}
