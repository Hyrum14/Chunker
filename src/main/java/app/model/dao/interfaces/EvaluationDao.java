package app.model.dao.interfaces;

import utils.objects.Evaluation;

import java.util.Collection;

public interface EvaluationDao
{
    void add(Evaluation toAdd);

    Collection<Evaluation> getTable();
}
