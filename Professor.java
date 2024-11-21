// Professor.java
public class Professor extends Pessoa {
    private String especialidade;

    public Professor(String nome, int idade, String especialidade) {
        super(nome, idade);
        this.especialidade = especialidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public void exibirDados() {
        System.out.println("Professor: " + getNome() + ", Idade: " + getIdade() + ", Especialidade: " + especialidade);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return nome.equals(professor.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
}
}