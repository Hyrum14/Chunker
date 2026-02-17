package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalOldCell extends OldCell<BigDecimal>
{
    private final int precision;

    public DecimalOldCell(BigDecimal contents, int precision)
    {
        super(contents);
        this.precision = precision;
    }

    @Override
    public String toString()
    {
        return contents.setScale(precision, RoundingMode.HALF_UP).toPlainString();
    }


    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Set the cell value
        xssfCell.setCellValue(toString());
        xssfCell.setCellStyle(OldCellStyleFactory.getCellStyle(getCellVariant(), this, xssfCell));
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.DECIMAL;
    }


    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the number format with the desired precision
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        DataFormat dataFormat = xssfCell.getSheet().getWorkbook().createDataFormat();

        // Use a custom number format with the specified precision
        String numberFormat = "0." + new String(new char[precision]).replace("\0", "#");
        cellStyle.setDataFormat(dataFormat.getFormat(numberFormat));
        return cellStyle;
    }
}

