package app.model.service.executor;

import app.model.service.tasks.base.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutor
{
    private static ThreadPoolExecutor instance;
    private ExecutorService executorService;

    private ThreadPoolExecutor()
    {
        // Create a fixed thread pool with 20 threads
        executorService = Executors.newFixedThreadPool(20);
    }

    public static synchronized ThreadPoolExecutor getInstance()
    {
        if (instance == null)
        {
            instance = new ThreadPoolExecutor();
        }
        return instance;
    }

    public void execute(Task<?> task)
    {
        executorService.execute(() ->
        {
            try
            {
                task.run();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
}
