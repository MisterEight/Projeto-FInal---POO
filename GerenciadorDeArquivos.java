// GerenciadorDeArquivos.java
import java.io.*;
import java.util.*;

public class GerenciadorDeArquivos {
    public static void escreverDados(String caminho, List<String> dados) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            for (String linha : dados) {
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    public static List<String> lerDados(String caminho) throws IOException {
        List<String> dados = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                dados.add(linha);
            }
        }
        return dados;
    }
}
