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
        if (currentDirectory.findSubDirectory(name) != null) {
            System.out.println("Erro: Diretório já existe.");
            return;
        }
        journal.log("MKDIR: " + name + " em " + getCurrentPath());
        Directory newDir = new Directory(name, currentDirectory);
        currentDirectory.addSubDirectory(newDir);
        System.out.println("Diretório '" + name + "' criado com sucesso.");
    }

    public void touch(String name) {
        if (currentDirectory.findFile(name) != null) {
            System.out.println("Erro: Arquivo já existe.");
            return;
        }
        journal.log("TOUCH: " + name + " em " + getCurrentPath());
        File newFile = new File(name, currentDirectory);
        currentDirectory.addFile(newFile);
        System.out.println("Arquivo '" + name + "' criado com sucesso.");
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
        File file = currentDirectory.findFile(name);
        if (file != null) {
            journal.log("RM FILE: " + name + " em " + getCurrentPath());
            currentDirectory.removeFile(file);
            System.out.println("Arquivo '" + name + "' removido.");
            return;
        }

        Directory dir = currentDirectory.findSubDirectory(name);
        if (dir != null) {
            journal.log("RM DIR: " + name + " em " + getCurrentPath());
            currentDirectory.removeSubDirectory(dir);
            System.out.println("Diretório '" + name + "' removido.");
            return;
        }

        System.out.println("Erro: Item não encontrado.");
    }

    public void rename(String oldName, String newName) {
        File file = currentDirectory.findFile(oldName);
        if (file != null) {
            journal.log("RENAME FILE: " + oldName + " para " + newName + " em " + getCurrentPath());
            file.setName(newName);
            System.out.println("Arquivo renomeado para '" + newName + "'.");
            return;
        }

        Directory dir = currentDirectory.findSubDirectory(oldName);
        if (dir != null) {
            journal.log("RENAME DIR: " + oldName + " para " + newName + " em " + getCurrentPath());
            dir.setName(newName);
            System.out.println("Diretório renomeado para '" + newName + "'.");
            return;
        }

        System.out.println("Erro: Item não encontrado.");
    }

    public void cp(String sourceName, String destName) {
        File sourceFile = currentDirectory.findFile(sourceName);
        if (sourceFile == null) {
            System.out.println("Erro: Arquivo de origem não encontrado.");
            return;
        }

        if (currentDirectory.findFile(destName) != null) {
            System.out.println("Erro: Arquivo de destino já existe.");
            return;
        }

        journal.log("CP: " + sourceName + " para " + destName + " em " + getCurrentPath());
        File newFile = new File(destName, currentDirectory);
        currentDirectory.addFile(newFile);
        System.out.println("Arquivo '" + sourceName + "' copiado para '" + destName + "'.");
    }
}
