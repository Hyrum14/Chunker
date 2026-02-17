package app.view.gui.panel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class GridPanel<T extends Comparable<T>> extends JPanel
{
    private final String[] columnHeaders;
    protected Set<GridRow<T>> rows;
    protected JScrollPane scrollPane;
    protected JPanel gridPanel;
    private ItemToRowConverter<T> converter;
    private Class<?>[] columnClasses;
    private int[] columnWidths;
    private JPanel footer;
    private Set<T> items;

    public GridPanel(String[] columnHeaders, Class<?>[] columnClasses, ItemToRowConverter<T> converter, int[] columnWidths)
    {
        this.columnHeaders = columnHeaders;
        this.columnWidths = columnWidths;
        this.converter = converter;
        rows = new TreeSet<>();
        setLayout(new BorderLayout());
        this.columnClasses = columnClasses;
        this.items = new HashSet<>();
    }

    protected void innit()
    {
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        redraw();
        scrollPane = new JScrollPane(gridPanel);
        scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPane.setAutoscrolls(true);
        add(scrollPane, BorderLayout.CENTER);
        addFooter();
    }

    protected abstract JPanel createTitlePanel();

    public synchronized void redraw()
    {
        gridPanel.removeAll();
        footer = null;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.VERTICAL;


        for (int i = 0; i < columnHeaders.length; i++)
        {
            JLabel headerLabel = new JLabel(columnHeaders[i]);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            gbc.gridx = i;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            headerLabel.setPreferredSize(new Dimension(columnWidths[i], headerLabel.getPreferredSize().height));
            gridPanel.add(headerLabel, gbc);
        }

        List<GridRow<T>> rowList = rows.stream().toList();
        for (gbc.gridy = 1; gbc.gridy <= rows.size(); ++gbc.gridy)
        {
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            for (Object i : rowList.get(gbc.gridy - 1).getRowData())
            {
                Component component;
                if (i instanceof Component)
                {
                    component = (Component) i;
                } else
                {
                    JLabel label = new JLabel(String.valueOf(i));
                    label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    label.setHorizontalAlignment(JLabel.LEFT);
                    component = label;
                }


                gridPanel.add(component, gbc);
                gbc.gridx += 1;
                gbc.anchor = GridBagConstraints.WEST;
            }
        }

        addFooter();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public synchronized void addRow(T item)
    {

        GridRow<T> oldRow = null;
        for (GridRow<T> row : rows)
        {
            if (row.getItem().equals(item))
            {
                oldRow = row;
                break;
            }
        }
        if (oldRow != null)
        {
            rows.remove(oldRow);
            rows.add(converter.update(item, oldRow));
            redraw();
            updateExpansion(oldRow, item);
            redraw();
        } else
        {
            GridRow<T> gridRow = converter.convertToRow(item);
            rows.add(gridRow);
            items.add(item);
            redraw();
            addExpansion(gridRow);
            redraw();
        }

    }

    protected abstract void updateExpansion(GridRow<T> newRow, T item);

    private void removeFooter()
    {
        if (footer != null)
        {
            gridPanel.remove(footer);
            footer = null;
        }
    }

    private void addFooter()
    {
        removeFooter(); // Make sure to remove the existing footer before adding a new one

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = rows.size() + 1; // Add the footer below the last row
        gbc.weighty = 1; // Make the footer fill the remaining vertical space
        gbc.fill = GridBagConstraints.VERTICAL; // Make the footer fill its display area vertically

        // Create an empty JPanel with a preferred height
        footer = new JPanel();
        footer.setPreferredSize(new Dimension(0, 0)); // Set the preferred height to 0, so it only fills the remaining space

        gridPanel.add(footer, gbc);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    protected abstract void addExpansion(GridRow<T> row);


    public List<T> getItems()
    {
        return items.stream().toList();
    }

    public Set<T> getAllItems()
    {
        return items;
    }

    public interface ItemToRowConverter<T extends Comparable<T>>
    {
        GridRow<T> convertToRow(T item);

        GridRow<T> update(T item, GridRow<T> oldRow);
    }

    public interface CellEditorProvider
    {
        TableCellEditor getCellEditor();
    }

    public static class GridRow<T extends Comparable<T>> implements Comparable<GridRow<T>>
    {
        private T item;
        private List<Object> rowData;

        public GridRow(T item, List<Object> rowData)
        {
            this.item = item;
            this.rowData = rowData;
        }

        public T getItem()
        {
            return item;
        }

        public void setItem(T item)
        {
            this.item = item;
        }

        public List<Object> getRowData()
        {
            return rowData;
        }

        public void setRowData(List<Object> rowData)
        {
            this.rowData = rowData;
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder("GridRow{");
            sb.append("item=").append(item);
            sb.append(", rowData=").append(rowData);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridRow<?> gridRow = (GridRow<?>) o;
            return Objects.equals(item, gridRow.item) && Objects.equals(rowData, gridRow.rowData);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(item, rowData);
        }

        @Override
        public int compareTo(GridRow<T> o)
        {
            return this.getItem().compareTo(o.getItem());
        }
    }
}

