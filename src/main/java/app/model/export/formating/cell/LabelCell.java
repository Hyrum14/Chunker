package app.model.export.formating.cell;

public class LabelCell extends Cell
{
    public LabelCell(LabelConstant label)
    {
        super(label.getDisplayName());
    }
}

