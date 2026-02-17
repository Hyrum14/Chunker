package app.presenter.observers.document;

import app.presenter.observers.base.SimpleServiceObserver;
import app.view.View;
import utils.objects.FunctionPhrase;

import java.util.Set;

public class AddPhraseListObserver extends SimpleServiceObserver<Set<FunctionPhrase>>
{
    public AddPhraseListObserver(View view)
    {
        super(view);
    }

    @Override
    public void handleSuccess(Set<FunctionPhrase> item)
    {
        view.showMessage(String.format("Successfully imported %d function phrases", item.size()));
        view.setFunctionPhrases(item);
    }

    @Override
    public void onFailure(Set<FunctionPhrase> item)
    {
    }
}
