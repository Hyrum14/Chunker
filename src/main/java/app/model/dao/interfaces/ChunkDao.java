package app.model.dao.interfaces;

import utils.objects.chunk.Chunk;

import java.util.Collection;

public interface ChunkDao
{
    void add(Chunk chunk);

    Chunk get(String name);

    Collection<Chunk> getTable();
}
