// GerenciarProfessores.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciarProfessores extends JFrame {
    private List<Professor> professores = new ArrayList<>();
    private final String caminhoArquivo = "professores.txt";

    public GerenciarProfessores() {
        // Configurações básicas da janela
        setTitle("Gerenciamento de Professores");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));

        // Botões de ações
        JButton btnCadastrar = new JButton("Cadastrar Professor");
        JButton btnConsultar = new JButton("Consultar Professor");
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
                cadastrarProfessor();
            }
        });

        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarProfessor();
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

    private void cadastrarProfessor() {
        // Janela de cadastro
        JTextField nomeField = new JTextField();
        JTextField idadeField = new JTextField();
        JTextField especialidadeField = new JTextField();

        Object[] message = {
            "Nome:", nomeField,
            "Idade:", idadeField,
            "Especialidade:", especialidadeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Cadastro de Professor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText();
                int idade = Integer.parseInt(idadeField.getText());
                String especialidade = especialidadeField.getText();

                Professor professor = new Professor(nome, idade, especialidade);
                professores.add(professor);

                salvarDados();
                JOptionPane.showMessageDialog(this, "Professor cadastrado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void consultarProfessor() {
        String busca = JOptionPane.showInputDialog(this, "Digite o nome do professor:");
        if (busca != null && !busca.isEmpty()) {
            carregarDados();

            for (Professor professor : professores) {
                if (professor.getNome().equalsIgnoreCase(busca)) {
                    exibirOpcoesProfessor(professor);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Professor não encontrado!");
        }
    }

    private void exibirOpcoesProfessor(Professor professor) {
        String[] opcoes = {"Editar", "Excluir", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(
                this,
                "Professor encontrado: " + professor.getNome(),
                "Opções",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        switch (escolha) {
            case 0 -> editarProfessor(professor);
            case 1 -> excluirProfessor(professor);
        }
    }

    private void editarProfessor(Professor professor) {
        JTextField nomeField = new JTextField(professor.getNome());
        JTextField idadeField = new JTextField(String.valueOf(professor.getIdade()));
        JTextField especialidadeField = new JTextField(professor.getEspecialidade());

        Object[] message = {
            "Nome:", nomeField,
            "Idade:", idadeField,
            "Especialidade:", especialidadeField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Editar Professor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                professor.setNome(nomeField.getText());
                professor.setIdade(Integer.parseInt(idadeField.getText()));
                professor.setEspecialidade(especialidadeField.getText());

                salvarDados();
                JOptionPane.showMessageDialog(this, "Professor editado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Idade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirProfessor(Professor professor) {
        professores.remove(professor);
        try {
            salvarDados();
            JOptionPane.showMessageDialog(this, "Professor excluído com sucesso!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarDados() throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Professor professor : professores) {
            linhas.add(professor.getNome() + "," + professor.getIdade() + "," + professor.getEspecialidade());
        }
        GerenciadorDeArquivos.escreverDados(caminhoArquivo, linhas);
    }

    private void carregarDados() {
        professores.clear();
        try {
            List<String> linhas = GerenciadorDeArquivos.lerDados(caminhoArquivo);
            for (String linha : linhas) {
                String[] partes = linha.split(",");
                Professor professor = new Professor(partes[0], Integer.parseInt(partes[1]), partes[2]);
                professores.add(professor);
            }
        } catch (IOException ex) {
            // Arquivo pode não existir na primeira execução
        }
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
            JOptionPane.showMessageDialog(null, "Erro ao carregar professores!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return professores;
    }
}
