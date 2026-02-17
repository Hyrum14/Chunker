package app.model.service.tasks.base;


import utils.exeptions.TaskException;
import utils.logging.Logger;

public abstract class Task<R> implements Runnable
{
    protected TaskHandler<R> handler;
    protected Logger logger;

    public Task(TaskHandler<R> handler, Logger logger)
    {
        this.handler = handler;
        this.logger = logger;
    }

    @Override
    public void run()
    {
        try
        {
            if (evaluate())
            {
                handler.handleSuccess();
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
            handler.handleException(handler.getResult(), ex);
        }
    }

    protected abstract boolean evaluate() throws TaskException;

    protected void setResult(R result)
    {
        handler.setResult(result);
    }

    protected void handleWarning(String message)
    {
        handler.handleWarning(message);
    }

    protected void sendProgressUpdate(String message)
    {
        handler.handleProgressUpdate(message);
    }
}
