package app.view.gui.button;

import app.model.cache.Cache;
import app.view.View;
import app.view.gui.SelectedOptions;
import utils.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddDocumentButton extends ChunkerButton
{
    public static final String NAME = "ADD_DOCUMENT_BUTTON";
    public static final String TEXT = "Add Documents";

    public AddDocumentButton(Logger buttonLogger, View view, SelectedOptions ops)
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
        // Add file filter to only allow selection of text files or directories
        fileChooser.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription()
            {
                return "Text Files (*.txt) or Directories";
            }
        });

        fileChooser.setDialogTitle("Select documents to count");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setDragEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        Cache cache = Cache.getInstance();
        if (cache.getLastViewedDirectory(Cache.DirectoryKey.FUNCTION_PHRASE) != null)
        {
            File file = new File(cache.getLastViewedDirectory(Cache.DirectoryKey.ADD_FILE));
            fileChooser.setSelectedFile(file);
//            fileChooser.setCurrentDirectory(file);
        }


        int returnValue = fileChooser.showOpenDialog(jButton);
        buttonLogger.logItem("returnValue", returnValue);
        if (returnValue != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        Set<File> files = new HashSet<>();
        for (File file : fileChooser.getSelectedFiles())
        {
            if (file.isDirectory())
            {
                files.addAll(listFilesRecursively(file));
            } else if (!file.getName().toLowerCase().endsWith(".txt"))
            {
                view.showError(String.format("Selected file %s was not a .txt file", file.getName()));
            } else
            {
                files.add(file);
            }
        }
        if (files.stream().findFirst().isEmpty())
            return;

        File dir = files.stream().findFirst().get().getParentFile();
        cache.setMostRecentDirectory(dir, Cache.DirectoryKey.ADD_FILE);

        cache.setMostRecentDirectory(dir, Cache.DirectoryKey.SAVE_EVALUATION);

        if (cache.getLastViewedDirectory(Cache.DirectoryKey.FUNCTION_PHRASE) == null || cache.getLastViewedDirectory(Cache.DirectoryKey.FUNCTION_PHRASE).isEmpty())
            cache.setMostRecentDirectory(dir, Cache.DirectoryKey.FUNCTION_PHRASE);

        cache.setMostRecentDirectory(dir, Cache.DirectoryKey.SAVE_CHUNKS);

        presenter.addDocuments(files, selectedOptions.getFunctionPhrases(), false);
    }

    private List<File> listFilesRecursively(File directory)
    {
        List<File> files = new ArrayList<>();
        File[] fileList = directory.listFiles();
        if (fileList != null)
        {
            for (File file : fileList)
            {
                if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".txt"))
                {
                    files.add(file);
                }
            }
        }
        return files;
    }
}
