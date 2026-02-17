package app.model.export.formating.cell.old.files;

import app.model.export.formating.cell.CellVariant;
import app.model.export.formating.cell.LabelConstant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CellFactory
{
    public static OldCell<?> createCell(CellVariant variant, Object contents, Object... extraArgs)
    {
        switch (variant)
        {
            case LABEL:
                return new LabelOldCell((LabelConstant) contents);
            case INTEGER:
                return new IntegerOldCell((Integer) contents);
            case PADDED_INTEGER:
                return new PaddedIntegerOldCell((Integer) contents, (Integer) extraArgs[0]);
            case DECIMAL:
                return new DecimalOldCell((BigDecimal) contents, (Integer) extraArgs[0]);
            case STRING:
                return new StringOldCell((String) contents);
            case FORMULA:
                return new FormulaOldCell((String) contents);
            case BOOLEAN:
                return new BooleanOldCell((Boolean) contents);
            case ERROR:
                return new ErrorOldCell();
            case DATE:
                return new DateOldCell((LocalDate) contents);
            case TIME:
                return new TimeOldCell((LocalTime) contents);
            case DATE_TIME:
                return new DateTimeOldCell((LocalDateTime) contents);
            case FLOAT:
                return new FloatOldCell((Float) contents);
            case UTF_8:
                throw new IllegalArgumentException("UTF_8 Cell Variant is not yet implemented");
            case PERCENT:
                return new PercentOldCell((Float) contents);
            case DEFAULT:
            case EMPTY:
            default:
                return new EmptyOldCell();
        }
//        throw new IllegalArgumentException("Invalid cell variant");
    }

}
