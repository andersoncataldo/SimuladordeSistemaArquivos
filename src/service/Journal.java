package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Responsável pelo registro de operações em um log físico (Journaling).
 */
public class Journal {
    private static final String LOG_FILE = "journal.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra uma operação no arquivo de log.
     * @param operation Descrição da operação a ser registrada.
     */
    public void log(String operation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.write(String.format("[%s] %s%n", timestamp, operation));
            writer.flush();
        } catch (IOException e) {
            System.err.println("Erro ao gravar no journal: " + e.getMessage());
        }
    }
}
