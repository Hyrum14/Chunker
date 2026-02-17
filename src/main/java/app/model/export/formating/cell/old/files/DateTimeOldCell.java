package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeOldCell extends OldCell<LocalDateTime>
{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    public DateTimeOldCell(LocalDateTime contents)
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
        xssfCell.setCellValue(toString());
        xssfCell.setCellStyle(OldCellStyleFactory.getCellStyle(getCellVariant(), this, xssfCell));
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.DATE_TIME;
    }


    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the date format
        CellStyle cellStyle = xssfCell.getSheet().getWorkbook().createCellStyle();
        DataFormat dataFormat = xssfCell.getSheet().getWorkbook().createDataFormat();

        // Use the DateTimeFormatter's pattern for the Excel date format
        String dateFormatPattern = formatter.toString().replace("T", " ");
        cellStyle.setDataFormat(dataFormat.getFormat(dateFormatPattern));
        return cellStyle;
    }
}

