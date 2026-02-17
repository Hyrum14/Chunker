package app.view.gui.chunking;

import app.model.chunking.BagOfWordsWithReplacement;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BagOfWordsWithReplacementView implements ChunkingMethodView<BagOfWordsWithReplacement>
{
    JSpinner numberOfChunksSpinner;
    JSpinner chunkSizeSpinner;

    JPanel panel = null;

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
        SpinnerNumberModel spinnerModelNumChunks = new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1);
        SpinnerNumberModel spinnerModelSize = new SpinnerNumberModel(1000, 1, Integer.MAX_VALUE, 1);

        // Step 2: Create a JSpinner for the base number of chunks
        numberOfChunksSpinner = new JSpinner(spinnerModelNumChunks);
        chunkSizeSpinner = new JSpinner(spinnerModelSize);

        // Step 4: Add the components to the strategy panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Chunks per Document:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(numberOfChunksSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Chunk Size:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(chunkSizeSpinner, gbc);
        return panel;
    }

    @Override
    public String getName()
    {
        return "Bag of Words (with replacement)";
    }

    @Override
    public BagOfWordsWithReplacement generateStrategy(Logger chunkingStrategyLogger) throws TaskException
    {
        int numChunks = (int) numberOfChunksSpinner.getValue();
        int chunkSize = (int) chunkSizeSpinner.getValue();

        if (numChunks <= 0)
            throw new TaskException("number of chunks must be greater than 0");

        if (chunkSize <= 0)
            throw new TaskException("chunk size must be greater than 0");

        return new BagOfWordsWithReplacement(chunkingStrategyLogger, numChunks, chunkSize);
    }


    @Override
    public void notifySelectedDocumentsChanged(List<Document> documents)
    {

    }
}
