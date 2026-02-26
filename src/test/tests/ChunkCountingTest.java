package test.tests;

import app.model.chunking.ChunkingStrategy;
import app.model.chunking.EqualChunkSizes;
import app.model.parsing.DocumentParser;
import app.model.parsing.DocumentParsingException;
import app.model.parsing.RootConverter;
import app.model.parsing.StringDocumentProvider;
import app.model.dao.map.OccurrenceMapDao;
import utils.objects.Document;
import utils.objects.Token;
import utils.objects.Delimiter;
import utils.logging.DummyLogger;
import utils.objects.chunk.Chunk;
import utils.objects.CountResult;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChunkCountingTest {
    /**
     * Helper storage implementation that fills an OccurrenceMapDao and
     * updates document word count exactly as the real parser would.
     */
    private static class TestStorage implements DocumentParser.DocumentStorage {
        private final OccurrenceMapDao occDao;
        private final Document doc;
        private int index = 0;

        TestStorage(OccurrenceMapDao occDao, Document doc) {
            this.occDao = occDao;
            this.doc = doc;
        }

        @Override
        public void storeDelimiter(String contents) {
            // not needed for these tests
        }

        @Override
        public void storeOccurrence(String contents) {
            String root = RootConverter.convert(contents);
            Token tok = new Token(contents, root, doc.getId(), index);
            occDao.add(tok);
            doc.addWord();
            index++;
        }
    }

    @Test
    public void iShouldNotAppearInChunksWithoutIt() throws DocumentParsingException {
        // create very simple text with no "i" word
        String text = "hello world this test";
        Document doc = new Document("temp.txt", "", "", 0, Instant.now());
        doc.setId(1);

        OccurrenceMapDao occDao = new OccurrenceMapDao();
        DocumentParser parser = new DocumentParser();
        parser.parse(new StringDocumentProvider(text), new TestStorage(occDao, doc));

        // sanity check: word count matches expectation
        assertEquals(4, doc.getNumWords());

        // chunk the document into 2 chunks
        ChunkingStrategy.WarningHandler noWarn = msg -> {
        };
        List<Chunk> chunks = new EqualChunkSizes(2, new DummyLogger()).generate(List.of(doc), noWarn);
        assertEquals(2, chunks.size());

        for (Chunk chunk : chunks) {
            CountResult<String> counts = occDao.countWords(chunk);
            assertFalse(counts.getKeys().contains("i"), "found unexpected 'i' in chunk " + chunk.getChunkName());
        }
    }
}
