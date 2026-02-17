package app.view.gui.panel;

import utils.logging.LogLevel;
import utils.logging.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class MessagePanel extends JPanel
{
    private static final String OLD = "_OLD";
    private static final String MESSAGE = "Message";
    private static final String WARNING = "Warning";
    private static final String ERROR = "Error";
    private static final String FONT_FAMILY = "Consolas";
    private static final int FONT_SIZE = 14;
    private static final Color WARNING_COLOR = new Color(255, 140, 0); // Darker shade of orange

    private final Logger logger;
    private int lastLen = 0;
    private String lastFormat = "";
    private String lastStr = "";
    private int count = 0;

    private JTextPane textPane;
    private JScrollPane majorScrollPane;

//    private JTextPane recentMessageText;


    public MessagePanel(Logger logger)
    {
        super(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);
        initStyles();

        majorScrollPane = new JScrollPane(textPane);
        majorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        majorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        majorScrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));

//        recentMessageText = new JTextPane();
//        recentMessageText.setText("            ");
//        recentMessageText.setEditable(false);
//        recentMessageText.setBackground(UIManager.getColor("Panel.background")); // Match the background color of the panel
//        JScrollPane recentMessageScrollPane = new JScrollPane(recentMessageText);
//        recentMessageScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding

//        JPanel recentMessagePanel = new JPanel(new BorderLayout());
//        recentMessagePanel.setBorder(BorderFactory.createTitledBorder("Most Recent Message"));
//        recentMessagePanel.add(recentMessageScrollPane, BorderLayout.CENTER);

        add(majorScrollPane, BorderLayout.CENTER);
//        add(recentMessagePanel, BorderLayout.SOUTH);

        this.logger = logger;
    }


    private void initStyles()
    {
        Style defaultStyle = textPane.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(defaultStyle, FONT_FAMILY);
        StyleConstants.setFontSize(defaultStyle, FONT_SIZE);
        StyleConstants.setBold(defaultStyle, true);

        Style messageStyle = createStyle(MESSAGE, defaultStyle, Color.BLACK);
        Style warningStyle = createStyle(WARNING, defaultStyle, WARNING_COLOR);
        Style errorStyle = createStyle(ERROR, defaultStyle, Color.RED);

        createOldStyle(MESSAGE, messageStyle);
        createOldStyle(WARNING, warningStyle);
        createOldStyle(ERROR, errorStyle);
    }

    private Style createStyle(String name, Style parent, Color color)
    {
        Style style = textPane.addStyle(name, parent);
        StyleConstants.setForeground(style, color);
        return style;
    }

    private void createOldStyle(String name, Style parent)
    {
        Style oldStyle = textPane.addStyle(name + OLD, parent);
        StyleConstants.setBold(oldStyle, false);
    }

    public JTextPane getTextPane()
    {
        return textPane;
    }

    public void writeMessage(String message)
    {
        count++;
        appendText(formatText(message), MESSAGE);
        logger.log(LogLevel.TRACE, formatText("Message: " + message));
//        recentMessageText.setText("Message: " + message);
    }

    public void writeWarning(String warning)
    {
        count++;
        appendText(formatText("Warning: " + warning), WARNING);
        logger.log(LogLevel.WARNING, formatText("Warning: " + warning));
//        recentMessageText.setText("Warning: " + warning);
    }

    public void writeError(String error)
    {
        count++;
        appendText(formatText("Error: " + error), ERROR);
        logger.log(LogLevel.ERROR, formatText("Error: " + error));
//        recentMessageText.setText("Error: " + error);
    }


    private String formatText(String text)
    {
        return "[" + count + "] " + text + System.lineSeparator();
    }

    private void appendText(String text, String style)
    {
        Document document = textPane.getDocument();
        try
        {
            if (lastLen != 0)
            {
                removeLastEntry(document);
                insertText(document, lastStr, textPane.getStyle(lastFormat + OLD));
            }

            lastLen = text.length();
            lastStr = text;
            lastFormat = style;

            insertText(document, text, textPane.getStyle(style));
            scrollToBottom();
        } catch (BadLocationException e)
        {
            e.printStackTrace();
            logger.log(LogLevel.ERROR, e.getMessage());
        }
    }

    private void removeLastEntry(Document document) throws BadLocationException
    {
        document.remove(document.getLength() - lastLen, lastLen);
    }

    private void insertText(Document document, String text, Style style) throws BadLocationException
    {
        document.insertString(document.getLength(), text, style);
        textPane.setCaretPosition(document.getLength());
    }

    private void scrollToBottom()
    {
        SwingUtilities.invokeLater(() ->
        {
            JScrollBar verticalScrollBar = majorScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

}
