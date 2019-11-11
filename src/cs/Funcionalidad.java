package cs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import serverrmi.*;
import serverrpc.*;

public class Funcionalidad {
    Socket conexionSCK;
    DataInputStream dis;
    DataOutputStream dos;
    XmlRpcClient conexionRPC;    
    Server_rpc srpc;
    InterfazRMI interfazRMI;
    ServerRMI sRMI;
    private static final int PUERTO = 8085; //Si cambias aquí el puerto, recuerda cambiarlo en el servidor
    
    public String desplegaMenu(){
        return "----- Bienvenido al Sistema Jhonys -----\n¿Con qué tipo de servidor desear trabajar?\n1.- Sockets\n2.- RPC\n3.- RMI\n\n"
                + "Presiona el numero correspondiente a la opción: ";
    }
    
    public String solicitudMenu(int opc) throws IOException{
        String resultado = "";
        if(opc == 1){
            
            this.conexionSCK = conectaSCK(); 
            
            this.dos  = new DataOutputStream(conexionSCK.getOutputStream());
            this.dis = new DataInputStream(conexionSCK.getInputStream());
            
            String menu = dis.readUTF() + dis.readUTF() + dis.readUTF() + dis.readUTF();
            return menu;
            
        }else if(opc == 2){
            try{
                this.conexionRPC = conectaRPC();
                Vector<Integer> params = new Vector<Integer>();
                System.out.println("Conexion exitosa al servidor RPC");
                Object menu = conexionRPC.execute("myServerRPC.desplegaMenu", params);
                resultado = (String) menu;

            }catch(Exception ex){
                System.err.println("Client: " + ex);
            }
            return resultado;
        }else if(opc == 3){            
            try{
                this.interfazRMI = conectaRMI();
                //System.out.println(interfazRMI.mostrarMenu());
                System.out.println("Conexion exitosa al servidor RMI");
                resultado = (String) interfazRMI.mostrarMenu();
                
            } catch (Exception ex) {
                System.err.println("Client: " + ex);
            }
            return resultado;
        }else{
            return "Opcion no valida";
        }        
        //return null;
    }
    
    public String procesamientoRPC(int opc, int x1, int x2) throws XmlRpcException, IOException{
        int suma = 0;
        Vector<Integer> params = new Vector<Integer>();
        params.add(x1);
        params.add(x2);
        if(opc == 1){
            Object resS = conexionRPC.execute("myServerRPC.suma", params);
            suma = ((Integer)resS).intValue();
            System.out.println("La suma es: " + suma);
            return "La suma es: "+suma;
        }else if(opc == 2){
            Object resS = conexionRPC.execute("myServerRPC.resta", params);
            int resta = ((Integer)resS).intValue();
            System.out.println("La resta es: " + resta);
            return "La resta es: "+resta;
        }else if(opc == 3){
            //Debemos cerrar la conexion con serverRPC            
            System.out.println("Servidor RPC desconectado");
            srpc.apagaRPC();
            return "Servidor RPC apagado";
        }else
            return "Opcion no valida";        
    }
    
    public XmlRpcClient conectaRPC() throws MalformedURLException{
        XmlRpcClient conexionRPC;
        
        this.srpc = new Server_rpc();
        srpc.executeRPC();        
        conexionRPC = new XmlRpcClient("http://localhost:8081/");
        
        return conexionRPC;
    }
    
    public String procesamientoRMI(int opc, int x1, int x2) throws RemoteException, NotBoundException, MalformedURLException{        
        float resultado = 0;
        
        if(opc == 1){
            resultado = interfazRMI.sumar(x1, x2);
            System.out.println("La suma es: " + resultado);
            return "La suma es: " + resultado;
        }else if(opc == 2){
            resultado = interfazRMI.restar(x1, x2);
            System.out.println("La resta es: " + resultado);
            return "La resta es: " + resultado;
        }else if(opc == 3){
            System.out.println("Servidor RMI desconectado");
            sRMI.apagaRMI();
            return "Servidor RMI desconectado";
        }else{
            System.out.println("Opcion no valida");
            return "Opcion no valida";
        }                
    }
    
    public InterfazRMI conectaRMI() throws RemoteException, AlreadyBoundException, MalformedURLException, AccessException, NotBoundException{
        this.sRMI = new ServerRMI();
        sRMI.executeRMI();
        
        int PUERTO = 8085; //Si cambias aquí el puerto, recuerda cambiarlo en el servidor
        Registry registry = LocateRegistry.getRegistry("localhost",PUERTO);
        InterfazRMI interfazRMI = (InterfazRMI) registry.lookup("Calculadora");
        
        return interfazRMI;
    }
    
    public Socket conectaSCK() throws IOException {
        Socket sk = new Socket("localhost", 8080);
        return sk;
    }
    
    public String procesamientoSCK(int opc, String x1) throws RemoteException, NotBoundException, MalformedURLException, IOException{        

        int aux = -1;
        int resultado = 0;

        while(aux != 3){

            aux = opc;
            dos.writeInt(aux);

            System.out.println(dis.readUTF());
            String s = x1;
            dos.writeUTF(s);
            resultado = dis.readInt();
            System.out.println(resultado);
    }
        dis.close();
        dos.close();
        conexionSCK.close();
    return "el numero es" + resultado;
    }
}

