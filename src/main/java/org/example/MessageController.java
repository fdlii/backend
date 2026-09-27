package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    private static final Path DATA_FILE = Paths.get("data.txt");

    @PostMapping("/messages")
    public Map<String, String> receiveMessage(@RequestBody Map<String, String> body) {
        String text = body.get("text");

        if (text == null || text.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле 'text' не должно быть пустым");
        }

        String line = text.trim() + System.lineSeparator();

        try {
            Files.write(
                DATA_FILE,
                line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось сохранить данные", e);
        }

        return Map.of("message", "Данные успешно сохранены");
    }
}
