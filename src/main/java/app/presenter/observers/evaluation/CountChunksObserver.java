package app.presenter.observers.evaluation;

import app.presenter.observers.base.GroupServiceObserver;
import app.view.View;
import utils.objects.ChunkCountResult;
import utils.objects.Evaluation;

import java.util.Set;

public class CountChunksObserver extends GroupServiceObserver<ChunkCountResult>
{
    private final EvaluationReportingObserver evaluationReportingObserver;
    private Evaluation evaluation;

    public CountChunksObserver(View view, int groupSize, EvaluationReportingObserver evaluationReportingObserver, Evaluation evaluation)
    {
        super(view, groupSize);
        this.evaluationReportingObserver = evaluationReportingObserver;
        this.evaluation = evaluation;
    }

    @Override
    public String groupName()
    {
        return "Chunk Count Task";
    }

    @Override
    public String itemName()
    {
        return "Chunk";
    }

    @Override
    public void handleSuccess(Set<ChunkCountResult> item)
    {
        evaluation.setComplete();
        evaluationReportingObserver.handleSuccess(evaluation);
    }

    @Override
    public void onFailure(Set<ChunkCountResult> item)
    {
        evaluationReportingObserver.onFailure(evaluation);
    }

    @Override
    public void onSuccessReported(ChunkCountResult item)
    {
        evaluation.addCountResult(item);
    }
}
