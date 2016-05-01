import java.io.Serializable;

/**
 * Created by vicesource on 04-04-2016.
 */
public class Solucao implements Serializable {
    private String resposta;
    private int estado = 0; // 0 por validar, -1 errado, 1 correto
    private Utilizador user;

    public Solucao(String resposta, Utilizador user){
        this.resposta=resposta;
        this.user = user;
    }

    public void validarSolucao(){
        estado=1;
    }

    public void recusarSolucao(){
        estado=-1;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Utilizador getUser() {
        return user;
    }

    public void setUser(Utilizador user) {
        this.user = user;
    }

}
