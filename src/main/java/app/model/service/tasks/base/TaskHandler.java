package app.model.service.tasks.base;

import app.presenter.observers.base.SimpleServiceObserver;
import utils.logging.Logger;

public abstract class TaskHandler<R>
{
    protected SimpleServiceObserver<R> observer;
    protected R result = null;
    Logger logger;

    public TaskHandler(SimpleServiceObserver<R> observer, Logger logger)
    {
        this.observer = observer;
        this.logger = logger;
    }

    public void handleSuccess()
    {
        if (result == null)
        {
            handleFailure(null, String.format("Resource %s was not present", getName()));
            return;
        }
        observer.handleSuccess(getResult());
    }

    public void handleFailure(R item, String message)
    {
        observer.handleFailure(item, message);
    }

    public void handleException(R item, Exception exception)
    {
        observer.handleException(item, exception);
    }

    public void handleWarning(String message)
    {
        observer.handleWarning(message);
    }

    public void handleProgressUpdate(String message)
    {
        observer.handleProgressUpdate(message);
    }

    public R getResult()
    {
        return result;
    }

    public void setResult(R result)
    {
        this.result = result;
    }

    protected abstract String getName();

}
