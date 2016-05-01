import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by andre on 07/04/2016.
 */
public class Client extends UnicastRemoteObject implements ClientInterface{
    public Client() throws RemoteException {
        super();
    }

    public static void main(String[] args){
        System.setSecurityManager(new SecurityManager());
        Utilizador user = new Utilizador("Andre","pw");
        //Utilizador user = new Utilizador("Ze","pw");
        UC uc = null;
        try{
            Client client = new Client();
            ServerInterface server = (ServerInterface) Naming.lookup("Host");
            server.subscribe(client);
            System.out.println("Bem-vindo "+user.getUsername()+"!");
            server.aumentaNumLigacoes();
            if(server.getUnidades().isEmpty()){
                System.out.println("Erro: Não existem UCs. Adicione a primeira agora.");
                System.out.println("Nome: ");
                String nome= client.readString();
                System.out.println("Número: ");
                int numero = client.readInt();
                server.adicionarUC(numero, nome);
                server.guardarUCs();
            }
            int opUC, opEx;
            do {
                System.out.println(server.mostraUCs());
                System.out.println("0 - Sair");
                opUC = client.readInt();
                if (opUC > 0 && opUC <= server.getUnidades().size()){
                    uc = server.getUnidades().get(opUC-1);

                    do {
                        client.menuUC(uc);
                        opEx=client.readInt();
                        switch(opEx){
                            case 1: {
                                client.consultarExercicios(opUC,server,user);
                                System.out.println(server.exercicioConsultar(opUC));
                                break;
                            }
                            case 2: {
                                client.adicionarExercicio(opUC,server,user);
                                break;
                            }
                            case 3:{
                                client.apagarExercicio(opUC, user, server);
                                break;
                            }
                            case 4:{
                                client.encerrarExercicio(opUC,user,server);
                                break;
                            }
                        }
                    }while(opEx != 0 );
                }

            }while(opUC != 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }

    public String readString() throws RemoteException{
        return new Scanner(System.in).nextLine();
    }

    public int readInt() throws RemoteException{
        try{
            int i =new Scanner(System.in).nextInt();
            return i;
        }
        catch(InputMismatchException e){
            return -1;
        }
    }

    void menuUC(UC uc){
        System.out.println("#" + uc.getNumero() + " - " + uc.getNome());
        System.out.println("Escolha uma opção: \n" +
                "1 - Consultar exercícios da UC.\n" +
                "2 - Adicionar um exercício.\n" +
                "3 - Apagar um exercício.\n" +
                "4 - Encerrar um exercício.\n" +
                "0 - Voltar. ");
    }

    void adicionarExercicio(int nUC, ServerInterface server, Utilizador user) throws RemoteException {
        int numero;
        UC uc = server.getUnidades().get(nUC-1);
        System.out.println(uc.getNome());
        if(uc.getExercicios().isEmpty()) numero=1;
        else {
            numero= uc.getExercicios().get(uc.getExercicios().size()-1).getNumero()+1;
        }
        System.out.println("Introduza o enunciado do exercício: ");
        server.exercicioInserir(nUC, numero, readString(), user);
        server.guardarUCs();
    }

    void apagarExercicio(int nUC, Utilizador user,ServerInterface server) throws RemoteException{
        String s = server.exercicioConsultarApagar(nUC,user);
        if(s.equals("Não existe nenhum exercício que possa apagar.")){
            System.out.println(s);
            return;
        }
        int op;
        do{
            System.out.println("Escolha o exercício a apagar:");
            s = server.exercicioConsultarApagar(nUC,user);
            System.out.println(s);
            System.out.println("0 - Voltar.");
            op = readInt();
            if (op > 0 && op <= server.getUnidades().get(nUC-1).getExercicios().size()){
                server.exercicioApagar(nUC, op);
            }
        }while(op != 0);
        server.guardarUCs();
    }

    void encerrarExercicio(int nUC, Utilizador user, ServerInterface server) throws RemoteException{
        String s = server.exercicioConsultarEncerrar(nUC,user);
        if(s.equals("Não existe nenhum exercício que possa encerrar.")){
            System.out.println(s);
            return;
        }
        int op;
        do{
            System.out.println("Escolha o exercício a encerrar:");
            s = server.exercicioConsultarEncerrar(nUC,user);
            System.out.println(s);
            System.out.println("0 - Voltar.");
            op = readInt();
            if (op > 0 && op <= server.getUnidades().get(nUC-1).getExercicios().size()){
                server.exercicioEncerrar(nUC, op);
            }
        }while(op != 0);
        server.guardarUCs();
    }

    void consultarExercicios(int nUC, ServerInterface server, Utilizador user) throws RemoteException {
        int menuopt=0;
        do {
            //System.out.println("UC ---- " + server.getUnidades().get(nUC-1).getNome() + " ---- Exercícios");
            if(server.getUnidades().get(nUC-1).getExercicios().isEmpty()) {
                System.out.println("Não há exercícios para esta UC.");
                break;
            }else {

                try {
                    System.out.println(server.exercicioConsultar(nUC));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    menuopt = readInt();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                //Inserir soluçoes,
                //Classificar soluçoes se for owner
                try {
                    if (menuopt > 0 && menuopt <= server.getUnidades().get(nUC-1).getExercicios().size()) {
                        int menuopt2 = 0;
                        do {
                            System.out.println("Escolha uma opção: \n" +
                                    "1 - Inserir uma solução\n" +
                                    "2 - Classificar soluções\n" +
                                    "3 - Apagar soluções erradas\n" +
                                    "0 - Sair");

                            switch (menuopt2 = readInt()) {
                                case 1: {
                                    System.out.println("Solução:");
                                    //server.solucaoInserir(menuopt, nUC, readString(), user);
                                    String s = readString();
                                    server.solucaoInserir(menuopt, nUC, s, user);
                                    server.guardarUCs();
                                    break;
                                }
                                case 2: {
                                    if(server.getUnidades().get(nUC-1).getExercicios().get(menuopt-1).getSolucoes().isEmpty()){
                                        System.out.println("Não há soluções propostas para este exercício");
                                        break;
                                    }
                                    System.out.println("Soluções propostas para o exercício #" + server.getUnidades().get(nUC-1).getExercicios().get(menuopt-1).getNumero() +"-" + server.getUnidades().get(nUC-1).getExercicios().get(menuopt-1).getEnunciado() + ":");
                                    int menuopt3 = 0;
                                    do{
                                        System.out.println(server.solucaoMostra(nUC,menuopt)); //lista as soluçoes
                                        System.out.println("Qual a solução que deseja classificar?");
                                        menuopt3=readInt();
                                        if(menuopt3 > 0 && menuopt3 <= server.getUnidades().get(nUC-1).getExercicios().get(menuopt-1).getSolucoes().size()){
                                            int classiflag=-1;
                                            do {
                                                System.out.println("Enunciado: " + server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getEnunciado());
                                                System.out.println("Resposta: " + server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().get(menuopt3 - 1).getResposta());
                                                System.out.println("Esta resposta está correcta? \n 1 - Sim \n 0 - Não \n 2 - Retroceder");
                                                classiflag=readInt();
                                            }while(classiflag!=0 && classiflag!=1 && classiflag!=2);
                                            if(classiflag==2) break;
                                            server.solucaoClassificar(nUC - 1, menuopt - 1, menuopt3 - 1, classiflag);
                                            menuopt3=0;
                                            server.guardarUCs();
                                            break;
                                        }
                                    }while(menuopt3 != 0);
                                    break;
                                }
                                case 3: {
                                    int i = 0;
                                    if(server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().size() == 0){
                                        System.out.println("Não há soluções propostas ou erradas.");
                                        break;
                                    }
                                    int flagErrada=-1;
                                    for (i=0; i < server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().size(); i++){
                                        if(server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().get(i).getEstado()==-1){
                                            do {
                                                System.out.println("Apagar a solução incorrecta de " + server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().get(i).getUser().getUsername() + " '" + server.getUnidades().get(nUC - 1).getExercicios().get(menuopt - 1).getSolucoes().get(i).getResposta() + "' ? 1 - Sim   0 - Não\n");
                                            }while(flagErrada!=0 && flagErrada!=1);
                                            if(flagErrada==1) server.solucaoApagarErradas(nUC-1,menuopt-1,i);
                                            else continue;
                                            server.guardarUCs();
                                            //faltava confirmar se o user actual era o user criador mas o monteiro é um fag
                                        }
                                    }
                                    break;
                                }
                            }
                        } while (menuopt2 != 0);
                    }
                    else {
                        System.out.println("Não existe esse exercício.");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }while(menuopt!=0);
    }


}
