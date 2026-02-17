package app.view.gui.panel;

import app.model.chunking.ChunkingStrategy;
import app.view.gui.button.PerformEvaluationButton;
import app.view.gui.chunking.*;
import utils.exeptions.TaskException;
import utils.logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChunkingMethodSelectionPanel
{
    private PerformEvaluationButton evaluationButton;
    private JComboBox<ChunkingMethodView<?>> strategySelector;
    private List<ChunkingMethodView<?>> otherMethods;
    private List<DocumentSetObserver> observers;
    private JPanel mainPanel;
    private Logger chunkingStrategyLogger;
    private JCheckBox showAdvancedStrategiesCheckbox;

    public ChunkingMethodSelectionPanel(PerformEvaluationButton evaluationButton, Logger chunkingStrategyLogger)
    {
        this.evaluationButton = evaluationButton;
        this.chunkingStrategyLogger = chunkingStrategyLogger;
        otherMethods = createOtherMethods();
        createMainPanel();
    }

    public JPanel createMainPanel()
    {
        if (mainPanel != null)
            return mainPanel;

        mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createShowAdvancedStrategies(), BorderLayout.NORTH);
        topPanel.add(createStrategySelector(), BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        mainPanel.add(createStrategyPanels(), BorderLayout.CENTER);

        return mainPanel;
    }

    private JComboBox<ChunkingMethodView<?>> createStrategySelector()
    {
        strategySelector = new JComboBox<>();
        strategySelector.setRenderer(new ChunkingMethodRenderer());
        strategySelector.setEditable(false);
        strategySelector.setToolTipText("Select Chunking Method");

        ChunkingMethodView<?> equalVariance = new EqualVarianceView(evaluationButton);
        strategySelector.addItem(equalVariance);

        return strategySelector;
    }

    private JPanel createShowAdvancedStrategies()
    {
        JPanel panel = new JPanel(new BorderLayout());
        showAdvancedStrategiesCheckbox = new JCheckBox("Show other strategies");
        showAdvancedStrategiesCheckbox.setSelected(false);

        showAdvancedStrategiesCheckbox.addActionListener(e ->
        {
            if (showAdvancedStrategiesCheckbox.isSelected())
            {
                addOtherMethods();
            } else
            {
                removeOtherMethods();
            }

            mainPanel.revalidate();
            mainPanel.repaint();
        });

        panel.add(showAdvancedStrategiesCheckbox, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStrategyPanels()
    {
        JPanel strategyPanels = new JPanel(new CardLayout());
        observers = new ArrayList<>();

        for (int i = 0; i < strategySelector.getItemCount(); i++)
        {
            ChunkingMethodView<?> method = strategySelector.getItemAt(i);
            strategyPanels.add(method.getPanel(), method.getName());
            observers.add(method);
        }

        observers.addAll(otherMethods);
        mainPanel.add(strategyPanels, BorderLayout.CENTER);

        strategySelector.addActionListener(e ->
        {
            ChunkingMethodView<?> selectedMethod = (ChunkingMethodView<?>) strategySelector.getSelectedItem();
            ((CardLayout) strategyPanels.getLayout()).show(strategyPanels, selectedMethod.getName());
        });

        return strategyPanels;
    }

    private List<ChunkingMethodView<?>> createOtherMethods()
    {
        List<ChunkingMethodView<?>> output = new ArrayList<>();
        output.add(new EqualNumberChunksView());
        output.add(new EqualChunkSizesView());
        output.add(new BagOfWordsWithReplacementView());
        return output;
    }

    private void addOtherMethods()
    {
        JPanel strategyPanels = (JPanel) mainPanel.getComponent(1);

        for (ChunkingMethodView<?> method : otherMethods)
        {
            strategyPanels.add(method.getPanel(), method.getName());
            strategySelector.addItem(method);
        }
    }

    private void removeOtherMethods()
    {
        JPanel strategyPanels = (JPanel) mainPanel.getComponent(1);

        for (ChunkingMethodView<?> method : otherMethods)
        {
            strategyPanels.remove(method.getPanel());
            strategySelector.removeItem(method);
        }
    }

    public List<DocumentSetObserver> getDocumentObservers()
    {
        return observers;
    }

    public ChunkingStrategy getSelectedChunkingStrategy() throws TaskException
    {
        ChunkingMethodView<?> selectedMethod = (ChunkingMethodView<?>) strategySelector.getSelectedItem();
        return selectedMethod.generateStrategy(chunkingStrategyLogger);
    }

    public static class ChunkingMethodRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value instanceof ChunkingMethodView)
            {
                ChunkingMethodView<?> chunkingMethodView = (ChunkingMethodView<?>) value;
                return super.getListCellRendererComponent(list, chunkingMethodView.getName(), index, isSelected, cellHasFocus);
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
