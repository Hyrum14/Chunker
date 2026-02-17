package app.view.gui.chunking;

import app.model.chunking.EqualNumberOfChunks;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EqualNumberChunksView implements ChunkingMethodView<EqualNumberOfChunks>
{
    int numberOfChunks = 2;
    JSpinner numberOfChunksSpinner;
    private JPanel panel;

    public EqualNumberChunksView()
    {

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
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(numberOfChunks, 1, Integer.MAX_VALUE, 1);

        // Step 2: Create a JSpinner for the number field
        numberOfChunksSpinner = new JSpinner(spinnerModel);

        // Step 3: Add the components to the strategy panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Number of Chunks:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(numberOfChunksSpinner, gbc);

        return panel;
    }

    @Override
    public String getName()
    {
        return "Equal Number of Chunks";
    }

    @Override
    public EqualNumberOfChunks generateStrategy(Logger chunkingStrategyLogger) throws TaskException
    {
        return new EqualNumberOfChunks((int) numberOfChunksSpinner.getValue(), chunkingStrategyLogger);
    }

    @Override
    public void notifySelectedDocumentsChanged(List<Document> documents)
    {

    }
}
