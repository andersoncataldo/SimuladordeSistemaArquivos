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

    /**
     * Lê todas as linhas do arquivo de log para recuperação do sistema.
     * @return Uma lista de strings contendo cada linha do log.
     */
    public java.util.List<String> readLogs() {
        java.util.List<String> logs = new java.util.ArrayList<>();
        java.io.File file = new java.io.File(LOG_FILE);
        if (!file.exists()) return logs;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o journal para recuperação: " + e.getMessage());
        }
        return logs;
    }
}
