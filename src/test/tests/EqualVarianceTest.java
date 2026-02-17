package test.tests;

import app.model.chunking.EqualVariance;
import org.junit.jupiter.api.Test;
import utils.logging.PrintStreamLogger;

import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EqualVarianceTest
{


    private int calcExpected(int docId)
    {
        return switch (docId)
                {
                    case 1 -> 7;
                    case 2 -> 16;
                    case 3 -> 1;
                    case 4 -> 7;
                    case 5 -> 1;
                    case 6 -> 7;
                    case 7 -> 7;
                    default -> throw new IllegalStateException("Unexpected value: " + docId);
                };
    }

    @Test
    void calculateVariance()
    {
        EqualVariance equalVariance = new EqualVariance(5, 1, "test_doc", new PrintStreamLogger()
        {
            @Override
            public PrintStream getStream()
            {
                return System.out;
            }
        });
        double variance = equalVariance.calculateVariance(10, 100);
        assertEquals((double) 9 / (double) 99, variance);
    }


    @Test
    void calculateNumChunks()
    {
        EqualVariance equalVariance = new EqualVariance(5, 1, "test_doc", new PrintStreamLogger()
        {
            @Override
            public PrintStream getStream()
            {
                return System.out;
            }
        });
        long numChunks = equalVariance.calculateNumChunks(10, 100, 200, "test_doc");
        assertEquals(19, numChunks);

        numChunks = equalVariance.calculateNumChunks(2, 1065, 17125, "test_doc");
        assertEquals(17, numChunks);
    }

    @Test
    void getChunkSizes()
    {
        EqualVariance equalVariance = new EqualVariance(5, 1, "test_doc", new PrintStreamLogger()
        {
            @Override
            public PrintStream getStream()
            {
                return System.out;
            }
        });
        List<Integer> chunkSizes = equalVariance.getChunkSizes(1, 100, 10);

        assertEquals(10, chunkSizes.size());
        for (Integer size : chunkSizes)
        {
            assertTrue(size >= 10);
        }
    }
}
