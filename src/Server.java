import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by vicesource on 04-04-2016.
 */
public class Server extends UnicastRemoteObject implements ServerInterface{
    static String FILE_USERS = "users.dat";
    static String FILE_UC = "uc.dat";
    private static ClientInterface client;
    public static ArrayList<UC> unidades=new ArrayList<UC>();
    public static ArrayList<Utilizador> utilizadores=new ArrayList<Utilizador>();
    int nligacoes = 0;

    public Server() throws RemoteException {
        super();
    }

    class ThreadTCP extends Thread{

        public void run(){
            try {
                ServerSocket ssc = new ServerSocket(2222);
                while(true){
                    Socket sc = ssc.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());
                    oos.writeObject("Introduza a password:");
                    String pw = (String)ois.readObject();
                    if (!pw.equals("password")){
                        oos.writeObject("Password errada. Adeus.");
                        break;
                    }
                    int op;
                    do{
                        oos.writeObject("Bem-vindo ao cliente Admin. Escolha uma opção:" +
                                "1 - Apagar soluções erradas." +
                                "2 - Obter número de ligações ao servidor." +
                                "0 - Sair. ");
                        op=ois.readInt();
                        switch (op){
                            case 1: {
                                
                            }
                        }
                    }while( op != 0);
                    oos.close();
                    ois.close();
                    sc.close();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        //Init server
        System.setSecurityManager(new SecurityManager());

        try {
            if (new File(FILE_USERS).exists())
                utilizadores = (ArrayList<Utilizador>)(new ObjectInputStream(new FileInputStream(FILE_USERS))).readObject();
            else System.out.println("Aviso: Não existem utilizadores guardados.");
            if (new File(FILE_UC).exists()) {
                unidades = (ArrayList<UC>) (new ObjectInputStream(new FileInputStream(FILE_UC))).readObject();
            }
            else System.out.println("Aviso: Não existem unidades curriculares guardadas.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //END INIT

        //Connections
        Server server = null;
        try {

            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                server = new Server();
                Naming.rebind("Host",server);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void aumentaNumLigacoes(){
        nligacoes++;
    }

    public String mostraUCs() throws RemoteException{
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Escolha uma unidade curricular:\n");
        for (int i=1; i<= unidades.size(); i++){
            stringBuilder.append(i + " - #"+unidades.get(i-1).getNumero() + " " + unidades.get(i-1).getNome()+"\n");
        }
        return stringBuilder.toString();
    }

    public void adicionarUC(int numero, String nome ) throws RemoteException{
        unidades.add(new UC(numero,nome));
    }

    public void guardarUCs() throws RemoteException{
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(FILE_UC));
            os.writeObject(unidades);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientInterface c) throws RemoteException {
        client = c;
    }

    public void exercicioInserir(int nUC, int numero, String enunciado, Utilizador user) throws RemoteException{
        unidades.get(nUC-1).adicionaExercicio(new Exercicio(numero, enunciado,user));
    }

    public String exercicioConsultar(int nUC) throws RemoteException{
        StringBuilder sb = new StringBuilder();
        sb.append(unidades.get(nUC-1).getNome() + " - Lista de Exercícios: \n");
        for (Exercicio ex : unidades.get(nUC-1).getExercicios()){
            sb.append("#"+ex.getNumero()+": "+ex.getEnunciado()+"\n");
        }
        return sb.toString();
    }

    public String exercicioConsultarApagar(int nUC, Utilizador user){
        StringBuilder sb = new StringBuilder();
        if (!unidades.get(nUC-1).existsExerciciosEnviados(user))
            return "Não existe nenhum exercício que possa apagar.";
        int i=1;
        for (Exercicio ex : unidades.get(nUC-1).getExercicios()){
            if (ex.getUser().getUsername().equals(user.getUsername())) sb.append(i +" - #"+ex.getNumero()+" "+ex.getEnunciado()+"\n");
            i++;
        }
        return sb.toString();
    }

    public String exercicioConsultarEncerrar(int nUC, Utilizador user){
        StringBuilder sb = new StringBuilder();
        if (!unidades.get(nUC-1).existsExerciciosEnviadosEncerrar(user))
            return "Não existe nenhum exercício que possa encerrar.";
        int i=1;
        for (Exercicio ex : unidades.get(nUC-1).getExercicios()){
            if (ex.getUser().getUsername().equals(user.getUsername()) && ex.isEstado()) sb.append(i +" - #"+ex.getNumero()+" "+ex.getEnunciado()+"\n");
            i++;
        }
        return sb.toString();
    }

    public void exercicioApagar(int nUC, int nEx) throws RemoteException{
        unidades.get(nUC-1).getExercicios().remove(nEx-1);
    }

    public void exercicioEncerrar(int nUC, int nEx) throws RemoteException{
        unidades.get(nUC-1).getExercicios().get(nEx-1).setEstado(false);
    }

    public void solucaoInserir(int nEX, int nUC, String solucaoString, Utilizador solucaoUser) throws RemoteException{
        unidades.get(nUC-1).getExercicios().get(nEX-1).adicionaSolucao(new Solucao(solucaoString,solucaoUser));
    }


    public void solucaoClassificar(int nUC, int nEX, int nSOL, int flag) throws RemoteException {
        if (flag == 1)
            unidades.get(nUC).getExercicios().get(nEX).getSolucoes().get(nSOL).validarSolucao(); // certa
        else
            unidades.get(nUC).getExercicios().get(nEX).getSolucoes().get(nSOL).recusarSolucao(); // errada
    }

    public String solucaoMostra(int nUC, int nEX) throws RemoteException{

        StringBuilder sb = new StringBuilder();
        sb.append("# - Estado - Candidato - Resposta \n");
        if (unidades.get(nUC-1).getExercicios().get(nEX-1).getSolucoes().isEmpty())
            return "Não existe nenhuma solução para este exercício.";
        int i;
        int flag;
        for (i=1; i<= unidades.get(nUC-1).getExercicios().get(nEX-1).getSolucoes().size(); i++){
            sb.append(i + " - ");
            flag = unidades.get(nUC-1).getExercicios().get(nEX-1).getSolucoes().get(i-1).getEstado();
            if(flag==0)
                sb.append("Por classificar");
            if(flag==1)
                sb.append("Correcta");
            if(flag==-1)
                sb.append("Incorrecta");
            sb.append(" - " + unidades.get(nUC-1).getExercicios().get(nEX-1).getSolucoes().get(i-1).getUser().getUsername() + " - " + unidades.get(nUC-1).getExercicios().get(nEX-1).getSolucoes().get(i-1).getResposta() + "\n");
        }
        return sb.toString();
    }

    public void solucaoApagarErradas(int nUC, int nEX, int nSOL) throws RemoteException{
        unidades.get(nUC).getExercicios().get(nEX).getSolucoes().remove(nSOL);
    }

    public int  obterAcessos() throws RemoteException{return 0;}

    public ArrayList<UC> getUnidades() throws RemoteException{
        return unidades;
    }

    public ArrayList<Utilizador> getUtilizadores() throws RemoteException{
        return utilizadores;
    }
}
