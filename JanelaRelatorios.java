// JanelaRelatorios.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class JanelaRelatorios extends JFrame {
    private Relatorios relatorios;

    public JanelaRelatorios() {
        // Configurações básicas
        setTitle("Relatórios");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carregar dados usando GerenciadorDeDados
        GerenciadorDeDados gerenciadorDeDados = new GerenciadorDeDados();
        List<Estudante> estudantes = gerenciadorDeDados.carregarEstudantes();
        List<Professor> professores = gerenciadorDeDados.carregarProfessores();
        List<Curso> cursos = gerenciadorDeDados.carregarCursos();
        Map<Estudante, List<Curso>> matriculas = gerenciadorDeDados.carregarMatriculas();
        Map<Professor, List<Curso>> associacoes = gerenciadorDeDados.carregarAssociacoes();

        relatorios = new Relatorios(estudantes, professores, cursos, matriculas, associacoes);

        // Painel e botões
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnRelatorioEstudantes = new JButton("Relatório de Estudantes");
        JButton btnRelatorioProfessores = new JButton("Relatório de Professores");
        JButton btnVoltar = new JButton("Voltar");

        painel.add(btnRelatorioEstudantes);
        painel.add(btnRelatorioProfessores);
        painel.add(btnVoltar);

        add(painel);

        // Ações dos botões
        btnRelatorioEstudantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                relatorios.gerarRelatorioEstudantes();
            }
        });

        btnRelatorioProfessores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                relatorios.gerarRelatorioProfessores();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
}
