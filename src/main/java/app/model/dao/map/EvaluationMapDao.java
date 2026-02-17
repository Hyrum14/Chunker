package app.model.dao.map;

import app.model.dao.interfaces.EvaluationDao;
import utils.objects.Evaluation;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

public class EvaluationMapDao implements EvaluationDao
{
    Collection<Evaluation> data = new ConcurrentSkipListSet<>();

    @Override
    public synchronized void add(Evaluation toAdd)
    {
        data.add(toAdd);
    }

    @Override
    public Collection<Evaluation> getTable()
    {
        return data;
    }
}
