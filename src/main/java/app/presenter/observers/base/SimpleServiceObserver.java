package app.presenter.observers.base;

import app.view.View;

public abstract class SimpleServiceObserver<T>
{
    protected final View view;

    protected SimpleServiceObserver(View view)
    {
        this.view = view;
    }

    public void handleFailure(T item, String message)
    {
        view.showWarning(message);
        onFailure(item);
    }

    public void handleException(T item, Exception exception)
    {
        exception.printStackTrace();
        view.showError(exception.getMessage());
        onFailure(item);
    }

    public abstract void handleSuccess(T item);

    public void handleWarning(String message)
    {
        view.showWarning(message);
    }


    abstract public void onFailure(T item);

    public View getView()
    {
        return view;
    }


    public void handleProgressUpdate(String message)
    {
        view.showMessage(message);
    }

}