package app.model.dao.map;

import app.model.dao.interfaces.ChunkDao;
import utils.objects.chunk.Chunk;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkMapDao implements ChunkDao
{
    Map<String, Chunk> data = new ConcurrentHashMap<>();


    @Override
    public synchronized void add(Chunk chunk)
    {
        data.put(chunk.getChunkName(), chunk);
    }

    @Override
    public synchronized Chunk get(String name)
    {
        return data.get(name);
    }

    @Override
    public Collection<Chunk> getTable()
    {
        return data.values();
    }

}
