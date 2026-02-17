package app.view.gui.panel;

import app.view.gui.button.ChunkerButton;
import utils.logging.Logger;
import utils.objects.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class DocumentPanel extends GridPanel<Document>
{

    private final Logger logger;
    private final ChunkerButton addButton;
    private final ChunkerButton functionButton;
    private final List<DocumentSetObserver> observers;
    Map<Document, JCheckBox> boxes = new HashMap<>();


    public DocumentPanel(Logger logger, ChunkerButton addButton, ChunkerButton functionButton, List<DocumentSetObserver> observers)
    {
        super(
                new String[]{"Selected", "Directory", "Name", "Word Count"},
                new Class<?>[]{JCheckBox.class, String.class, String.class, String.class},
                new ItemToRowConverter<Document>()
                {
                    @Override
                    public GridRow<Document> convertToRow(Document item)
                    {
                        return new GridRow<>(
                                item,
                                Arrays.asList(
                                        new JCheckBox(),
                                        item.getParentDirectory(),
                                        item.getFileName(),
                                        String.valueOf(item.getNumWords())
                                ));
                    }

                    @Override
                    public GridRow<Document> update(Document item, GridRow<Document> oldRow)
                    {
                        return new GridRow<>(
                                item,
                                Arrays.asList(
                                        oldRow.getRowData().get(0),
                                        item.getParentDirectory(),
                                        item.getFileName(),
                                        String.valueOf(item.getNumWords())
                                )
                        );
                    }
                },
                new int[]{100, 100, 100, 100});
        this.observers = observers;
        this.addButton = addButton;
        this.functionButton = functionButton;
        this.logger = logger;
        innit();
    }

    private void notifyCheckBoxesChanged()
    {
        for (DocumentSetObserver observer : observers)
        {
            observer.notifySelectedDocumentsChanged(getSelectedDocuments().stream().toList());
        }
    }


    public Set<Integer> getSelectedDocumentIds()
    {
        Set<Integer> output = new TreeSet<>();
        for (Map.Entry<Document, JCheckBox> entry : boxes.entrySet())
        {
            if (entry.getValue().isSelected())
                output.add(entry.getKey().getId());
        }
        return output;
    }

    public List<Document> getSelectedDocuments()
    {
        Set<Document> output = new TreeSet<>();
        for (Map.Entry<Document, JCheckBox> entry : boxes.entrySet())
        {
            if (entry.getValue().isSelected())
                output.add(entry.getKey());
        }
        return output.stream().toList();
    }


    @Override
    protected JPanel createTitlePanel()
    {
        JPanel titlePanel = new JPanel(new BorderLayout());

        // Add "Documents" label
        JLabel titleLabel = new JLabel("Documents");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        // Add Check All and Uncheck All buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton.getButton());
        buttonsPanel.add(functionButton.getButton());
        JButton checkAllButton = new JButton("Check All");
        checkAllButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (JCheckBox box : boxes.values())
                {
                    if (!box.isSelected())
                        box.doClick();
                    box.repaint();
                    box.revalidate();
                }
            }
        });
        buttonsPanel.add(checkAllButton);

        JButton uncheckAllButton = new JButton("Uncheck All");
        uncheckAllButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (JCheckBox box : boxes.values())
                {
                    if (box.isSelected())
                        box.doClick();
                    box.repaint();
                    box.revalidate();
                }
            }
        });
        buttonsPanel.add(uncheckAllButton);

        titlePanel.add(buttonsPanel, BorderLayout.CENTER);

        return titlePanel;
    }

    @Override
    protected void updateExpansion(GridRow<Document> newRow, Document item)
    {
        notifyCheckBoxesChanged();
    }


    @Override
    protected void addExpansion(GridRow<Document> row)
    {
        JCheckBox box = (JCheckBox) row.getRowData().get(0);
        box.addActionListener(e ->
        {
            notifyCheckBoxesChanged();
        });
        box.doClick();
        if (!box.isSelected())
            box.doClick();
        boxes.put(row.getItem(), box);
        notifyCheckBoxesChanged();
    }


}

