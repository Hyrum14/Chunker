package app.view.gui.button;

import app.presenter.Presenter;
import app.view.View;
import app.view.gui.SelectedOptions;
import utils.logging.Logger;

import javax.swing.*;
import java.awt.*;

public abstract class ChunkerButton
{
    protected final Presenter presenter;
    protected final JButton jButton;
    protected final Logger buttonLogger;
    protected final View view;
    protected final SelectedOptions selectedOptions;

    public ChunkerButton(Logger buttonLogger, View view, SelectedOptions selectedOptions)
    {
        jButton = new JButton(getButtonText());
        this.presenter = selectedOptions.getPresenter();
        jButton.setName(getName());
        this.buttonLogger = buttonLogger;
        jButton.addActionListener(e ->
        {
            buttonLogger.trace(true, null);
            performAction();
            buttonLogger.trace(false, null);
        });
        this.view = view;
        this.selectedOptions = selectedOptions;
        jButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    protected abstract void performAction();

    public boolean isEnabled()
    {
        return jButton.isEnabled();
    }

    public void enable(boolean b)
    {
        jButton.setEnabled(b);
    }

    public JButton getButton()
    {

        return jButton;
    }

    abstract String getName();

    abstract String getButtonText();
}
