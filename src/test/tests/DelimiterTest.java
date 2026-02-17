package test.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.logging.Logger;
import utils.logging.PrintStreamLogger;
import utils.objects.Delimiter;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DelimiterTest
{

    Logger logger = new PrintStreamLogger()
    {
        @Override
        public PrintStream getStream()
        {
            return System.out;
        }
    };

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void invalidatesPhrase()
    {
        Delimiter doesNotInvalidatePhrase = new Delimiter(0, 0, " ");
        assertFalse(doesNotInvalidatePhrase.invalidatesPhrase(logger));

        Delimiter doesInvalidatePhrase = new Delimiter(0, 0, ", ");
        assertTrue(doesInvalidatePhrase.invalidatesPhrase(logger));

        doesInvalidatePhrase = new Delimiter(0, 0, ". ");
        assertTrue(doesInvalidatePhrase.invalidatesPhrase(logger));
    }
}