package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ErrorOldCell extends OldCell<Void>
{
    public ErrorOldCell()
    {
        super(null);
    }

    @Override
    public String toString()
    {
        return "ERROR";
    }

    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Set the cell value as "ERROR"
        xssfCell.setCellValue("ERROR");

        // Create a cell style with a red background
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Apply the cell style to the cell
        xssfCell.setCellStyle(cellStyle);
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.ERROR;
    }

    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }
}

