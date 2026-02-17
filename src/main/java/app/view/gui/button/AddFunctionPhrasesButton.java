package app.view.gui.button;

import app.model.cache.Cache;
import app.view.View;
import app.view.gui.SelectedOptions;
import utils.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class AddFunctionPhrasesButton extends ChunkerButton
{
    public static final String NAME = "ADD_FUNCTION_BUTTON";
    public static final String TEXT = "Add Word/Phrase List";

    public AddFunctionPhrasesButton(Logger buttonLogger, View view, SelectedOptions ops)
    {
        super(buttonLogger, view, ops);
    }

    @Override
    String getName()
    {
        return NAME;
    }

    @Override
    String getButtonText()
    {
        return TEXT;
    }

    @Override
    protected void performAction()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                String filename = file.getName().toLowerCase();
                if (file.isDirectory())
                    return true;
                return filename.endsWith(".csv") || filename.endsWith(".tsv") || filename.endsWith(".xls") || filename.endsWith(".xlsx");
            }

            @Override
            public String getDescription()
            {
                return "Spreadsheet Files (*.csv, *.tsv, *.xls, *.xlsx)";
            }
        });

        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDragEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.FUNCTION_PHRASE) != null)
        {
            File file = new File(Cache.getInstance().getLastViewedDirectory(Cache.DirectoryKey.FUNCTION_PHRASE));
            if (file.isDirectory())
                fileChooser.setCurrentDirectory(file);
            else
                fileChooser.setSelectedFile(file);
        }


        int returnValue = fileChooser.showOpenDialog(jButton);
        buttonLogger.logItem("returnValue", returnValue);
        if (returnValue != JFileChooser.APPROVE_OPTION)
        {
            return;
        }
        File file = fileChooser.getSelectedFile();
        presenter.addPhraseList(file);
        Cache.getInstance().setMostRecentDirectory(file, Cache.DirectoryKey.FUNCTION_PHRASE);
    }
}
