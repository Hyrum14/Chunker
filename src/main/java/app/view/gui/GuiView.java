package app.view.gui;

import app.model.cache.Cache;
import app.model.chunking.ChunkingStrategy;
import app.presenter.LoggingPresenter;
import app.presenter.MainPresenter;
import app.presenter.Presenter;
import app.view.View;
import app.view.gui.button.AddDocumentButton;
import app.view.gui.button.AddFunctionPhrasesButton;
import app.view.gui.button.ChunkerButton;
import app.view.gui.button.PerformEvaluationButton;
import app.view.gui.panel.ChunkingMethodSelectionPanel;
import app.view.gui.panel.DocumentPanel;
import app.view.gui.panel.EvaluationPanel;
import app.view.gui.panel.MessagePanel;
import utils.containers.Pair;
import utils.exeptions.TaskException;
import utils.logging.DummyLogger;
import utils.logging.FileLogger;
import utils.logging.Logger;
import utils.objects.Document;
import utils.objects.Evaluation;
import utils.objects.FunctionPhrase;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiView extends MainFrame implements View, SelectedOptions {
    public static final String VERSION = "1057";
    private Logger buttonLogger;
    private Logger guiLogger;
    private Logger chunkingStrategyLogger;

    private Presenter presenter;

    private ChunkerButton addButton = null;
    private PerformEvaluationButton evaluationButton = null;

    private MessagePanel messagePanel = null;
    private DocumentPanel documentPanel = null;
    private EvaluationPanel evaluationPanel = null;
    // Add a new instance variable
    private ChunkingMethodSelectionPanel chunkingMethodSelectionPanel = null;
    private Set<FunctionPhrase> phrases;
    private AddFunctionPhrasesButton functionButton;

    public GuiView(String version) {
        super(version);
        Logger serviceLogger;
        Logger presenterLogger;
        try {
            // Create logs directory in user home if it doesn't exist
            File logsDir = new File(System.getProperty("user.home"), ".chunker/logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }

            String logPath = logsDir.getAbsolutePath();
            buttonLogger = new FileLogger(new File(logPath, "Button_Log.log").getAbsolutePath());
            guiLogger = new FileLogger(new File(logPath, "Gui_Log.log").getAbsolutePath());
            presenterLogger = new FileLogger(new File(logPath, "Presenter_Log.log").getAbsolutePath());
            serviceLogger = new FileLogger(new File(logPath, "Presenter_Log.log").getAbsolutePath());
            chunkingStrategyLogger = new FileLogger(
                    new File(logPath, "Chunking_Strategy_Logger.log").getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            String message = "Failed to setup logger: " + e.getMessage();
            System.err.println(message);
            this.showError(message);
            guiLogger = new DummyLogger();
            buttonLogger = new DummyLogger();
            presenterLogger = new DummyLogger();
            serviceLogger = new DummyLogger();
        }
        this.presenter = new LoggingPresenter(presenterLogger, new MainPresenter(this, serviceLogger));
        innitFrame();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiView guiView = new GuiView(VERSION);
            guiView.setVisible(true);
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        fixSplitPlaneLocations();
    }

    @Override
    public void setButtonState(String buttonName, boolean isEnabled) {

    }

    @Override
    public void block(boolean state) {

    }

    @Override
    public boolean closeApplication() {
        Cache.getInstance().saveConfig();
        return false;
    }

    @Override
    public void addDocument(Document document) {
        SwingUtilities.invokeLater(() -> documentPanel.addRow(document));
    }

    @Override
    public void open() {
        Cache.getInstance().loadConfig();
    }

    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            guiLogger.trace(true, new Pair[] { new Pair<>("message", message) });
            messagePanel.writeMessage(message);
            guiLogger.trace(false, null);
        });

    }

    @Override
    public void showSubMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            guiLogger.trace(true, new Pair[] { new Pair<>("message", message) });
            messagePanel.writeMessage(message);
            guiLogger.trace(false, null);
        });

    }

    @Override
    public void showWarning(String message) {
        SwingUtilities.invokeLater(() -> {
            guiLogger.trace(true, new Pair[] { new Pair<>("warning", message) });
            messagePanel.writeWarning(message);
            guiLogger.trace(false, null);
        });
    }

    @Override
    public void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            guiLogger.trace(true, new Pair[] { new Pair<>("error", message) });
            messagePanel.writeError(message);
            guiLogger.trace(false, null);
        });
    }

    @Override
    public List<Document> getCachedDocuments() {
        return documentPanel.getItems();
    }

    @Override
    public List<Evaluation> getCachedEvaluations() {
        return evaluationPanel.getItems();
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    @Override
    public Set<Integer> getSelectedDocuments() {
        return documentPanel.getSelectedDocumentIds();
    }

    @Override
    public ChunkingStrategy getSelectedChunkingStrategy() throws TaskException {
        return chunkingMethodSelectionPanel.getSelectedChunkingStrategy();
    }

    @Override
    public Set<FunctionPhrase> getFunctionPhrases() {
        return phrases;
    }

    @Override
    public void setFunctionPhrases(Set<FunctionPhrase> phrases) {
        if (this.phrases != null && this.phrases.equals(phrases))
            return;
        this.phrases = phrases;
        Set<File> documents = documentPanel.getAllItems().stream().map(doc -> new File(doc.getAbsolutePath()))
                .collect(Collectors.toSet());

        if (documents.size() == 0)
            return;

        showMessage("Recounting documents...");
        presenter.addDocuments(documents, phrases, true);
    }

    @Override
    public void beginEvaluation(Evaluation evaluation) {
        reportEvaluationStateChanged(evaluation);
    }

    @Override
    public void reportEvaluationStateChanged(Evaluation evaluation) {

        switch (evaluation.getState()) {
            case CREATED ->
                presenter.generateChunks(evaluation, evaluation.getDocumentIds(), evaluation.getChunkingStrategy());
            case GENERATED_CHUNKS -> presenter.countChunks(evaluation, evaluation.getChunks());
            case COMPLETE -> SwingUtilities.invokeLater(() -> evaluationPanel.addRow(evaluation));
            case FAILED -> {

            }
            default -> throw new IllegalStateException("Unexpected value: " + evaluation.getState());
        }
    }

    @Override
    protected JPanel createMessagesPanel() {
        if (messagePanel == null)
            messagePanel = new MessagePanel(guiLogger);
        return messagePanel;
    }

    @Override
    protected JPanel createEvaluationsPanel() {
        if (evaluationButton == null) {
            evaluationButton = new PerformEvaluationButton(buttonLogger, this, this);
        }

        if (chunkingMethodSelectionPanel == null) {
            chunkingMethodSelectionPanel = new ChunkingMethodSelectionPanel(evaluationButton, chunkingStrategyLogger);
        }

        if (evaluationPanel == null)
            evaluationPanel = new EvaluationPanel(guiLogger, messagePanel, evaluationButton,
                    chunkingMethodSelectionPanel, presenter, this);
        return evaluationPanel;
    }

    @Override
    protected JPanel createDocumentsPanel() {

        if (addButton == null) {
            addButton = new AddDocumentButton(buttonLogger, this, this);
        }

        if (evaluationButton == null) {
            evaluationButton = new PerformEvaluationButton(buttonLogger, this, this);
        }

        if (chunkingMethodSelectionPanel == null) {
            chunkingMethodSelectionPanel = new ChunkingMethodSelectionPanel(evaluationButton, chunkingStrategyLogger);
        }

        if (functionButton == null) {
            functionButton = new AddFunctionPhrasesButton(buttonLogger, this, this);
        }

        if (documentPanel == null)
            documentPanel = new DocumentPanel(guiLogger, addButton, functionButton,
                    chunkingMethodSelectionPanel.getDocumentObservers());
        return documentPanel;
    }
}
