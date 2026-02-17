package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class IntegerOldCell extends OldCell<Integer>
{
    public IntegerOldCell(Integer contents)
    {
        super(contents);
    }

    @Override
    public String toString()
    {
        return String.valueOf(contents);
    }


    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Set the cell value
        xssfCell.setCellValue(contents);
        xssfCell.setCellStyle(OldCellStyleFactory.getCellStyle(getCellVariant(), this, xssfCell));
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.INTEGER;
    }


    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the number format
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();

        // Since we don't have padding, we can use a simple format for integers
        cellStyle.setDataFormat((short) 1); // Predefined format for integers in POI
        return cellStyle;
    }
}

