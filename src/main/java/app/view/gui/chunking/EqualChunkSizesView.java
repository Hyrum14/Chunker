package app.view.gui.chunking;

import app.model.chunking.EqualChunkSizes;
import utils.exeptions.TaskException;
import utils.logging.Logger;
import utils.objects.Document;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EqualChunkSizesView implements ChunkingMethodView<EqualChunkSizes>
{
    int targetChunkSize = 1000;
    JSpinner targetChunkSizeSpinner;
    private JPanel panel;

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
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(targetChunkSize, 1, Integer.MAX_VALUE, 1);

        // Step 2: Create a JSpinner for the number field
        targetChunkSizeSpinner = new JSpinner(spinnerModel);

        // Step 3: Add the components to the strategy panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Target Chunk Size:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(targetChunkSizeSpinner, gbc);

        return panel;
    }

    @Override
    public String getName()
    {
        return "Equal Chunk Sizes";
    }

    @Override
    public EqualChunkSizes generateStrategy(Logger chunkingStrategyLogger) throws TaskException
    {
        System.out.println(targetChunkSize);
        return new EqualChunkSizes((int) targetChunkSizeSpinner.getValue(), chunkingStrategyLogger);
    }

    @Override
    public void notifySelectedDocumentsChanged(List<Document> documents)
    {

    }
}
