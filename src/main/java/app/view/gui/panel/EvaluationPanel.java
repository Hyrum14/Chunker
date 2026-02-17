package app.view.gui.panel;

import app.model.cache.Cache;
import app.model.export.formating.page.generators.*;
import app.presenter.Presenter;
import app.view.gui.button.PerformEvaluationButton;
import app.view.gui.dialog.CheckBoxDialog;
import app.view.gui.dialog.SaveDialog;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.logging.Logger;
import utils.objects.Evaluation;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationPanel extends GridPanel<Evaluation>
{

    private final Logger logger;
    private final MessagePanel messagePanel;
    private final PerformEvaluationButton evaluationButton;
    private final ChunkingMethodSelectionPanel chunkingMethodSelectionPanel;
    private final Frame frame;
    private final Presenter presenter;
    private int countFactor = 10000;

    public EvaluationPanel(Logger logger, MessagePanel messagePanel, PerformEvaluationButton evaluationButton, ChunkingMethodSelectionPanel chunkingMethodSelectionPanel, Presenter presenter, Frame frame)
    {
        super(
                new String[]{"Save", "", "Name", "Date", "Time", "Strategy", "Document Count"},
                new Class<?>[]{JButton.class, JButton.class, String.class, String.class, String.class, String.class, String.class},
                new ItemToRowConverter<>()
                {

                    @Override
                    public GridRow convertToRow(Evaluation item)
                    {
                        return new GridRow(
                                item,
                                Arrays.asList(
                                        new JButton("Export Results"),
                                        new JButton("Export Chunks"),
                                        item.getName(),
                                        item.getTime().toLocalDate().toString(),
                                        item.getTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss a")),
                                        item.getChunkingStrategy().getName(),
                                        String.valueOf(item.getDocuments().size())
                                )
                        );
                    }

                    @Override
                    public GridRow update(Evaluation item, GridRow oldRow)
                    {
                        return new GridRow(
                                item,
                                Arrays.asList(
                                        oldRow.getRowData().get(0),
                                        oldRow.getRowData().get(1),
                                        item.getName(),
                                        item.getTime().toLocalDate().toString(),
                                        item.getTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss a")),
                                        item.getChunkingStrategy().getName(),
                                        String.valueOf(item.getDocuments().size())
                                )
                        );
                    }
                },
                new int[]{125, 125, 125, 125, 125, 125, 125});
        this.logger = logger;
        this.messagePanel = messagePanel;
        this.evaluationButton = evaluationButton;
        this.chunkingMethodSelectionPanel = chunkingMethodSelectionPanel;
        this.presenter = presenter;
        innit();
        scrollPane.setAutoscrolls(true);
        this.frame = frame;
    }


    @Override
    protected JPanel createTitlePanel()
    {
        JLabel countFactorLabel1 = new JLabel("Results reported");
        JLabel countFactorLabel2 = new JLabel("in counts per");
        JLabel countFactorLabel3 = new JLabel("10,000 words");
        JButton changeCountFactorButton = new JButton("Change");

//        countFactorLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
//        countFactorLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
//        countFactorLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeCountFactorButton.addActionListener(e ->
        {
            String input = JOptionPane.showInputDialog(null, String.format("Enter a new count factor (min 1, max %,d):", Integer.MAX_VALUE), countFactor);
            try
            {
                int newCountFactor = Integer.parseInt(input);
                if (newCountFactor >= 1)
                {
                    countFactor = newCountFactor;
                    countFactorLabel3.setText(String.format("%,d words", countFactor));
                    messagePanel.writeMessage(String.format("Set count factor of %,d", countFactor));
                } else
                {
                    JOptionPane.showMessageDialog(null, "Invalid input. Count factor must be at least 1.");
                    messagePanel.writeError("Invalid input. Count factor must be at least 1.");
                }
            } catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
                messagePanel.writeError("Invalid input. Please enter a valid integer.");
            }
        });

        JPanel titlePanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        titlePanel.setLayout(gridBagLayout);
//
//        // Add "Evaluations" label
        JLabel titleLabel = new JLabel("Evaluations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 12;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 20, 5, 20);
        gridBagLayout.setConstraints(titleLabel, constraints);
        titlePanel.add(titleLabel);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        titlePanel.add(evaluationButton.getButton(), constraints);

        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        titlePanel.add(countFactorLabel1, constraints);

        constraints.gridy = 2;
        titlePanel.add(countFactorLabel2, constraints);

        constraints.gridy = 3;
        titlePanel.add(countFactorLabel3, constraints);

        constraints.gridy = 4;
        titlePanel.add(changeCountFactorButton, constraints);


        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 10;
        JLabel label0 = new JLabel("Chunking Strategy");
//        label0.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        titlePanel.add(label0, constraints);
//        titlePanel.add(new JLabel("Chunking Strategy"));

        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridheight = 10;
//        constraints.gridwidth = 10;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 5, 0, 5);
//        titlePanel.add(chunkingMethodSelectionPanel.createMainPanel(), constraints);
//        JLabel label = new JLabel("FILL ALL SPACE");
        JPanel label = chunkingMethodSelectionPanel.createMainPanel();
//        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        titlePanel.add(label, constraints);
//
//        constraints.gridx = 0;
//        constraints.gridy = 1;
//        constraints.gridwidth = 8;
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        buttonsPanel.add(changeCountFactorButton, constraints);
//
//        GridBagConstraints buttonsConstraints = new GridBagConstraints();
//        buttonsConstraints.gridx = 0;
//        buttonsConstraints.gridy = 1;
//        buttonsConstraints.gridwidth = 8;
//        buttonsConstraints.anchor = GridBagConstraints.NORTH;
//        gridBagLayout.setConstraints(buttonsPanel, buttonsConstraints);
//        titlePanel.add(buttonsPanel);

        return titlePanel;
    }


