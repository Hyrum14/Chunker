package app.view.gui.chunking;

import app.model.chunking.ChunkingStrategy;
import app.view.gui.panel.DocumentSetObserver;
import utils.exeptions.TaskException;
import utils.logging.Logger;

import javax.swing.*;

public interface ChunkingMethodView<METHOD extends ChunkingStrategy> extends DocumentSetObserver
{

    JPanel getPanel();


    String getName();

    METHOD generateStrategy(Logger chunkingStrategyLogger) throws TaskException;

}
