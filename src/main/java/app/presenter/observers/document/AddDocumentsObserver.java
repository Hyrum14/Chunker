package app.presenter.observers.document;


import app.presenter.observers.base.GroupServiceObserver;
import app.view.View;
import utils.objects.Document;

import java.util.Set;

public class AddDocumentsObserver extends GroupServiceObserver<Document>
{

    public AddDocumentsObserver(View view, int groupSize)
    {
        super(view, groupSize);
    }


    @Override
    public void onFailure(Set<Document> item)
    {

    }

    @Override
    public void handleSuccess(Set<Document> documents)
    {
    }


    @Override
    public String groupName()
    {
        return "Add Documents Task";
    }

    @Override
    public String itemName()
    {
        return "Document";
    }

    @Override
    public void onSuccessReported(Document document)
    {
        view.addDocument(document);
    }
}
