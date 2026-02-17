package app.model.dao.map;

import app.model.dao.interfaces.DocumentDao;
import utils.objects.Document;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentMapDao implements DocumentDao
{
    Map<Integer, Document> data = new ConcurrentHashMap<>();
    private int nextAvailableId = 1;

    @Override
    public synchronized void add(Document toAdd)
    {
        data.put(toAdd.getId(), toAdd);
    }

    @Override
    public Document get(int id)
    {
        return data.get(id);
    }

    @Override
    public List<Document> get(List<Integer> ids)
    {
        List<Document> documents = new ArrayList<>();
        for (Integer id : ids)
        {
            documents.add(get(id));
        }
        return documents;
    }

    @Override
    public synchronized Optional<Integer> containsDocument(Document document)
    {
        for (Document candidate : data.values())
        {
            if (candidate.getFileName().equals(document.getFileName()) &&
                    candidate.getAbsolutePath().equals(document.getAbsolutePath()))
                return Optional.of(candidate.getId());
        }
        return Optional.empty();
    }

    @Override
    public synchronized Optional<Integer> containsDocument(int id)
    {
        if (data.containsKey(id))
            return Optional.of(id);
        return Optional.empty();
    }

    @Override
    public synchronized int reserveId()
    {
        int copy = nextAvailableId;
        nextAvailableId += 1;
        return copy;
    }

    @Override
    public synchronized void setSize(int id, int size)
    {
        Document doc = data.get(id);
        if (doc != null)
        {
            doc.setNumWords(size);
        }
    }

    @Override
    public Collection<Document> getTable()
    {
        return data.values();
    }


    @Override
    public void clear(Integer id)
    {
        for (Integer item : data.keySet())
        {
            if (item.equals(id))
                data.remove(item);
        }
    }

    @Override
    public synchronized boolean shouldUpdate(int id)
    {
        return get(id).getDateLastChanged().isAfter(data.get(id).getDateAdded());
    }

    @Override
    public boolean wasDeleted(Integer id)
    {

        return false;
    }
}
