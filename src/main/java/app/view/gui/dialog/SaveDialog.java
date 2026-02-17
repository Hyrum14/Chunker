package app.view.gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SaveDialog
{
    private boolean overwriteAll = false;
    private boolean canceled = false;

    public File showSaveDialog(Frame parent, File existingFile, String ending)
    {
        if (!checkIfExists(existingFile))
            return existingFile;

        List<String> options = Arrays.asList(Result.CHANGE_NAME.toString(), Result.OVERWRITE.toString(), Result.OVERWRITE_ALL.toString(), Result.CANCEL.toString());

        int choice = JOptionPane.showOptionDialog(
                parent,
                "The file already exists. What would you like to do?",
                "Save Dialog",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options.toArray(),
                options.get(0));

        if (options.get(choice).equals(Result.CHANGE_NAME.toString()) && !existingFile.isDirectory())
        {
            String newName = JOptionPane.showInputDialog(parent, "Enter a new name:", existingFile.getName());
            if (newName != null && !newName.isEmpty())
            {
                if (!newName.endsWith(ending))
                    newName += ending;
                existingFile = new File(existingFile.getParentFile(), newName);
            }
            return showSaveDialog(parent, existingFile, ending);
        }

        if (options.get(choice).equals(Result.OVERWRITE_ALL.toString()))
        {
            overwriteAll = true;
        }

        if (options.get(choice).equals(Result.CANCEL.toString()))
        {
            canceled = true;
        }

        return existingFile;
    }

    public boolean isOverwriteAll()
    {
        return overwriteAll;
    }

    private boolean checkIfExists(File existingFile)
    {
        return existingFile != null && existingFile.exists();
    }

    public boolean isCanceled()
    {
        return canceled;
    }

    public enum Result
    {
        NO_CHANGES("No Changes Needed"),
        OVERWRITE("Overwrite"),
        CANCEL("Cancel"),
        CHANGE_NAME("Change Name"),
        OVERWRITE_ALL("Overwrite All");

        private final String displayName;

        Result(String displayName)
        {
            this.displayName = displayName;
        }

        @Override
        public String toString()
        {
            return displayName;
        }
    }
}
