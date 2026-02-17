package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class PaddedIntegerOldCell extends OldCell<Integer>
{
    public final int padding;

    protected PaddedIntegerOldCell(Integer contents, int padding)
    {
        super(contents);
        this.padding = padding;
    }

    public PaddedIntegerOldCell(Integer contents)
    {
        super(contents);
        this.padding = Cell.DEFAULT_PADDING;
    }

    @Override
    public String toString()
    {
        return Cell.paddedNumber(contents, padding);
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
        return CellVariant.PADDED_INTEGER;
    }

    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the number format
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        DataFormat dataFormat = xssfCell.getSheet().getWorkbook().createDataFormat();

        // Generate the appropriate number format pattern based on the padding
        String numberFormatPattern = String.format("%0" + padding + "d", 0).replaceAll("0", "#");
        cellStyle.setDataFormat(dataFormat.getFormat(numberFormatPattern));
        return cellStyle;
    }
}
