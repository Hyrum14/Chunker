package test;

import javax.swing.*;
import java.awt.*;

public class TextPanelExample
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(TextPanelExample::createAndShowGUI);
    }

    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Text Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a text area with a long paragraph of text
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco.");

        // Create a scroll pane and add the text area to it
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create a button and center it
        JButton button = new JButton("Click Me");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a panel for the button and set its layout to center the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(button);

        // Add the scroll pane and button panel to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}

