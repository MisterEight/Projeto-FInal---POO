// Estudante.java
public class Estudante extends Pessoa {
    private String matricula;

    public Estudante(String nome, int idade, String matricula) {
        super(nome, idade);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    @Override
    public void exibirDados() {
        System.out.println("Aluno: " + getNome() + ", Idade: " + getIdade() + ", Matrícula: " + matricula);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudante estudante = (Estudante) o;
        return matricula.equals(estudante.matricula);
    }

    @Override
    public int hashCode() {
        return matricula.hashCode();
    }
}