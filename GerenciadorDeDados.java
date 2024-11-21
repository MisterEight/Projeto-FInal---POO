// GerenciadorDeDados.java
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorDeDados {
    public List<Estudante> carregarEstudantes() {
        List<Estudante> estudantes = new ArrayList<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("alunos.txt");
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Estudante estudante = new Estudante(partes[0], Integer.parseInt(partes[1]), partes[2]);
                estudantes.add(estudante);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao carregar estudantes: " + ex.getMessage());
        }
        return estudantes;
    }

    public List<Professor> carregarProfessores() {
        List<Professor> professores = new ArrayList<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("professores.txt");
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Professor professor = new Professor(partes[0], Integer.parseInt(partes[1]), partes[2]);
                professores.add(professor);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao carregar professores: " + ex.getMessage());
        }
        return professores;
    }

    public List<Curso> carregarCursos() {
        List<Curso> cursos = new ArrayList<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("cursos.txt");
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Curso curso = new Curso(partes[0], Integer.parseInt(partes[1]));
                cursos.add(curso);
            }
        } catch (IOException ex) {
            System.err.println("Erro ao carregar cursos: " + ex.getMessage());
        }
        return cursos;
    }

    public Map<Estudante, List<Curso>> carregarMatriculas(List<Estudante> estudantes, List<Curso> cursos) {
        Map<Estudante, List<Curso>> matriculas = new HashMap<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("vinculacoes.txt");
            for (String linha : linhas) {
                if (linha.startsWith("Estudante: ")) {
                    String[] partes = linha.replace("Estudante: ", "").split(" -> ");
                    String nomeEstudante = partes[0];
                    String[] nomesCursos = partes[1].split(", ");
    
                    Estudante estudante = estudantes.stream()
                            .filter(e -> e.getNome().equalsIgnoreCase(nomeEstudante))
                            .findFirst()
                            .orElse(null);
    
                    if (estudante != null) {
                        List<Curso> cursosMatriculados = new ArrayList<>();
                        for (String nomeCurso : nomesCursos) {
                            Curso curso = cursos.stream()
                                    .filter(c -> c.getNomeCurso().equalsIgnoreCase(nomeCurso))
                                    .findFirst()
                                    .orElse(null);
                            if (curso != null) {
                                cursosMatriculados.add(curso);
                            }
                        }
                        matriculas.put(estudante, cursosMatriculados);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro ao carregar matrículas: " + ex.getMessage());
        }
        return matriculas;
    }
    
    
    public Map<Professor, List<Curso>> carregarAssociacoes(List<Professor> professores, List<Curso> cursos) {
        Map<Professor, List<Curso>> associacoes = new HashMap<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("vinculacoes.txt");
            for (String linha : linhas) {
                if (linha.startsWith("Professor: ")) {
                    String[] partes = linha.replace("Professor: ", "").split(" -> ");
                    String nomeProfessor = partes[0];
                    String[] nomesCursos = partes[1].split(", ");
    
                    Professor professor = professores.stream()
                            .filter(p -> p.getNome().equalsIgnoreCase(nomeProfessor))
                            .findFirst()
                            .orElse(null);
    
                    if (professor != null) {
                        List<Curso> cursosAssociados = new ArrayList<>();
                        for (String nomeCurso : nomesCursos) {
                            Curso curso = cursos.stream()
                                    .filter(c -> c.getNomeCurso().equalsIgnoreCase(nomeCurso))
                                    .findFirst()
                                    .orElse(null);
                            if (curso != null) {
                                cursosAssociados.add(curso);
                            }
                        }
                        associacoes.put(professor, cursosAssociados);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Erro ao carregar associações: " + ex.getMessage());
        }
        return associacoes;
    }    

    public Map<Estudante, List<Curso>> carregarMatriculas() {
        List<Estudante> estudantes = carregarEstudantes();
        List<Curso> cursos = carregarCursos();
        return carregarMatriculas(estudantes, cursos);
    }
    
    public Map<Professor, List<Curso>> carregarAssociacoes() {
        List<Professor> professores = carregarProfessores();
        List<Curso> cursos = carregarCursos();
        return carregarAssociacoes(professores, cursos);
    }
}
