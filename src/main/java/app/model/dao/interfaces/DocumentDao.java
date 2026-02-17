package app.model.dao.interfaces;

import utils.objects.Document;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DocumentDao
{
    void add(Document toAdd);

    Document get(int id);

    List<Document> get(List<Integer> ids);

    Optional<Integer> containsDocument(Document document);

    Optional<Integer> containsDocument(int id);

    int reserveId();

    void setSize(int id, int size);

    Collection<Document> getTable();

    boolean shouldUpdate(int id);

    void clear(Integer id);

    boolean wasDeleted(Integer id);
}
