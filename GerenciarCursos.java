// GerenciarCursos.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class GerenciarCursos extends JFrame {
    private List<Curso> cursos = new ArrayList<>();
    private final String caminhoArquivo = "cursos.txt";

    private Map<Estudante, List<Curso>> matriculas = new HashMap<>();
    private Map<Professor, List<Curso>> associacoes = new HashMap<>();


    public GerenciarCursos() {
        // Configurações básicas da janela
        setTitle("Gerenciamento de Cursos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        carregarVinculacoes();

        // Painel principal
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));

        // Botões de ações
        JButton btnCadastrar = new JButton("Cadastrar Curso");
        JButton btnConsultar = new JButton("Consultar Curso");
        JButton btnVincular = new JButton("Vincular Estudantes/Professores");
        JButton btnVoltar = new JButton("Voltar");

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(1, 4, 10, 10));
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnConsultar);
        painelBotoes.add(btnVincular);
        painelBotoes.add(btnVoltar);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCurso();
            }
        });

        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarCurso();
            }
        });

        btnVincular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vincularEstudantesProfessores();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fecha a janela e volta para o menu principal
            }
        });

        add(painel);
        setVisible(true);
    }

    private void cadastrarCurso() {
        // Janela de cadastro
        JTextField nomeField = new JTextField();
        JTextField cargaHorariaField = new JTextField();

        Object[] message = {
            "Nome do Curso:", nomeField,
            "Carga Horária:", cargaHorariaField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Cadastro de Curso",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText();
                int cargaHoraria = Integer.parseInt(cargaHorariaField.getText());

                Curso curso = new Curso(nome, cargaHoraria);
                cursos.add(curso);

                salvarDados();
                JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarCurso() {
        String busca = JOptionPane.showInputDialog(this, "Digite o nome do curso:");
        if (busca != null && !busca.isEmpty()) {
            carregarDados();

            for (Curso curso : cursos) {
                if (curso.getNomeCurso().equalsIgnoreCase(busca)) {
                    exibirOpcoesCurso(curso);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Curso não encontrado!");
        }
    }

    private void exibirOpcoesCurso(Curso curso) {
        String[] opcoes = {"Editar", "Excluir", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Curso encontrado: " + curso.getNomeCurso(),
                "Opções",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 0 -> editarCurso(curso);
            case 1 -> excluirCurso(curso);
        }
    }

    private void editarCurso(Curso curso) {
        JTextField nomeField = new JTextField(curso.getNomeCurso());
        JTextField cargaHorariaField = new JTextField(String.valueOf(curso.getCargaHoraria()));

        Object[] message = {
            "Nome do Curso:", nomeField,
            "Carga Horária:", cargaHorariaField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Editar Curso",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                curso.setNomeCurso(nomeField.getText());
                curso.setCargaHoraria(Integer.parseInt(cargaHorariaField.getText()));

                salvarDados();
                JOptionPane.showMessageDialog(this, "Curso editado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Carga horária inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirCurso(Curso curso) {
        cursos.remove(curso);
        try {
            salvarDados();
            JOptionPane.showMessageDialog(this, "Curso excluído com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vincularEstudantesProfessores() {
        carregarDados(); // Carregar dados dos cursos
    
        // Escolha do curso
        String[] nomesCursos = cursos.stream().map(Curso::getNomeCurso).toArray(String[]::new);
        String cursoSelecionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione um curso:",
                "Vinculação",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomesCursos,
                nomesCursos.length > 0 ? nomesCursos[0] : null
        );
    
        if (cursoSelecionado == null) return;
    
        Curso curso = cursos.stream().filter(c -> c.getNomeCurso().equals(cursoSelecionado)).findFirst().orElse(null);
        if (curso == null) {
            JOptionPane.showMessageDialog(this, "Curso não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Opções de vinculação
        String[] opcoes = {"Matricular Estudantes", "Associar Professores", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Escolha uma opção de vinculação:",
                "Vinculação",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
    
        switch (escolha) {
            case 0 -> {
                matricularEstudantes(curso);
                try {
                    salvarVinculacoes(); // Salvar após vincular
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar as vinculações!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            case 1 -> {
                associarProfessores(curso);
                try {
                    salvarVinculacoes(); // Salvar após vincular
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar as vinculações!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void salvarDados() throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Curso curso : cursos) {
            linhas.add(curso.getNomeCurso() + "," + curso.getCargaHoraria());
        }
        GerenciadorDeArquivos.escreverDados(caminhoArquivo, linhas);
    }

    private void carregarDados() {
        cursos.clear();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados(caminhoArquivo);
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Curso curso = new Curso(partes[0], Integer.parseInt(partes[1]));
                cursos.add(curso);
            }
        } catch (IOException ex) {
            // Arquivo pode não existir na primeira execução
        }
    }

    private void matricularEstudantes(Curso curso) {
        // Lista de estudantes disponíveis
        List<Estudante> estudantesDisponiveis = carregarEstudantes(); // Método para carregar estudantes
        String[] nomesEstudantes = estudantesDisponiveis.stream().map(Estudante::getNome).toArray(String[]::new);
    
        String estudanteSelecionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione um estudante para matricular em: " + curso.getNomeCurso(),
                "Matricular Estudante",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomesEstudantes,
                nomesEstudantes.length > 0 ? nomesEstudantes[0] : null
        );
    
        if (estudanteSelecionado == null) return;
    
        Estudante estudante = estudantesDisponiveis.stream().filter(e -> e.getNome().equals(estudanteSelecionado)).findFirst().orElse(null);
        if (estudante != null) {
            if (!matriculas.containsKey(estudante)) {
                matriculas.put(estudante, new ArrayList<>());
            }
            matriculas.get(estudante).add(curso);
            JOptionPane.showMessageDialog(this, "Estudante matriculado com sucesso!");
        }
    }

    private void associarProfessores(Curso curso) {
        // Lista de professores disponíveis
        List<Professor> professoresDisponiveis = carregarProfessores(); // Método para carregar professores
        String[] nomesProfessores = professoresDisponiveis.stream().map(Professor::getNome).toArray(String[]::new);
    
        String professorSelecionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecione um professor para associar ao curso: " + curso.getNomeCurso(),
                "Associar Professor",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nomesProfessores,
                nomesProfessores.length > 0 ? nomesProfessores[0] : null
        );
    
        if (professorSelecionado == null) return;
    
        Professor professor = professoresDisponiveis.stream().filter(p -> p.getNome().equals(professorSelecionado)).findFirst().orElse(null);
        if (professor != null) {
            if (!associacoes.containsKey(professor)) {
                associacoes.put(professor, new ArrayList<>());
            }
            associacoes.get(professor).add(curso);
            JOptionPane.showMessageDialog(this, "Professor associado com sucesso!");
        }
    }

    private List<Estudante> carregarEstudantes() {
        // Implementação para carregar estudantes do arquivo
        List<Estudante> estudantes = new ArrayList<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("alunos.txt");
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Estudante estudante = new Estudante(partes[0], Integer.parseInt(partes[1]), partes[2]);
                estudantes.add(estudante);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar estudantes!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return estudantes;
    }

    private List<Professor> carregarProfessores() {
        // Implementação para carregar professores do arquivo
        List<Professor> professores = new ArrayList<>();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("professores.txt");
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Professor professor = new Professor(partes[0], Integer.parseInt(partes[1]), partes[2]);
                professores.add(professor);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar professores!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return professores;
    }

    private void salvarVinculacoes() throws IOException {
        List<String> linhas = new ArrayList<>();
    
        // Salvar estudantes e cursos
        for (Map.Entry<Estudante, List<Curso>> entry : matriculas.entrySet()) {
            String estudante = "Estudante: " + entry.getKey().getNome() + " -> ";
            String cursos = String.join(", ", entry.getValue().stream()
                .map(Curso::getNomeCurso).toArray(String[]::new));
            linhas.add(estudante + cursos);
        }
    
        // Salvar professores e cursos
        for (Map.Entry<Professor, List<Curso>> entry : associacoes.entrySet()) {
            String professor = "Professor: " + entry.getKey().getNome() + " -> ";
            String cursos = String.join(", ", entry.getValue().stream()
                .map(Curso::getNomeCurso).toArray(String[]::new));
            linhas.add(professor + cursos);
        }
    
        GerenciadorDeArquivos.escreverDados("vinculacoes.txt", linhas);
    }
    
    private void carregarVinculacoes() {
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados("vinculacoes.txt");
    
            // Processar as linhas para estudantes e professores
            for (String linha : linhas) {
                if (linha.startsWith("Estudante: ")) {
                    String[] partes = linha.replace("Estudante: ", "").split(" -> ");
                    String nomeEstudante = partes[0];
                    List<Curso> cursos = localizarCursos(partes[1].split(", "));
                    Estudante estudante = localizarEstudante(nomeEstudante);
                    if (estudante != null) {
                        matriculas.put(estudante, cursos);
                    }
                } else if (linha.startsWith("Professor: ")) {
                    String[] partes = linha.replace("Professor: ", "").split(" -> ");
                    String nomeProfessor = partes[0];
                    List<Curso> cursos = localizarCursos(partes[1].split(", "));
                    Professor professor = localizarProfessor(nomeProfessor);
                    if (professor != null) {
                        associacoes.put(professor, cursos);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar as vinculações!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Métodos auxiliares para localizar estudantes, professores e cursos
    private Estudante localizarEstudante(String nome) {
        for (Estudante estudante : carregarEstudantes()) {
            if (estudante.getNome().equalsIgnoreCase(nome)) {
                return estudante;
            }
        }
        return null;
    }
    
    private Professor localizarProfessor(String nome) {
        for (Professor professor : carregarProfessores()) {
            if (professor.getNome().equalsIgnoreCase(nome)) {
                return professor;
            }
        }
        return null;
    }
    
    private List<Curso> localizarCursos(String[] nomesCursos) {
        List<Curso> cursosEncontrados = new ArrayList<>();
        for (String nomeCurso : nomesCursos) {
            for (Curso curso : cursos) {
                if (curso.getNomeCurso().equalsIgnoreCase(nomeCurso)) {
                    cursosEncontrados.add(curso);
                }
            }
        }
        return cursosEncontrados;
    }

    public List<Curso> getCursos() {
        return cursos;
    }
    
    public Map<Estudante, List<Curso>> getMatriculas() {
        return matriculas;
    }
    
    public Map<Professor, List<Curso>> getAssociacoes() {
        return associacoes;
    }
}


