package app.presenter.observers.base;

import app.view.View;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GroupServiceObserver<ITEM> extends SimpleServiceObserver<Set<ITEM>>
{
    protected final int groupSize;
    protected Set<ITEM> completed;
    protected AtomicInteger failedCount = new AtomicInteger(0);

    public GroupServiceObserver(View view, int groupSize)
    {
        super(view);
        this.groupSize = groupSize;
        completed = ConcurrentHashMap.newKeySet();
    }


    public synchronized void reportSuccess(ITEM item)
    {
        completed.add(item);
        onSuccessReported(item);
        if (completed.size() + failedCount.get() == groupSize)
            reportCompletion();
    }

    public synchronized void reportFailure(ITEM item)
    {
        if (item == null)
            view.showError(String.format("%s failed", groupName()));
        else
            view.showError(String.format("%s failed", groupName()));
        failedCount.incrementAndGet();
        onFailureReported(item);
        if (completed.size() + failedCount.get() == groupSize)
            reportCompletion();

    }

    public synchronized void reportCompletion()
    {
        view.showMessage(String.format("%s %d / %d complete, %d failed", groupName(), completed.size(), groupSize, failedCount.get()));
        handleSuccess(completed);

    }

    public void onSuccessReported(ITEM item)
    {
    }

    public void onFailureReported(ITEM item)
    {
    }

    public abstract String groupName();

    public abstract String itemName();

    public MemberObserver<ITEM> createMemberObserver()
    {
        return new MemberObserver<>(view, this, itemName());
    }

    public static class MemberObserver<ITEM> extends SimpleServiceObserver<ITEM>
    {

        private final String itemName;
        private final GroupServiceObserver<ITEM> groupObserver;

        protected MemberObserver(View view, GroupServiceObserver<ITEM> groupObserver, String itemName)
        {
            super(view);
            this.itemName = itemName;
            this.groupObserver = groupObserver;
        }

        @Override
        public void handleSuccess(ITEM item)
        {
            view.showSubMessage(String.format("%s %s is complete", this.itemName(), item.toString()));
            this.groupObserver.reportSuccess(item);
        }

        @Override
        public void onFailure(ITEM item)
        {
            if (item != null)
                view.showWarning(String.format("%s of %s failed", this.itemName(), item));
            else
                view.showWarning(String.format(" %s failed", this.itemName()));
            this.groupObserver.reportFailure(item);
        }

        public String itemName()
        {
            return itemName;
        }
    }
}