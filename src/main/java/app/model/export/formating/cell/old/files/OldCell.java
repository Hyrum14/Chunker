package app.model.export.formating.cell.old.files;


import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

public abstract class OldCell<CONTENT_TYPE>
{
    protected CONTENT_TYPE contents;

    protected OldCell(CONTENT_TYPE contents)
    {
        this.contents = contents;
    }

    public CONTENT_TYPE getContents()
    {
        return contents;
    }

    public abstract String toString();

    public abstract void formatXssfCell(XSSFCell xssfCell);

    public abstract CellVariant getCellVariant();

    public abstract CellStyle createStyle(XSSFCell xssfCell);
}
