package app.view.gui.button;

import app.model.dao.interfaces.DocumentDao;
import app.model.chunking.ChunkingStrategy;
import app.view.View;
import app.view.gui.SelectedOptions;
import utils.UtilityManager;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.Evaluation;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PerformEvaluationButton extends ChunkerButton
{
    public static final String NAME = "PERFORM_EVALUATION_BUTTON";
    public static final String TEXT = "Perform Evaluation";

    public PerformEvaluationButton(Logger buttonLogger, View view, SelectedOptions selectedOptions)
    {
        super(buttonLogger, view, selectedOptions);
    }

    @Override
    String getName()
    {
        return NAME;
    }

    @Override
    String getButtonText()
    {
        return TEXT;
    }

    @Override
    protected void performAction()
    {

        ChunkingStrategy chunkingStrategy;
        try
        {
            chunkingStrategy = selectedOptions.getSelectedChunkingStrategy();
        } catch (TaskException ex)
        {
            ex.printStackTrace();
            return;
        }

        String evaluationName = JOptionPane.showInputDialog(null, "Enter the evaluation name:");

        if (evaluationName == null)
            return;

        if (evaluationName.equals("") || selectedOptions.getCachedEvaluations().stream().map(Evaluation::getName).collect(Collectors.toSet()).contains(evaluationName))
        {
            view.showError("Evaluation requires a unique name");
            return;
        }
        List<Integer> documentIds = selectedOptions.getSelectedDocuments().stream().toList();

        Set<Document> deleted = new HashSet<>();
        Set<Document> updated = new HashSet<>();
        DocumentDao documentDao = UtilityManager.getDaoFactory().getDocumentDao();
        for (Integer docId : documentIds)
        {
            if (documentDao.shouldUpdate(docId))
            {
                updated.add(documentDao.get(docId));
            }

            if (documentDao.wasDeleted(docId))
            {
                deleted.add(documentDao.get(docId));
            }
        }

        if (!deleted.isEmpty())
        {
            int deleteResponse = JOptionPane.showConfirmDialog(null, "Documents have been deleted. Do you want to continue with this evaluation?",
                    "Warning - Documents Deleted", JOptionPane.YES_NO_OPTION);
            if (deleteResponse == JOptionPane.NO_OPTION)
            {
                return; // Cancel selected, return early
            }
        }

        if (!updated.isEmpty())
        {
            int updateResponse = JOptionPane.showConfirmDialog(null, "Documents have been changed since they were added. Do you want to continue with this evaluation?",
                    "Warning - Documents Updated", JOptionPane.YES_NO_OPTION);
            if (updateResponse == JOptionPane.NO_OPTION)
            {
                return; // Cancel selected, return early
            }
        }

        Evaluation evaluation = new Evaluation(evaluationName, chunkingStrategy, selectedOptions.getSelectedDocuments().stream().toList());
        selectedOptions.beginEvaluation(evaluation);
    }
}
