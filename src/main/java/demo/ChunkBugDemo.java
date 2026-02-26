package demo;

import app.model.chunking.ChunkingStrategy;
import app.model.chunking.EqualChunkSizes;
import app.model.parsing.DocumentParser;
import app.model.parsing.RootConverter;
import app.model.parsing.StringDocumentProvider;
import app.model.dao.map.OccurrenceMapDao;
import utils.objects.Document;
import utils.objects.Token;
import utils.logging.DummyLogger;
import utils.objects.chunk.Chunk;
import utils.objects.CountResult;

import java.time.Instant;
import java.util.List;

public class ChunkBugDemo {
    public static void main(String[] args) throws Exception {
        // allow reading from a file if provided
        String text;
        if (args.length > 0) {
            java.nio.file.Path path = java.nio.file.Paths.get(args[0]);
            text = java.nio.file.Files.readString(path);
            System.out.println("Loaded text from " + path);
        } else {
            text = "hello world this test"; // no 'i'
        }

        Document doc = new Document("temp", "", "", 0, Instant.now());
        doc.setId(1);
        OccurrenceMapDao occDao = new OccurrenceMapDao();
        DocumentParser parser = new DocumentParser();

        parser.parse(new StringDocumentProvider(text), new DocumentParser.DocumentStorage() {
            int index = 0;

            @Override
            public void storeDelimiter(String contents) {
            }

            @Override
            public void storeOccurrence(String contents) {
                String root = RootConverter.convert(contents);
                Token tok = new Token(contents, root, doc.getId(), index);
                occDao.add(tok);
                doc.addWord();
                index++;
            }
        });

        System.out.println("Parsed words: " + doc.getNumWords());
        ChunkingStrategy.WarningHandler noWarn = msg -> {
        };

        // try equal chunk sizes and equal number of chunks if two args provided
        if (args.length > 1 && args[1].equals("equalnum")) {
            int numChunks = 3;
            if (args.length > 2) {
                try {
                    numChunks = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignore) {
                }
            }
            System.out.println("Generating " + numChunks + " equal-number chunks");
            List<Chunk> chunks = new app.model.chunking.EqualNumberOfChunks(numChunks, new DummyLogger())
                    .generate(List.of(doc), noWarn);
            for (Chunk chunk : chunks) {
                CountResult<String> counts = occDao.countWords(chunk);
                System.out.printf("Chunk %s size=%d counts=%s\n", chunk.getChunkName(), chunk.getSize(), counts);
            }
        } else {
            List<Chunk> chunks = new EqualChunkSizes(2, new DummyLogger()).generate(List.of(doc), noWarn);
            for (Chunk chunk : chunks) {
                CountResult<String> counts = occDao.countWords(chunk);
                System.out.printf("Chunk %s size=%d counts=%s\n", chunk.getChunkName(), chunk.getSize(), counts);
            }
        }
    }
}
