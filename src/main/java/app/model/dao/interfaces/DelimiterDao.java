package app.model.dao.interfaces;

import utils.objects.Delimiter;

import java.util.Collection;

public interface DelimiterDao
{
    void add(Delimiter toAdd);

    Delimiter get(int index, int documentId);

    Collection<Delimiter> getTable();

    void clear(Integer documentId);
}
