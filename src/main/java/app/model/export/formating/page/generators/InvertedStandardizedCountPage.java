package app.model.export.formating.page.generators;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.LabelCell;
import app.model.export.formating.cell.LabelConstant;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;
import utils.objects.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InvertedStandardizedCountPage extends PageGenerator
{
    public static int MIN_LENGTH = (LabelConstant.SUM_ACROSS_ALL_CHUNKS.toString().length() + 1);
    private final int standardizedCountFactor;

    public InvertedStandardizedCountPage(int standardizedCountFactor)
    {
        super(STANDARDIZED_COUNT_PAGE, true, true, 0);
        this.standardizedCountFactor = standardizedCountFactor;
    }

    @Override
    protected void fill_page(Evaluation evaluation)
    {
        List<Cell> docNames = new ArrayList<>(List.of(
                new Cell("Document"),
                new Cell(LabelConstant.SUM_ACROSS_ALL_CHUNKS.getDisplayName() + " " + createXsDocs(evaluation))
        ));
        for (Chunk chunk : evaluation.getChunks())
        {
            docNames.add(new Cell(chunk.getDocumentName()));
        }
        addRow(docNames);

        List<Cell> labels = new ArrayList<>(List.of(
                new LabelCell(LabelConstant.CHUNK),
                new Cell(LabelConstant.SUM_ACROSS_ALL_CHUNKS + " " + createXsChunks(evaluation))
        ));

        for (Chunk chunk : evaluation.getChunks())
        {
            labels.add(new Cell(chunk.getChunkName()));
        }
        addRow(labels);


        List<Cell> currRow;

        for (FunctionPhrase phrase : evaluation.getPhraseCountManager().getPossibleItems())
        {
            if (phrase.getPartOfSpeech().equals(FunctionPhrase.EMPTY_PART_OF_SPEECH))
                continue;

            int phrase_count = evaluation.getPhraseCountManager().getTotalByItem().get(phrase);
            if (phrase_count == 0)
                continue;
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(List.of(
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
            if (phrase_count == 0)
                continue;
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(
                    List.of(
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
            if (phrase_count == 0)
                continue;
            int chunk_size = evaluation.getTotalCountOfAllWords();
            currRow = new ArrayList<>(List.of(
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

    private String createXsDocs(Evaluation evaluation)
    {
        AtomicInteger max = new AtomicInteger();
        evaluation.getDocuments().forEach(document ->
        {
            max.set(Integer.max(document.getFileName().length(), max.get()));
        });
        if (max.get() <= MIN_LENGTH)
            return "";
        else
            return "x".repeat(max.get() - MIN_LENGTH);
    }

    private String createXsChunks(Evaluation evaluation)
    {
        AtomicInteger max = new AtomicInteger();
        evaluation.getChunks().forEach(document ->
        {
            max.set(Integer.max(document.getChunkName().length(), max.get()));
        });
        if (max.get() <= MIN_LENGTH)
            return "";
        else
            return "x".repeat(max.get() - MIN_LENGTH);
    }

    public double CalcStandardizedCount(int numAppearances, int totalWordsInChunk)
    {
        return (((double) numAppearances) / ((double) totalWordsInChunk)) * standardizedCountFactor;
    }


}
