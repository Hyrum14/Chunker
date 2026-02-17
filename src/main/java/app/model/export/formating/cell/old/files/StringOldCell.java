package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class StringOldCell extends OldCell<String>
{
    public StringOldCell(String contents)
    {
        super(contents);
    }

    @Override
    public String toString()
    {
        return contents;
    }

    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Use the CreationHelper to create a RichTextString that won't be interpreted as a formula, boolean, or number
        CreationHelper creationHelper = xssfCell.getSheet().getWorkbook().getCreationHelper();
        xssfCell.setCellValue(creationHelper.createRichTextString(contents));
        xssfCell.setCellStyle(OldCellStyleFactory.getCellStyle(getCellVariant(), this, xssfCell));
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.STRING;
    }

    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        return xssfCell.getSheet().getWorkbook().createCellStyle();
    }
}

