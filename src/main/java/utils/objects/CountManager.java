package utils.objects;

import utils.objects.chunk.Chunk;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CountManager<T extends Comparable<T>>
{
    private Set<T> items = new TreeSet<>();
    private CountResult<T> totalByItem = new CountResult<>();
    private Map<Chunk, CountResult<T>> countByChunk = new TreeMap<>();
    private CountResult<String> totalByChunkName = new CountResult<>();

    public CountResult<String> getTotalByChunkName()
    {
        return totalByChunkName;
    }

    public synchronized void addPossibleItem(T candidates)
    {
        items.add(candidates);
    }


    public synchronized Set<T> getPossibleItems()
    {
        return items;
    }

    public synchronized CountResult<T> getTotalByItem()
    {
        return totalByItem;
    }

    public synchronized CountResult<T> getCount(Chunk chunk)
    {
        return countByChunk.getOrDefault(chunk, new CountResult<>());
    }

    public synchronized void add(Chunk chunk, CountResult<T> countResult)
    {
        totalByItem.addAllResults(countResult);
        countByChunk.putIfAbsent(chunk, new CountResult<>());
        countByChunk.get(chunk).addAllResults(countResult);
        totalByChunkName.add(chunk.getChunkName(), countResult.getTotal());
        items.addAll(countResult.getKeys());
    }

    public synchronized void add(Chunk chunk, T item, int count)
    {
        totalByItem.add(item, count);
        countByChunk.putIfAbsent(chunk, new CountResult<>());
        countByChunk.get(chunk).add(item, count);
        totalByChunkName.add(chunk.getChunkName(), count);
        items.add(item);
    }
}
