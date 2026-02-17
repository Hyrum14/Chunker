package app.presenter.observers.evaluation;

import app.presenter.observers.base.SimpleServiceObserver;
import app.view.View;

public class SaveEvaluationObserver extends SimpleServiceObserver<String>
{
    public SaveEvaluationObserver(View view)
    {
        super(view);
    }

    @Override
    public void handleSuccess(String evaluationName)
    {
        view.showMessage(String.format("Successfully saved evaluation %s", evaluationName));
    }

    @Override
    public void onFailure(String item)
    {
        if (item != null)
            view.showMessage(String.format("Failed to save evaluation %s", item));
        else
            view.showMessage("Failed to save evaluation");
    }


}

