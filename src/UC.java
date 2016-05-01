import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vicesource on 04-04-2016.
 */
public class UC implements Serializable{
    private int numero;
    private String nome;
    private ArrayList<Exercicio> exercicios=new ArrayList<Exercicio>();

    public UC(int numero, String nome) {
        this.numero = numero;
        this.nome = nome;
    }

    public boolean existsExerciciosEnviados(Utilizador user){
        for (Exercicio ex : exercicios){
            if (ex.getUser().getUsername().equals(user.getUsername()))
                return true;
        }
        return false;
    }

    public boolean existsExerciciosEnviadosEncerrar(Utilizador user){
        for (Exercicio ex : exercicios){
            if (ex.getUser().getUsername().equals(user.getUsername()) && ex.isEstado())
                return true;
        }
        return false;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(ArrayList<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }

    public void adicionaExercicio(Exercicio ex){
        exercicios.add(ex);
    }
}
