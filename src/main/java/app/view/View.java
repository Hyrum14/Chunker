package app.view;

import utils.objects.Document;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;

import java.util.Set;

public interface View
{
    // Set a button state (takes a button name (string))
    void setButtonState(String buttonName, boolean isEnabled);

    void block(boolean state);

    // Close application
    boolean closeApplication();

    void addDocument(Document document);

    void open();

    void showMessage(String message);

    void showSubMessage(String message);

    void showWarning(String message);

    void showError(String message);

    String getName();

    Set<FunctionPhrase> getFunctionPhrases();

    void setFunctionPhrases(Set<FunctionPhrase> phrases);


    void reportEvaluationStateChanged(Evaluation evaluation);
}

