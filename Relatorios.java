// Relatorios.java
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.awt.Dimension;

public class Relatorios {
    private List<Estudante> estudantes;
    private List<Professor> professores;
    private Map<Estudante, List<Curso>> matriculas;
    private Map<Professor, List<Curso>> associacoes;

    public Relatorios(
            List<Estudante> estudantes,
            List<Professor> professores,
            List<Curso> cursos,
            Map<Estudante, List<Curso>> matriculas,
            Map<Professor, List<Curso>> associacoes
    ) {
        this.estudantes = estudantes;
        this.professores = professores;
        this.matriculas = matriculas;
        this.associacoes = associacoes;
    }

    public void gerarRelatorioEstudantes() {
        StringBuilder relatorio = new StringBuilder("Relatório de Estudantes:\n");
        for (Estudante estudante : estudantes) {
            relatorio.append("Nome: ").append(estudante.getNome())
                    .append(", Matrícula: ").append(estudante.getMatricula())
                    .append("\nCursos:\n");

            List<Curso> cursosEstudante = matriculas.get(estudante);
            if (cursosEstudante != null) {
                for (Curso curso : cursosEstudante) {
                    relatorio.append("  - ").append(curso.getNomeCurso())
                            .append(" (").append(curso.getCargaHoraria()).append(" horas)\n");
                }
            } else {
                relatorio.append("  Nenhum curso matriculado.\n");
            }
        }

        exibirRelatorio(relatorio.toString());
    }

    public void gerarRelatorioProfessores() {
        StringBuilder relatorio = new StringBuilder("Relatório de Professores:\n");
        for (Professor professor : professores) {
            relatorio.append("Nome: ").append(professor.getNome())
                    .append(", Especialidade: ").append(professor.getEspecialidade())
                    .append("\nCursos:\n");

            List<Curso> cursosProfessor = associacoes.get(professor);
            if (cursosProfessor != null) {
                for (Curso curso : cursosProfessor) {
                    relatorio.append("  - ").append(curso.getNomeCurso())
                            .append(" (").append(curso.getCargaHoraria()).append(" horas)\n");
                }
            } else {
                relatorio.append("  Nenhum curso associado.\n");
            }
        }

        exibirRelatorio(relatorio.toString());
    }

    private void exibirRelatorio(String relatorio) {
        JTextArea textArea = new JTextArea(relatorio);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scrollPane, "Relatório", JOptionPane.INFORMATION_MESSAGE);
    }
}
