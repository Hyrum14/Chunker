package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class PercentOldCell extends OldCell<Float>
{
    public PercentOldCell(Float contents)
    {
        super(contents);
    }

    @Override
    public String toString()
    {
        return String.format("%.2f%%", contents * 100);
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
        return CellVariant.PERCENT;
    }


    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style for the percentage cell
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        DataFormat dataFormat = xssfCell.getSheet().getWorkbook().createDataFormat();

        // Set the cell style data format to display the value as a percentage
        cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
        return cellStyle;
    }
}
