package fox;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fox.storage.Storage;

/** JUnit integration tests for Fox command handling and persistence wiring. */
class FoxJUnitTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void handlesCommandsAndReportsMalformedInput() {
        Fox fox = new Fox(new Storage(temporaryDirectory.resolve("fox.txt")));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        try {
            System.setOut(new PrintStream(output));
            fox.run(new Scanner("todo remember this\nmark 1\nlist\nmark nope\nbye\n"));
        } finally {
            System.setOut(originalOutput);
        }

        String text = output.toString();
        assertTrue(text.contains("remember this"));
        assertTrue(text.contains("[X] remember this"));
        assertTrue(text.contains("whole number"));
        assertTrue(text.contains("Bye for now!"));
    }

    @Test
    void guiResponseUsesSameCommandsAndPersistsTasks() {
        Path dataFile = temporaryDirectory.resolve("gui-fox.txt");
        Fox fox = new Fox(new Storage(dataFile));

        assertTrue(fox.getResponse("todo remember this").contains("remember this"));
        assertTrue(fox.getResponse("list").contains("remember this"));
        fox.getResponse("bye");

        Fox reloadedFox = new Fox(new Storage(dataFile));
        assertTrue(reloadedFox.getResponse("list").contains("remember this"));
    }
}
