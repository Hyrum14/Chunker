package app.view.gui.panel;

import utils.objects.Document;

import java.util.List;

public interface DocumentSetObserver
{
    void notifySelectedDocumentsChanged(List<Document> documents);
}
