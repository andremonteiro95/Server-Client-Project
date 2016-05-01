import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vicesource on 04-04-2016.
 */
public class Exercicio implements Serializable {
    //a cada exercício é atribuído um número sequencial dentro da UC e é-lhe
    //associada uma etiqueta (estado) que assinala que é um exercício em “aberto”. Os dados do
    //exercício serão armazenados no ficheiro exercícios.dat
    private int numero;
    private boolean estado = true; // aberto por default
    private String enunciado = null;
    private ArrayList<Solucao> solucoes = new ArrayList<Solucao>();
    private Utilizador user;

    public Exercicio(int numero, String enunciado, Utilizador user) {
        this.numero = numero;
        this.enunciado = enunciado;
        this.user = user;
    }

    public boolean isEstado() {
        return estado;
    }

    public void fecharExercicio() {
        this.estado = false;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public ArrayList<Solucao> getSolucoes() {
        return solucoes;
    }

    public void setSolucoes(ArrayList<Solucao> solucoes) {
        this.solucoes = solucoes;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Utilizador getUser() {
        return user;
    }

    public void setUser(Utilizador user) {
        this.user = user;
    }

    public void adicionaSolucao(Solucao solucao){
        solucoes.add(solucao);
    }
}
