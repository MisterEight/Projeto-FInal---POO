// GerenciarAlunos.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciarAlunos extends JFrame {
    private List<Estudante> estudantes = new ArrayList<>();
    private final String caminhoArquivo = "alunos.txt";

    public GerenciarAlunos() {
        // Configurações básicas da janela
        setTitle("Gerenciamento de Alunos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));

        // Botões de ações
        JButton btnCadastrar = new JButton("Cadastrar Aluno");
        JButton btnConsultar = new JButton("Consultar Aluno");
        JButton btnVoltar = new JButton("Voltar");

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(1, 3, 10, 10));
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnConsultar);
        painelBotoes.add(btnVoltar);

        painel.add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarAluno();
            }
        });

        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarAluno();
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

    private void cadastrarAluno() {
        // Janela de cadastro
        JTextField nomeField = new JTextField();
        JTextField idadeField = new JTextField();
        JTextField matriculaField = new JTextField();

        Object[] message = {
            "Nome:", nomeField,
            "Idade:", idadeField,
            "Matrícula:", matriculaField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Cadastro de Aluno",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText();
                int idade = Integer.parseInt(idadeField.getText());
                String matricula = matriculaField.getText();

                Estudante estudante = new Estudante(nome, idade, matricula);
                estudantes.add(estudante);

                salvarDados();
                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarAluno() {
        String busca = JOptionPane.showInputDialog(this, "Digite o nome ou matrícula do aluno:");
        if (busca != null && !busca.isEmpty()) {
            carregarDados();

            for (Estudante estudante : estudantes) {
                if (estudante.getNome().equalsIgnoreCase(busca) || estudante.getMatricula().equalsIgnoreCase(busca)) {
                    exibirOpcoesAluno(estudante);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Aluno não encontrado!");
        }
    }

    private void exibirOpcoesAluno(Estudante estudante) {
        String[] opcoes = {"Editar", "Excluir", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Aluno encontrado: " + estudante.getNome(),
                "Opções",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 0 -> editarAluno(estudante);
            case 1 -> excluirAluno(estudante);
        }
    }

    private void editarAluno(Estudante estudante) {
        JTextField nomeField = new JTextField(estudante.getNome());
        JTextField idadeField = new JTextField(String.valueOf(estudante.getIdade()));

        Object[] message = {
            "Nome:", nomeField,
            "Idade:", idadeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Editar Aluno",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                estudante.setNome(nomeField.getText());
                estudante.setIdade(Integer.parseInt(idadeField.getText()));
                salvarDados();
                JOptionPane.showMessageDialog(this, "Aluno editado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirAluno(Estudante estudante) {
        estudantes.remove(estudante);
        try {
            salvarDados();
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarDados() throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Estudante estudante : estudantes) {
            linhas.add(estudante.getNome() + "," + estudante.getIdade() + "," + estudante.getMatricula());
        }
        GerenciadorDeArquivos.escreverDados(caminhoArquivo, linhas);
    }

    void carregarDados() {
        estudantes.clear();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados(caminhoArquivo);
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Estudante estudante = new Estudante(partes[0], Integer.parseInt(partes[1]), partes[2]);
                estudantes.add(estudante);
            }
        } catch (IOException ex) {
            // Arquivo pode não existir na primeira execução
        }
    }

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
            JOptionPane.showMessageDialog(null, "Erro ao carregar estudantes!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return estudantes;
    }
}
