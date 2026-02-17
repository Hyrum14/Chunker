package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class FormulaOldCell extends OldCell<String>
{
    public FormulaOldCell(String contents)
    {
        super(validateFormula(contents));
    }

    public static String validateFormula(String formula)
    {
        if (formula == null || !formula.startsWith("="))
        {
            throw new IllegalArgumentException("Formula must start with an equal sign.");
        }

        if (!hasBalancedParentheses(formula))
        {
            throw new IllegalArgumentException("Formula has unbalanced parentheses.");
        }

        return formula;
    }

    public static boolean hasBalancedParentheses(String formula)
    {
        int balance = 0;
        for (char c : formula.toCharArray())
        {
            if (c == '(')
            {
                balance++;
            } else if (c == ')')
            {
                balance--;
                if (balance < 0)
                {
                    return false;
                }
            }
        }
        return balance == 0;
    }

    @Override
    public String toString()
    {
        return contents;
    }

    @Override
    public void formatXssfCell(XSSFCell xssfCell)
    {
        // Set the cell value as the formula or use the ErrorCell in case of a formula error
        try
        {
            xssfCell.setCellFormula(contents);
        } catch (Exception e)
        {
            new ErrorOldCell().formatXssfCell(xssfCell);
        }
    }

    @Override
    public CellVariant getCellVariant()
    {
        return CellVariant.FORMULA;
    }


    @Override
    public CellStyle createStyle(XSSFCell xssfCell)
    {
        // Create a cell style and set the date format
        return xssfCell.getSheet().getWorkbook().createCellStyle();
    }
}

