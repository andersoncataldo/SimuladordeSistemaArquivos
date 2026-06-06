package service;

import model.Directory;
import model.File;
import java.util.List;

/**
 * Lógica central do simulador de sistema de arquivos.
 */
public class FileSystemSimulator {
    private Directory root;
    private Directory currentDirectory;
    private Journal journal;

    public FileSystemSimulator() {
        this.root = new Directory("root", null);
        this.currentDirectory = root;
        this.journal = new Journal();
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public String getCurrentPath() {
        StringBuilder path = new StringBuilder();
        Directory temp = currentDirectory;
        while (temp != null) {
            path.insert(0, temp.getName() + "/");
            temp = temp.getParent();
        }
        return path.toString();
    }

    public void mkdir(String name) {
        mkdir(name, true);
    }

    private void mkdir(String name, boolean shouldLog) {
        if (currentDirectory.findSubDirectory(name) != null) {
            if (shouldLog) System.out.println("Erro: Diretório já existe.");
            return;
        }
        if (shouldLog) journal.log("MKDIR: " + name + " em " + getCurrentPath());
        Directory newDir = new Directory(name, currentDirectory);
        currentDirectory.addSubDirectory(newDir);
        if (shouldLog) System.out.println("Diretório '" + name + "' criado com sucesso.");
    }

    public void touch(String name) {
        touch(name, true);
    }

    private void touch(String name, boolean shouldLog) {
        if (currentDirectory.findFile(name) != null) {
            if (shouldLog) System.out.println("Erro: Arquivo já existe.");
            return;
        }
        if (shouldLog) journal.log("TOUCH: " + name + " em " + getCurrentPath());
        File newFile = new File(name, currentDirectory);
        currentDirectory.addFile(newFile);
        if (shouldLog) System.out.println("Arquivo '" + name + "' criado com sucesso.");
    }

    public void ls() {
        List<Directory> dirs = currentDirectory.getSubDirectories();
        List<File> files = currentDirectory.getFiles();

        for (Directory dir : dirs) {
            System.out.println("[DIR]  " + dir.getName());
        }
        for (File file : files) {
            System.out.println("[FILE] " + file.getName());
        }
    }

    public void cd(String name) {
        if (name.equals("..")) {
            if (currentDirectory.getParent() != null) {
                currentDirectory = currentDirectory.getParent();
            }
            return;
        }

        Directory nextDir = currentDirectory.findSubDirectory(name);
        if (nextDir != null) {
            currentDirectory = nextDir;
        } else {
            System.out.println("Erro: Diretório não encontrado.");
        }
    }

    public void rm(String name) {
        rm(name, true);
    }

    private void rm(String name, boolean shouldLog) {
        File file = currentDirectory.findFile(name);
        if (file != null) {
            if (shouldLog) journal.log("RM FILE: " + name + " em " + getCurrentPath());
            currentDirectory.removeFile(file);
            if (shouldLog) System.out.println("Arquivo '" + name + "' removido.");
            return;
        }

        Directory dir = currentDirectory.findSubDirectory(name);
        if (dir != null) {
            if (shouldLog) journal.log("RM DIR: " + name + " em " + getCurrentPath());
            currentDirectory.removeSubDirectory(dir);
            if (shouldLog) System.out.println("Diretório '" + name + "' removido.");
            return;
        }

        if (shouldLog) System.out.println("Erro: Item não encontrado.");
    }

    public void rename(String oldName, String newName) {
        rename(oldName, newName, true);
    }

    private void rename(String oldName, String newName, boolean shouldLog) {
        File file = currentDirectory.findFile(oldName);
        if (file != null) {
            if (shouldLog) journal.log("RENAME FILE: " + oldName + " para " + newName + " em " + getCurrentPath());
            file.setName(newName);
            if (shouldLog) System.out.println("Arquivo renomeado para '" + newName + "'.");
            return;
        }

        Directory dir = currentDirectory.findSubDirectory(oldName);
        if (dir != null) {
            if (shouldLog) journal.log("RENAME DIR: " + oldName + " para " + newName + " em " + getCurrentPath());
            dir.setName(newName);
            if (shouldLog) System.out.println("Diretório renomeado para '" + newName + "'.");
            return;
        }

        if (shouldLog) System.out.println("Erro: Item não encontrado.");
    }

    public void cp(String sourceName, String destName) {
        cp(sourceName, destName, true);
    }

    private void cp(String sourceName, String destName, boolean shouldLog) {
        File sourceFile = currentDirectory.findFile(sourceName);
        if (sourceFile == null) {
            if (shouldLog) System.out.println("Erro: Arquivo de origem não encontrado.");
            return;
        }

        if (currentDirectory.findFile(destName) != null) {
            if (shouldLog) System.out.println("Erro: Arquivo de destino já existe.");
            return;
        }

        if (shouldLog) journal.log("CP: " + sourceName + " para " + destName + " em " + getCurrentPath());
        File newFile = new File(destName, currentDirectory);
        currentDirectory.addFile(newFile);
        if (shouldLog) System.out.println("Arquivo '" + sourceName + "' copiado para '" + destName + "'.");
    }

    /**
     * Navega para um caminho absoluto começando de root.
     */
    private void navigateToPath(String path) {
        currentDirectory = root;
        if (path.equals("root/")) return;
        
        String[] parts = path.split("/");
        for (String part : parts) {
            if (part.isEmpty() || part.equals("root")) continue;
            Directory next = currentDirectory.findSubDirectory(part);
            if (next != null) {
                currentDirectory = next;
            }
        }
    }

    /**
     * Reconstrói o estado do sistema a partir do Journal.
     */
    public void recover() {
        List<String> logs = journal.readLogs();
        if (logs.isEmpty()) return;

        System.out.println("Recuperando estado do sistema de arquivos...");
        Directory originalDir = currentDirectory;

        for (String logLine : logs) {
            try {
                // Formato esperado: [data] COMANDO: args em caminho/
                int closingBracket = logLine.indexOf("]");
                if (closingBracket == -1) continue;
                
                String content = logLine.substring(closingBracket + 1).trim();
                String[] emParts = content.split(" em ");
                if (emParts.length < 2) continue;

                String commandFull = emParts[0];
                String path = emParts[1];

                navigateToPath(path);

                if (commandFull.startsWith("MKDIR: ")) {
                    mkdir(commandFull.replace("MKDIR: ", ""), false);
                } else if (commandFull.startsWith("TOUCH: ")) {
                    touch(commandFull.replace("TOUCH: ", ""), false);
                } else if (commandFull.startsWith("RM FILE: ")) {
                    rm(commandFull.replace("RM FILE: ", ""), false);
                } else if (commandFull.startsWith("RM DIR: ")) {
                    rm(commandFull.replace("RM DIR: ", ""), false);
                } else if (commandFull.startsWith("RENAME FILE: ")) {
                    String[] renameParts = commandFull.replace("RENAME FILE: ", "").split(" para ");
                    rename(renameParts[0], renameParts[1], false);
                } else if (commandFull.startsWith("RENAME DIR: ")) {
                    String[] renameParts = commandFull.replace("RENAME DIR: ", "").split(" para ");
                    rename(renameParts[0], renameParts[1], false);
                } else if (commandFull.startsWith("CP: ")) {
                    String[] cpParts = commandFull.replace("CP: ", "").split(" para ");
                    cp(cpParts[0], cpParts[1], false);
                }
            } catch (Exception e) {
                // Silenciosamente ignora linhas malformadas
            }
        }
        currentDirectory = root; // Volta para a raiz após recuperar
        System.out.println("Recuperação concluída.");
    }
}
