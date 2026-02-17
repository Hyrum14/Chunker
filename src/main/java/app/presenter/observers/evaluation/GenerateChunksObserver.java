package app.presenter.observers.evaluation;

import app.presenter.observers.base.SimpleServiceObserver;
import app.view.View;
import utils.objects.Evaluation;
import utils.objects.chunk.Chunk;

import java.util.Set;

public class GenerateChunksObserver extends SimpleServiceObserver<Set<Chunk>>
{
    private final EvaluationReportingObserver evaluationReportingObserver;
    private Evaluation evaluation;

    public GenerateChunksObserver(View view, EvaluationReportingObserver evaluationReportingObserver, Evaluation evaluation)
    {
        super(view);
        this.evaluationReportingObserver = evaluationReportingObserver;
        this.evaluation = evaluation;

    }

    @Override
    public void handleSuccess(Set<Chunk> item)
    {
        view.showMessage("Chunks have been generated");
        evaluation.setChunks(item);
        evaluationReportingObserver.handleSuccess(evaluation);
    }

    @Override
    public void onFailure(Set<Chunk> item)
    {
        evaluationReportingObserver.onFailure(evaluation);
    }

}
