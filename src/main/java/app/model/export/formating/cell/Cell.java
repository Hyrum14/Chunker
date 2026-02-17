package app.model.export.formating.cell;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.time.LocalDateTime;

public class Cell
{
    public static int DEFAULT_PADDING = 4;
    public int padding = 4;
    private Object value;
    private CellVariant type;

    private Cell(Object value, CellVariant type)
    {
        this.value = value;
        this.type = type;
        checkCellValue(value, type);
    }

    public Cell(String value)
    {
        this.value = value;
        this.type = CellVariant.STRING;
        checkCellValue(value, type);
    }

    public Cell(int number)
    {
        this.value = number;
        this.type = CellVariant.INTEGER;
        checkCellValue(value, type);
    }

    public Cell(int number, int padding)
    {
        this.value = number;
        this.type = CellVariant.PADDED_INTEGER;
        this.padding = padding;
        checkCellValue(value, type);
    }

    public Cell(LocalDateTime dateTime, boolean showTime, boolean showDate)
    {
        this.value = dateTime;
        if (showTime && showDate)
            this.type = CellVariant.DATE_TIME;
        else if (showDate)
            this.type = CellVariant.DATE;
        else if (showTime)
            this.type = CellVariant.TIME;
        else
            this.type = CellVariant.DATE_TIME;
        checkCellValue(value, type);
    }

    public Cell()
    {
        this.value = "";
        this.type = CellVariant.EMPTY;
    }

    public Cell(boolean value)
    {
        this.value = value;
        this.type = CellVariant.BOOLEAN;
    }

    public Cell(double value)
    {
        this.value = value;
        this.type = CellVariant.FLOAT;
    }

    public static String paddedNumber(int number)
    {
        return paddedNumber(number, DEFAULT_PADDING);
    }

    public static String paddedNumber(int number, int padding)
    {
        String format = String.format("%%0%dd", padding);
        return String.format(format, number);
    }

    public static void checkCellValue(Object value, CellVariant type)
    {
        switch (type)
        {
            case INTEGER:
            case PADDED_INTEGER:
                if (!(value instanceof Integer))
                {
                    throw new ClassCastException("Expected Integer for INTEGER or PADDED_INTEGER");
                }
                break;
            case DECIMAL:
            case FLOAT:
            case PERCENT:
                if (!(value instanceof Double))
                {
                    throw new ClassCastException("Expected Double for DECIMAL, FLOAT, or PERCENT");
                }
                break;
            case STRING:
            case UTF_8:
            case LABEL:
                if (!(value instanceof String))
                {
                    throw new ClassCastException("Expected String for STRING, UTF_8, or LABEL");
                }
                break;
            case FORMULA:
                if (!(value instanceof String))
                {
                    throw new ClassCastException("Expected String for FORMULA");
                }
                break;
            case EMPTY:
                // No type checking required for EMPTY
                break;
            case BOOLEAN:
                if (!(value instanceof Boolean))
                {
                    throw new ClassCastException("Expected Boolean for BOOLEAN");
                }
                break;
            case ERROR:
                if (!(value instanceof Byte))
                {
                    throw new ClassCastException("Expected Byte for ERROR");
                }
                break;
            case DATE:
            case TIME:
            case DATE_TIME:
                if (!(value instanceof LocalDateTime))
                {
                    throw new ClassCastException("Expected LocalDateTime for DATE, TIME, or DATE_TIME");
                }
                break;
            case DEFAULT:
                // Handle DEFAULT case if necessary
                break;
        }
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public CellVariant getType()
    {
        return type;
    }

    public void setType(CellVariant type)
    {
        this.type = type;
    }

    public void setCellValue(XSSFCell xssfCell, CellStyleFactory cellStyleFactory)
    {
        CellStyle cellStyle = cellStyleFactory.getCellStyle(type);
        xssfCell.setCellStyle(cellStyle);

        switch (type)
        {
            case INTEGER:
            case PADDED_INTEGER:
                xssfCell.setCellValue((Integer) value);
                break;
            case DECIMAL:
            case FLOAT:
            case PERCENT:
                xssfCell.setCellValue((Double) value);
                break;
            case STRING:
            case UTF_8:
            case LABEL:
                xssfCell.setCellValue((String) value);
                break;
            case FORMULA:
                xssfCell.setCellFormula((String) value);
                break;
            case EMPTY:
                xssfCell.setBlank();
                break;
            case BOOLEAN:
                xssfCell.setCellValue((Boolean) value);
                break;
            case ERROR:
                xssfCell.setCellErrorValue((Byte) value);
                break;
            case DATE:
            case TIME:
            case DATE_TIME:
                xssfCell.setCellValue((java.time.LocalDateTime) value);
                break;
            case DEFAULT:
        }
    }

    public void formatXssfCell(XSSFCell xssfCell, CellStyleFactory cellStyleFactory)
    {
        CellStyle cellStyle = cellStyleFactory.getCellStyle(type);
        xssfCell.setCellStyle(cellStyle);

        if (value != null)
        {
            checkCellValue(value, type);
            setCellValue(xssfCell, cellStyleFactory);
        }
    }


}

