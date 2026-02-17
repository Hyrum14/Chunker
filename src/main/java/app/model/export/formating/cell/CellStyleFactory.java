package app.model.export.formating.cell;// CellStyleFactory.java

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class CellStyleFactory
{
    private final Workbook workbook;
    private final Map<CellVariant, CellStyle> cellStyles = new HashMap<>();

    public CellStyleFactory(Workbook workbook)
    {
        this.workbook = workbook;
    }

    public CellStyle getCellStyle(CellVariant cellVariant)
    {
        if (!cellStyles.containsKey(cellVariant))
        {
            cellStyles.put(cellVariant, createCellStyle(cellVariant));
        }
        return cellStyles.get(cellVariant);
    }

    private CellStyle createCellStyle(CellVariant cellVariant)
    {
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();

        switch (cellVariant)
        {
            case INTEGER:
                break; // default style
            case PADDED_INTEGER:
                cellStyle.setDataFormat(dataFormat.getFormat("0000")); // Apply 4-digit padding
                break;
            case DECIMAL:
                cellStyle.setDataFormat(dataFormat.getFormat("0.000000")); // Apply 2 decimal places
                break;
            case STRING:
                break; // default style
            case FORMULA:
                break; // default style
            case EMPTY:
                break; // default style
            case BOOLEAN:
                break; // default style
            case ERROR:
                break; // default style
            case DATE:
                cellStyle.setDataFormat(dataFormat.getFormat("MM/dd/yyyy"));
                break;
            case TIME:
                cellStyle.setDataFormat(dataFormat.getFormat("HH:mm:ss"));
                break;
            case DATE_TIME:
                cellStyle.setDataFormat(dataFormat.getFormat("MM/dd/yyyy HH:mm:ss"));
                break;
            case FLOAT:
                cellStyle.setDataFormat(dataFormat.getFormat("0.000000"));
                break;
            case UTF_8:
                break; // default style
            case LABEL:
                break; // default style
            case PERCENT:
                cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));
                break;
            case DEFAULT:
                break; // default style
        }

        return cellStyle;
    }
}
