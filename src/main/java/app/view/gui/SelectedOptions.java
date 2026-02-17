package app.view.gui;

import app.presenter.Presenter;
import app.model.chunking.ChunkingStrategy;
import utils.exeptions.TaskException;
import utils.objects.Document;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;

import java.util.List;
import java.util.Set;

public interface SelectedOptions
{
    List<Document> getCachedDocuments();

    List<Evaluation> getCachedEvaluations();

    Presenter getPresenter();

    Set<Integer> getSelectedDocuments();

    ChunkingStrategy getSelectedChunkingStrategy() throws TaskException;

    Set<FunctionPhrase> getFunctionPhrases();

    void beginEvaluation(Evaluation evaluation);
}
