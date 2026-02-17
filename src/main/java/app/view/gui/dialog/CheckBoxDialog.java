package app.view.gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBoxDialog<T> extends JDialog
{
    private List<JCheckBox> checkBoxes;
    private List<T> items;
    private boolean canceled;


    public CheckBoxDialog(Frame parent, List<T> items, Map<T, Boolean> initialBoxStates)
    {
        super(parent, "Pages to Include", true);
        setLayout(new BorderLayout());

        this.items = items;
        checkBoxes = new ArrayList<>();
        canceled = false;

        // Create checkboxes
        for (T item : items)
        {
            JCheckBox checkBox = new JCheckBox(item.toString());
            checkBox.setSelected(initialBoxStates.getOrDefault(item, true));
            checkBoxes.add(checkBox);
        }

        // Create buttons
        JButton allButton = new JButton("All");
        JButton noneButton = new JButton("None");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        // Add checkboxes and buttons to the dialog
        JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
        for (JCheckBox checkBox : checkBoxes)
        {
            checkBoxPanel.add(checkBox);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(allButton);
        buttonPanel.add(noneButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(checkBoxPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        allButton.addActionListener(e ->
        {
            for (JCheckBox checkBox : checkBoxes)
            {
                checkBox.setSelected(true);
            }
        });

        noneButton.addActionListener(e ->
        {
            for (JCheckBox checkBox : checkBoxes)
            {
                checkBox.setSelected(false);
            }
        });

        okButton.addActionListener(e ->
        {
            dispose();  // Close the dialog
        });

        cancelButton.addActionListener(e ->
        {
            canceled = true;
            dispose();  // Close the dialog
        });

        // Set dialog properties
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);

                List<String> itemList = new ArrayList<>();
                itemList.add("Item 1");
                itemList.add("Item 2");
                itemList.add("Item 3");

                JButton showDialogButton = new JButton("Show Dialog");
                JLabel selectedItemsLabel = new JLabel("Selected Items: ");

                showDialogButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        CheckBoxDialog<String> dialog = new CheckBoxDialog<>(null, itemList, new HashMap<>());
                        dialog.setVisible(true);

                        if (dialog.wasCanceled())
                        {
                            selectedItemsLabel.setText("Dialog Canceled");
                        } else
                        {
                            List<String> selectedItems = dialog.getSelectedItems();
                            StringBuilder itemsText = new StringBuilder();
                            for (String item : selectedItems)
                            {
                                itemsText.append(item).append(", ");
                            }
                            if (itemsText.length() > 0)
                            {
                                itemsText.setLength(itemsText.length() - 2);
                            }
                            selectedItemsLabel.setText("Selected Items: " + itemsText);
                        }
                    }
                });

                JPanel panel = new JPanel();
                panel.add(showDialogButton);
                panel.add(selectedItemsLabel);

                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.setVisible(true);
            }
        });
    }

    public List<T> getSelectedItems()
    {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++)
        {
            if (checkBoxes.get(i).isSelected())
            {
                selectedItems.add(items.get(i));
            }
        }
        return selectedItems;
    }

    public boolean wasCanceled()
    {
        return canceled;
    }
}
