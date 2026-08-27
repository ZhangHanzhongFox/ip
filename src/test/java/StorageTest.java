import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Dependency-free tests for Level-7 file persistence. Run with assertions enabled. */
public class StorageTest {
    /** Runs all storage tests. */
    public static void main(String[] args) throws Exception {
        testRoundTrip();
        testEmptyAndMissingFile();
        testCorruptedFile();
        testReadAndWriteFailures();
    }

    private static void testRoundTrip() throws Exception {
        Path directory = Files.createTempDirectory("fox-storage-round-trip");
        Path file = directory.resolve("nested").resolve("fox.txt");
        Storage storage = new Storage(file);
        Task[] tasks = {new Todo("buy | milk"), new Deadline("submit report", "Friday"),
                new Event("team meeting", "10am", "11am")};
        tasks[0].markAsDone();
        storage.save(tasks, tasks.length);
        Task[] loaded = storage.load();
        assert loaded.length == 3;
        assert loaded[0].toString().equals("[T][X] buy | milk");
        assert loaded[1].toString().equals("[D][ ] submit report (by: Friday)");
        assert loaded[2].toString().equals("[E][ ] team meeting (from: 10am to: 11am)")
                : loaded[2].toString();
    }

    private static void testEmptyAndMissingFile() throws Exception {
        Path directory = Files.createTempDirectory("fox-storage-empty");
        Path file = directory.resolve("new").resolve("fox.txt");
        Storage storage = new Storage(file);
        assert storage.load().length == 0;
        assert Files.isDirectory(file.getParent());
        Files.createFile(file);
        assert storage.load().length == 0;
    }

    private static void testCorruptedFile() throws Exception {
        Path file = Files.createTempFile("fox-storage-corrupt", ".txt");
        String original = "not a Fox task record\n";
        Files.writeString(file, original, StandardCharsets.UTF_8);
        try {
            new Storage(file).load();
            throw new AssertionError("Corrupted data should fail to load");
        } catch (Storage.StorageException exception) {
            assert exception.getMessage().contains("Invalid data");
        }
        assert Files.readString(file).equals(original);
    }

    private static void testReadAndWriteFailures() throws Exception {
        Path directory = Files.createTempDirectory("fox-storage-failure");
        Path parentFile = directory.resolve("parent-file");
        Files.createFile(parentFile);
        try {
            new Storage(parentFile.resolve("fox.txt")).save(new Task[0], 0);
            throw new AssertionError("Write failure should be reported");
        } catch (Storage.StorageException expected) {
            assert expected.getMessage().contains("Could not write");
        }
        try {
            new Storage(directory).load();
            throw new AssertionError("Read failure should be reported");
        } catch (Storage.StorageException expected) {
            assert expected.getMessage().contains("Could not read");
        }
    }
}
