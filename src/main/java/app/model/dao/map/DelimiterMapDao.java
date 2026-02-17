package app.model.dao.map;

import app.model.dao.interfaces.DelimiterDao;
import utils.containers.Pair;
import utils.objects.Delimiter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelimiterMapDao implements DelimiterDao
{
    Map<Pair<Integer, Integer>, Delimiter> data = new ConcurrentHashMap<>();

    @Override
    public synchronized void add(Delimiter toAdd)
    {
        data.put(new Pair<>(toAdd.getIndex(), toAdd.getDocumentId()), toAdd);
    }

    @Override
    public Delimiter get(int index, int documentId)
    {
        return data.get(new Pair<>(index, documentId));
    }

    @Override
    public Collection<Delimiter> getTable()
    {
        return data.values();
    }

    @Override
    public void clear(Integer documentId)
    {
        for (Pair<Integer, Integer> item : data.keySet())
        {
            if (item.getSecond().equals(documentId))
                data.remove(item);
        }
    }
}
