package app.view.gui.chunking;

import app.model.chunking.EqualVariance;
import app.view.gui.button.PerformEvaluationButton;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class EqualVarianceView implements ChunkingMethodView<EqualVariance>
{
    static final Document dummyDocument = new Document(-1, "<smallest>", "", "", -1, null, null);
    private final PerformEvaluationButton evaluationButton;
    Document selectedDocument = dummyDocument;
    List<Document> documentOptions;
    int baseNumberChunks = 2;
    private JComboBox<Object> documentSelector;
    private JPanel panel;


    public EqualVarianceView(PerformEvaluationButton evaluationButton)
    {
        this.evaluationButton = evaluationButton;
        this.documentOptions = List.of(dummyDocument);
    }

    @Override
    public String getName()
    {
        return "Equal Variance";
    }

    @Override
    public EqualVariance generateStrategy(Logger chunkingStrategyLogger) throws TaskException
    {
        if (selectedDocument == null)
            return new EqualVariance(baseNumberChunks, -1, null, chunkingStrategyLogger);
        return new EqualVariance(baseNumberChunks, selectedDocument.getId(), selectedDocument.getFileName(), chunkingStrategyLogger);
    }

    @Override
    public void notifySelectedDocumentsChanged(List<Document> documents)
    {
        if (!documents.stream().map(Document::getId).collect(Collectors.toSet()).contains(selectedDocument.getId()))
            selectedDocument = dummyDocument;

        evaluationButton.enable(!documents.isEmpty());
        documentSelector.removeAllItems();

        documentOptions = new ArrayList<>();
        documentOptions.add(dummyDocument);
        documentOptions.addAll(documents);
        documentOptions.forEach(document -> documentSelector.addItem(document));

        documentSelector.setSelectedItem(selectedDocument);
    }

    @Override
    public JPanel getPanel()
    {
        if (panel == null)
            panel = createStrategyPanel();
        return panel;
    }

    public JPanel createStrategyPanel()
    {
        // Step 1: Create a SpinnerNumberModel for the number field
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(baseNumberChunks, 1, Integer.MAX_VALUE, 1);

        // Step 2: Create a JSpinner for the base number of chunks
        JSpinner baseChunksSpinner = new JSpinner(spinnerModel);
        baseChunksSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                baseNumberChunks = (int) ((JSpinner) e.getSource()).getValue();
            }
        });

        // Step 3: Create a JComboBox for the document options
        documentSelector = new JComboBox<Object>(new Vector<>(documentOptions));
        documentSelector.addActionListener(e -> selectedDocument = (Document) documentSelector.getSelectedItem());
        documentSelector.setRenderer(new DocumentRenderer());

        // Step 4: Add the components to the strategy panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Base number of chunks:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(baseChunksSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Select document:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(documentSelector, gbc);

        return panel;
    }

    public static class DocumentRenderer extends DefaultListCellRenderer
    {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value instanceof Document document)
            {
                return super.getListCellRendererComponent(list, document.getFileName(), index, isSelected, cellHasFocus);
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }


}
