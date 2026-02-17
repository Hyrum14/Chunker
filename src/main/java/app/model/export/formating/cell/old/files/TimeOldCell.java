package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeOldCell extends OldCell<LocalTime>
{
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

    public TimeOldCell(LocalTime contents)
    {
        super(contents);
    }

    @Override
    public String toString()
    {
        return contents.format(formatter);
    }

    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Set the cell value
        xssfCell.setCellValue(contents.toString());
        xssfCell.setCellStyle(OldCellStyleFactory.getCellStyle(getCellVariant(), this, xssfCell));
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.TIME;
    }

    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the time format
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        DataFormat dataFormat = xssfCell.getSheet().getWorkbook().createDataFormat();

        // Use the DateTimeFormatter's pattern for the Excel time format
        String timeFormatPattern = formatter.toString();
        cellStyle.setDataFormat(dataFormat.getFormat(timeFormatPattern));
        return cellStyle;
    }
}

