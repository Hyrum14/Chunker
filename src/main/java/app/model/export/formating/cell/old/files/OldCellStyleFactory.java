package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.HashMap;
import java.util.Map;

public class OldCellStyleFactory
{
    private static Map<CellVariant, CellStyle> cellStyleCache = new HashMap<>();

    public static CellStyle getCellStyle(CellVariant variant, OldCell<?> cell, XSSFCell xssfCell)
    {
        if (!cellStyleCache.containsKey(variant))
        {
            CellStyle cellStyle = cell.createStyle(xssfCell);
            cellStyleCache.put(variant, cellStyle);
        }
        return cellStyleCache.get(variant);
    }
}

