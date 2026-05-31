package model;

/**
 * Representa um arquivo no simulador de sistema de arquivos.
 */
public class File {
    private String name;
    private Directory parent;

    public File(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
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

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return name;
    }
}
