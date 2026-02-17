package test.tests;

import app.model.parsing.CharacterCategorizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Execution(ExecutionMode.CONCURRENT)
public class CharacterCategorizerComprehensiveTest
{
    private static final int RANGE_SIZE = 256;
    private static final String ERROR_FILE = "errors.txt";


    @TestFactory
    public Stream<DynamicTest> testAllUnicodeCharacters()
    {
        List<DynamicTest> tests = new ArrayList<>();
        for (int start = 0; start <= 0x10FFFF; start += RANGE_SIZE)
        {
            final int rangeStart = start;
            tests.add(DynamicTest.dynamicTest("Testing range " + rangeStart + "-" + (rangeStart + RANGE_SIZE - 1),
                    () -> testUnicodeRange(rangeStart, Math.min(rangeStart + RANGE_SIZE, 0x10FFFF))));
        }

        return tests.stream();
    }

    private void testUnicodeRange(int rangeStart, int rangeEnd)
    {
        List<String> errors = new ArrayList<>();
        for (int codePoint = rangeStart; codePoint <= rangeEnd; codePoint++)
        {
            boolean isInvalid = CharacterCategorizer.isInvalid(codePoint);
            boolean isSplit = CharacterCategorizer.isSplit(codePoint);
            boolean isLetter = CharacterCategorizer.isLetter(codePoint);
            boolean isContextLetter = CharacterCategorizer.isContextLetter(codePoint);

            int trueCount = 0;
            if (isInvalid) trueCount++;
            if (isSplit) trueCount++;
            if (isLetter) trueCount++;
            if (isContextLetter) trueCount++;

            // Collect errors instead of failing immediately
            if (trueCount != 1)
            {
                String error = "Code point: " + codePoint + " (" + new String(Character.toChars(codePoint)) + "), " +
                        "isInvalid: " + isInvalid + ", isSplit: " + isSplit + ", isLetter: " + isLetter +
                        ", isContextLetter: " + isContextLetter;
                errors.add(error);
            }
        }

        // Write errors to file
        writeErrorsToFile(errors);

        // Assert all collected errors
        Assertions.assertAll(errors.stream().map(Assertions::fail));
    }

    private void writeErrorsToFile(List<String> errors)
    {
        try
        {
            Files.deleteIfExists(Paths.get(ERROR_FILE));
            Files.write(Paths.get(ERROR_FILE), errors, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}