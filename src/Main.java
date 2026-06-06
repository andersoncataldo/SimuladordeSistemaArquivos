import service.FileSystemSimulator;
import java.util.Scanner;

/**
 * Classe principal que executa o Shell interativo do simulador.
 */
public class Main {
    public static void main(String[] args) {
        FileSystemSimulator fs = new FileSystemSimulator();
        fs.recover();
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("==========================================");
        System.out.println("   Simulador de Sistema de Arquivos      ");
        System.out.println("   Modo Shell Interativo Ativado         ");
        System.out.println("==========================================");
        System.out.println("Comandos disponíveis: ls, mkdir, touch, cd, rm, rename, cp, exit, help");

        while (true) {
            System.out.print(fs.getCurrentPath() + "> ");
            command = scanner.nextLine().trim();

            if (command.isEmpty()) continue;

            String[] parts = command.split("\\s+");
            String action = parts[0].toLowerCase();

            try {
                switch (action) {
                    case "exit":
                        System.out.println("Saindo do simulador...");
                        scanner.close();
                        return;

                    case "help":
                        showHelp();
                        break;

                    case "ls":
                        fs.ls();
                        break;

                    case "mkdir":
                        if (parts.length < 2) {
                            System.out.println("Uso: mkdir <nome_diretorio>");
                        } else {
                            fs.mkdir(parts[1]);
                        }
                        break;

                    case "touch":
                        if (parts.length < 2) {
                            System.out.println("Uso: touch <nome_arquivo>");
                        } else {
                            fs.touch(parts[1]);
                        }
                        break;

                    case "cd":
                        if (parts.length < 2) {
                            System.out.println("Uso: cd <nome_diretorio>");
                        } else {
                            fs.cd(parts[1]);
                        }
                        break;

                    case "rm":
                        if (parts.length < 2) {
                            System.out.println("Uso: rm <nome_item>");
                        } else {
                            fs.rm(parts[1]);
                        }
                        break;

                    case "rename":
                        if (parts.length < 3) {
                            System.out.println("Uso: rename <nome_antigo> <nome_novo>");
                        } else {
                            fs.rename(parts[1], parts[2]);
                        }
                        break;

                    case "cp":
                        if (parts.length < 3) {
                            System.out.println("Uso: cp <origem> <destino>");
                        } else {
                            fs.cp(parts[1], parts[2]);
                        }
                        break;

                    default:
                        System.out.println("Comando desconhecido: " + action + ". Digite 'help' para ajuda.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao executar comando: " + e.getMessage());
            }
        }
    }

    private static void showHelp() {
        System.out.println("\nComandos Disponíveis:");
        System.out.println("ls                  - Lista o conteúdo do diretório atual.");
        System.out.println("mkdir <nome>        - Cria um novo diretório.");
        System.out.println("touch <nome>        - Cria um novo arquivo vazio.");
        System.out.println("cd <nome>           - Entra em um diretório (use '..' para voltar).");
        System.out.println("rm <nome>           - Remove um arquivo ou diretório.");
        System.out.println("rename <antigo> <novo> - Renomeia um arquivo ou diretório.");
        System.out.println("cp <origem> <destino>  - Copia um arquivo.");
        System.out.println("exit                - Encerra o simulador.");
        System.out.println("help                - Mostra esta lista de comandos.\n");
    }
}
