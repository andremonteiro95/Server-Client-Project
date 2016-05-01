import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by vicesource on 04-04-2016.
 */
public interface ServerInterface extends java.rmi.Remote{
    public void subscribe(ClientInterface c) throws java.rmi.RemoteException ;
    public String mostraUCs() throws java.rmi.RemoteException;
    public void adicionarUC(int numero,String nome) throws RemoteException;
    public void guardarUCs() throws RemoteException;
    public void exercicioInserir(int nUC, int numero, String enunciado, Utilizador user) throws java.rmi.RemoteException;
    public void exercicioApagar(int nUC, int nEx) throws java.rmi.RemoteException;
    public void exercicioEncerrar(int nUC, int nEx) throws java.rmi.RemoteException;
    public void solucaoInserir(int nEX, int nUC, String solucaoString, Utilizador solucaoUser) throws java.rmi.RemoteException;
    public String solucaoMostra(int nUC, int nEX) throws java.rmi.RemoteException;
    public void solucaoClassificar(int nUC, int nEX, int nSOL, int flag) throws java.rmi.RemoteException;
    public String exercicioConsultar(int nUC) throws java.rmi.RemoteException;
    public void solucaoApagarErradas(int nUC, int nEX, int nSOL) throws java.rmi.RemoteException;
    public int  obterAcessos() throws java.rmi.RemoteException;
    public ArrayList<UC> getUnidades() throws java.rmi.RemoteException;
    public ArrayList<Utilizador> getUtilizadores() throws java.rmi.RemoteException;
    public String exercicioConsultarApagar(int nUC, Utilizador user) throws java.rmi.RemoteException;
    public String exercicioConsultarEncerrar(int nUC, Utilizador user) throws java.rmi.RemoteException;
    public void aumentaNumLigacoes() throws java.rmi.RemoteException;


    //inserir exercicio, apagar, inserir
    //soluçao, consultar UCS, consultar exercicios
    //abertos e encerrados
    //consultar ex por uc e numero
    //classificar solucao, encerrar exercicio
}
