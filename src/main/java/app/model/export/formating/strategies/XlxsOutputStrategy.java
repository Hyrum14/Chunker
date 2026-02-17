package app.model.export.formating.strategies;

import app.model.export.formating.Page;
import app.model.export.formating.cell.Cell;
import app.model.export.formating.cell.CellStyleFactory;
import app.model.export.formating.page.generators.PageGenerator;
import app.view.View;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.objects.Coordinate;

import java.util.List;


public class XlxsOutputStrategy extends OutputStrategy
{
    XSSFWorkbook workbook;

    public XlxsOutputStrategy(List<PageGenerator> pageGenerators, XSSFWorkbook workbook, View view)
    {
        super(pageGenerators, view);
        if (workbook == null)
            throw new NullArgumentException();

        this.workbook = workbook;
    }

    @Override
    public void producePageOutput(Page page)
    {
        XSSFSheet sheet = workbook.createSheet(page.getName());
        CellStyleFactory cellStyleFactory = new CellStyleFactory(workbook);
        for (int rowIndex = 0; rowIndex <= page.getMaxRow(); rowIndex++)
        {
            XSSFRow xssfRow = sheet.createRow(rowIndex);
            for (int colIndex = 0; colIndex <= page.getMaxCol(); colIndex++)
            {
                Cell cell = page.getCell(new Coordinate(rowIndex, colIndex));
                if (cell != null)
                {
                    cell.formatXssfCell(xssfRow.createCell(colIndex), cellStyleFactory);
                }
            }
        }

        if (page.getMaxCol() < 1)
        {
            return;
        }

        if (page.isAutoSize())
        {
            for (int i = 0; i <= page.getMaxCol(); i++)
            {
                sheet.autoSizeColumn(i);
            }
        }
    }
}
