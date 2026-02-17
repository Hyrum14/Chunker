package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.LabelConstant;

public class LabelOldCell extends StringOldCell
{
    public LabelOldCell(LabelConstant label)
    {
        super(label.getDisplayName());
    }
}
