package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um diretório no simulador de sistema de arquivos.
 */
public class Directory {
    private String name;
    private Directory parent;
    private List<Directory> subDirectories;
    private List<File> files;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.subDirectories = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory getParent() {
        return parent;
    }

    public List<Directory> getSubDirectories() {
        return subDirectories;
    }

    public List<File> getFiles() {
        return files;
    }

    public void addSubDirectory(Directory dir) {
        subDirectories.add(dir);
    }

    public void removeSubDirectory(Directory dir) {
        subDirectories.remove(dir);
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }

    public Directory findSubDirectory(String name) {
        for (Directory dir : subDirectories) {
            if (dir.getName().equals(name)) {
                return dir;
            }
        }
        return null;
    }

    public File findFile(String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + "/";
    }
}