//    @Override
//    protected JPanel createTitlePanel()
//    {
//        JPanel titlePanel = new JPanel(new BorderLayout());
//
//
//        // Add "Evaluations" label
//        JLabel titleLabel = new JLabel("Evaluations");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        titlePanel.add(titleLabel, BorderLayout.NORTH);
//
//        JPanel buttonsPanel = new JPanel();
//        BoxLayout layout = new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS);
//        buttonsPanel.setLayout(layout);
//
//        JLabel countFactorLabel1 = new JLabel("Results reported");
//        JLabel countFactorLabel2 = new JLabel("in counts per");
//        JLabel countFactorLabel3 = new JLabel("10,000 words");
//
//        JButton changeCountFactorButton = new JButton("Change");
//        changeCountFactorButton.addActionListener(e ->
//        {
//            String input = JOptionPane.showInputDialog(null, String.format("Enter a new count factor (min 1, max %,d):", Integer.MAX_VALUE), countFactor);
//            try
//            {
//                int newCountFactor = Integer.parseInt(input);
//                if (newCountFactor >= 1)
//                {
//                    countFactor = newCountFactor;
//                    countFactorLabel3.setText(String.format("%,d words", countFactor));
//                } else
//                {
//                    JOptionPane.showMessageDialog(null, "Invalid input. Count factor must be at least 1.");
//                    messagePanel.writeError("Invalid input. Count factor must be at least 1.");
//                }
//            } catch (NumberFormatException ex)
//            {
//                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
//                messagePanel.writeError("Invalid input. Please enter a valid integer.");
//            }
//        });
//        changeCountFactorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        changeCountFactorButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
//        countFactorLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
//        countFactorLabel1.setAlignmentY(Component.TOP_ALIGNMENT);
//        countFactorLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
//        countFactorLabel2.setAlignmentY(Component.TOP_ALIGNMENT);
//        countFactorLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
//        countFactorLabel3.setAlignmentY(Component.TOP_ALIGNMENT);
//
//        // Create a panel for the button and set its layout to center the button
//        JPanel changeCountFactorButtonPanel = new JPanel();
//        changeCountFactorButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        changeCountFactorButtonPanel.add(changeCountFactorButton);
//
//
////        buttonsPanel.add(evaluationButton.getButton());
//
//        JPanel countFactorPanel = new JPanel();
//        countFactorPanel.setLayout(new BoxLayout(countFactorPanel, BoxLayout.Y_AXIS));
//        countFactorPanel.setAlignmentY(Component.TOP_ALIGNMENT);
//        countFactorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add a 5-pixel border
//        countFactorPanel.add(evaluationButton.getButton());
//        countFactorPanel.add(countFactorLabel1);
//        countFactorPanel.add(countFactorLabel2);
//        countFactorPanel.add(countFactorLabel3);
//        countFactorPanel.add(changeCountFactorButton);
//        buttonsPanel.add(countFactorPanel);
//
//        buttonsPanel.add(chunkingMethodSelectionPanel.createMainPanel());
//
//        titlePanel.add(buttonsPanel, BorderLayout.CENTER);
//
//        return titlePanel;
//    }


    @Override
    protected void updateExpansion(GridRow<Evaluation> newRow, Evaluation item)
    {

    }

    @Override
    protected void addExpansion(GridRow<Evaluation> row)
    {
        fillSaveEvalButton((JButton) row.getRowData().get(0), row);
        fillSaveChunksButton((JButton) row.getRowData().get(1), row);

        SwingUtilities.invokeLater(() ->
        {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

    private void fillSaveChunksButton(JButton saveChunksButton, GridRow<Evaluation> row)
    {
        saveChunksButton.addActionListener(e ->
        {
            saveChunksButton.setEnabled(false);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a directory to save the chunks to");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f)
                {
                    return f.isDirectory();
                }

                @Override
                public String getDescription()
                {
                    return "Directories Only";
                }
            });
            File chosenFile = null;
            if (Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.SAVE_CHUNKS) != null)
            {
                chosenFile = new File(Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.SAVE_CHUNKS), row.getItem().getName());
                chosenFile.mkdir();
                fileChooser.setCurrentDirectory(chosenFile);
            }


            int result = fileChooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION)
            {
                saveChunksButton.setEnabled(true);
                return;
            }
            messagePanel.writeMessage("Exporting chunks...");

            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.isDirectory())
            {
                if (!selectedFile.exists())
                {
                    boolean created = selectedFile.mkdirs();
                    if (!created)
                    {
                        saveChunksButton.setEnabled(true);
                        // Failed to create the directory
                        messagePanel.writeError("Failed to create the directory: " + selectedFile.getAbsolutePath());
                        return;
                    }
                } else
                {
                    saveChunksButton.setEnabled(true);

                    // selectedFile exists, but it's not a directory
                    messagePanel.writeError("Must select a directory to save the chunks to");
                    return;
                }
            }

            if (chosenFile != null && !selectedFile.equals(chosenFile))
            {
                try
                {
                    Files.delete(chosenFile.toPath());
                    messagePanel.writeMessage("Empty folder was deleted");
                } catch (IOException ex)
                {
                    messagePanel.writeWarning("Created directory could not be deleted: " + ex.getMessage());
                }
            }
            presenter.recreateChunks(row.getItem(), selectedFile);
            saveChunksButton.setEnabled(true);
        });
    }

    private void fillSaveEvalButton(JButton saveEvalButton, GridRow<Evaluation> row)
    {
        saveEvalButton.addActionListener(e ->
        {
            saveEvalButton.setEnabled(false);
            String filename = row.getItem().getName() + ".xlsx";
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a location to save the file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));

            if (Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.SAVE_EVALUATION) != null)
            {
                File file = new File(Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.SAVE_EVALUATION), filename);
                fileChooser.setSelectedFile(file);
            }

            int result = fileChooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION)
            {
                saveEvalButton.setEnabled(true);
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".xlsx"))
            {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xlsx");
            }
            SaveDialog saveDialog = new SaveDialog();
            selectedFile = saveDialog.showSaveDialog(null, selectedFile, ".xlsx");
            if (saveDialog.isCanceled())
            {
                saveEvalButton.setEnabled(true);
                messagePanel.writeMessage("Canceled");
                return;
            }
            messagePanel.writeMessage("Exporting file...");

            Cache.getInstance().setMostRecentDirectory(selectedFile.getParentFile(), Cache.DirectoryKey.SAVE_EVALUATION);
            Cache.getInstance().setMostRecentDirectory(selectedFile.getParentFile(), Cache.DirectoryKey.SAVE_CHUNKS);
            XSSFWorkbook workbook = new XSSFWorkbook();

            List<PageGenerator> possiblePages = Arrays.asList(
                    row.getItem().getChunkingStrategy().getMetaDataPage(countFactor), // 0
                    new RawDataPage(), // 1
                    new RawFunctionWordDataPage(), // 2
                    new AggregateFunctionWordDataPage(), // 3
                    new SpecialPhrasesDataPage(), // 4
                    new StandardizedCountPage(countFactor), // 5
                    new InvertedStandardizedCountPage(countFactor), // 6
                    row.getItem().getChunkingStrategy().getDocumentDataPage(), // 7
                    row.getItem().getChunkingStrategy().getChunkDataPage() // 8
            );

            boolean hasFunctionPhrases = row.getItem().getPhraseCountManager().getPossibleItems().size() > 0;
            boolean hasSpecialPhrases = row.getItem().getSpecialPhraseCountManager().getPossibleItems().size() > 0;
            Map<PageGenerator, Boolean> boxInitialStates = new HashMap<>();
            boxInitialStates.put(possiblePages.get(2), hasFunctionPhrases);
            boxInitialStates.put(possiblePages.get(3), hasFunctionPhrases);
            boxInitialStates.put(possiblePages.get(4), hasSpecialPhrases);
            boxInitialStates.put(possiblePages.get(5), hasFunctionPhrases);
            boxInitialStates.put(possiblePages.get(6), hasFunctionPhrases);
            // note that if not included it will default to being checked

            CheckBoxDialog<PageGenerator> checkBoxDialog = new CheckBoxDialog<>(
                    frame,
                    possiblePages,
                    boxInitialStates
            );

            checkBoxDialog.setVisible(true);

            if (checkBoxDialog.wasCanceled())
            {
                saveEvalButton.setEnabled(true);
                messagePanel.writeMessage("Canceled");
                return;
            }

            List<PageGenerator> pages = checkBoxDialog.getSelectedItems();

            try
            {
                presenter.saveEvaluation(
                        row.getItem(),
                        workbook,
                        selectedFile,
                        countFactor,
                        pages
                );
            } catch (IOException ex)
            {
                ex.printStackTrace();
                messagePanel.writeError(ex.getMessage());
            }


            saveEvalButton.setEnabled(true);
        });
    }


}

