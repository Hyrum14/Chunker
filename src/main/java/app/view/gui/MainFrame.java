package app.view.gui;

import javax.swing.*;
import java.awt.*;

public abstract class MainFrame extends JFrame
{
    private final String version;
    protected JSplitPane splitPane;
    protected JSplitPane nestedSplitPane;

    public MainFrame(String version)
    {
        this.version = version;
    }

    protected void innitFrame()
    {

        setTitle(String.format("Chunker (Version %s)", version));

        // Create an ImageIcon object with the path to your .ico file
        ImageIcon icon = new ImageIcon("icon.ico");
        setIconImage(icon.getImage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);


        // Split Pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        getContentPane().add(splitPane, BorderLayout.CENTER);


        // Right section with MESSAGES
        JPanel messagesPanel = createMessagesPanel();
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        splitPane.setRightComponent(messagesPanel);
//        JPanel messagesPanel = createMessagesPanel();
//        messagesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        JScrollPane messagesScrollPane = new JScrollPane(messagesPanel);
//        messagesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        messagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        splitPane.setRightComponent(messagesScrollPane);

        // Left section with DOCUMENTS and EVALUATIONS
        JPanel leftPanel = new JPanel(new BorderLayout());
        splitPane.setLeftComponent(leftPanel);

        // Nested Split Pane for DOCUMENTS and EVALUATIONS
        nestedSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPanel.add(nestedSplitPane, BorderLayout.CENTER);


        // DOCUMENTS section
        JPanel documentsPanel = createDocumentsPanel();
        documentsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane documentsScrollPane = new JScrollPane(documentsPanel);
        documentsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        documentsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        nestedSplitPane.setTopComponent(documentsScrollPane);

        // EVALUATIONS section
        JPanel evaluationsPanel = createEvaluationsPanel();
        evaluationsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane evaluationsScrollPane = new JScrollPane(evaluationsPanel);
        evaluationsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        evaluationsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        nestedSplitPane.setBottomComponent(evaluationsScrollPane);
    }


    protected abstract JPanel createMessagesPanel();

    protected abstract JPanel createEvaluationsPanel();

    protected abstract JPanel createDocumentsPanel();

    protected void fixSplitPlaneLocations()
    {
        nestedSplitPane.setDividerLocation(0.5);
        splitPane.setDividerLocation(0.66);
    }
}
