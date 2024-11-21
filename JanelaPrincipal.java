// JanelaPrincipal.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaPrincipal extends JFrame {
    public JanelaPrincipal() {
        // Configurações básicas da janela
        setTitle("Gerenciamento Estudantil");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // Painel principal
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(5, 1, 10, 10)); // Alterado para 5 linhas (adicionar botão de relatórios)

        // Botões do menu principal
        JButton btnAlunos = new JButton("Gerenciar Alunos");
        JButton btnProfessores = new JButton("Gerenciar Professores");
        JButton btnCursos = new JButton("Gerenciar Cursos");
        JButton btnRelatorios = new JButton("Gerar Relatórios"); // Novo botão
        JButton btnSair = new JButton("Sair");

        // Adicionar botões ao painel
        painel.add(btnAlunos);
        painel.add(btnProfessores);
        painel.add(btnCursos);
        painel.add(btnRelatorios); // Novo botão
        painel.add(btnSair);

        // Adicionando o painel à janela
        add(painel);

        // Ações dos botões
        btnAlunos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GerenciarAlunos();
            }
        });

        btnProfessores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GerenciarProfessores();
            }
        });

        btnCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GerenciarCursos();
            }
        });

        btnRelatorios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaRelatorios(); // Abre a janela de relatórios
            }
        });

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Encerra o programa
            }
        });

        setVisible(true); // Exibe a janela
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaPrincipal());
    }
}