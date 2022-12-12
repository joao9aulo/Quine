import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Quine {
    public static void main(String[] args) {
        Class<?> clazz = Quine.class;
        String className = clazz.getSimpleName();

        UUID uuid = UUID.randomUUID();
        String filename = className + "_" + uuid.toString().replace("-", "_") + ".java";

        System.out.println("Class name gerado: "+filename);
        try (Scanner scanner = new Scanner(new File(className + ".java"));
             PrintWriter writer = new PrintWriter(filename)) {

            List<StringBuilder> linhas = new ArrayList<StringBuilder>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String newLine = line.replace(className, filename.replace(".java", ""));
                linhas.add(new StringBuilder(newLine));
            }

            randomizeString(linhas);

            for(StringBuilder linha:linhas) {
                writer.println(linha);
            }
            System.out.println("Compiling the generated class");
            // Compile the generated class
            ProcessBuilder pb = new ProcessBuilder("javac", className + ".java");
            Process p = pb.start();
            p.waitFor();

            System.out.println("Executing the generated class");
            // Execute the generated class
            pb = new ProcessBuilder("java", className);
            p = pb.start();
            p.waitFor();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private static void randomizeString(List<StringBuilder> input) {
        // Cria uma instância do gerador de números aleatórios
        Random random = new Random();

        // Gera um índice aleatório na string
        int linha = random.nextInt(input.size());

        // Gera um índice aleatório na string
        int index = random.nextInt(input.get(linha).length());

        // Gera um caractere aleatório
        char randomChar = (char)(random.nextInt(26) + 'a');

        // Substitui o caractere na posição gerada pelo caractere aleatório
        input.get(linha).setCharAt(index,randomChar);
    }
}
