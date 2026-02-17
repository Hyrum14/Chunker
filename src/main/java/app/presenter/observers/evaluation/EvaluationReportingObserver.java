package app.presenter.observers.evaluation;

import app.presenter.observers.base.SimpleServiceObserver;
import app.view.View;
import utils.objects.Evaluation;

public class EvaluationReportingObserver extends SimpleServiceObserver<Evaluation>
{
    public EvaluationReportingObserver(View view)
    {
        super(view);
    }

    @Override
    public void handleSuccess(Evaluation item)
    {
        view.reportEvaluationStateChanged(item);
    }

    @Override
    public void onFailure(Evaluation item)
    {
        item.setFailed();
        view.reportEvaluationStateChanged(item);
    }
}
