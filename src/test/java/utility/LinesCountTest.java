package utility;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LinesCountTest {

    int countLines(File file) {
        if (file.isFile()) {
            System.out.println(file);
            try {
                return Files.readAllLines(file.toPath()).size();
            } catch (IOException e) {
                return 0;
            }
        } else {
            int s = 0;
            for (File file1 : file.listFiles()) {
                s += countLines(file1);
            }
            return s;
        }
    }

    @SneakyThrows
    @Test
    void countLines() {
        System.out.println(countLines(new File("src")));
    }
}
