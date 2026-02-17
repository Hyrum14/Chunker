package app.presenter.observers.evaluation;

import app.presenter.observers.base.GroupServiceObserver;
import app.view.View;
import utils.objects.chunk.Chunk;

import java.util.Set;

public class RecreateChunksObserver extends GroupServiceObserver<Chunk>
{
    public RecreateChunksObserver(View view, int groupSize)
    {
        super(view, groupSize);
    }

    @Override
    public String groupName()
    {
        return "Recreated Chunks";
    }

    @Override
    public String itemName()
    {
        return "Recreated Chunk";
    }

    @Override
    public void handleSuccess(Set<Chunk> item)
    {

    }

    @Override
    public void onFailure(Set<Chunk> item)
    {

    }

}
