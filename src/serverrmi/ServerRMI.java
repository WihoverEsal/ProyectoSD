/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverrmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import funciontareas.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ServerRMI {
    private static final int PUERTO = 8085; //Si cambias aquí el puerto, recuerda cambiarlo en el cliente
    Registry registry;
    //public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
    public void executeRMI() throws RemoteException, AlreadyBoundException, MalformedURLException{
        Remote remote = UnicastRemoteObject.exportObject(new InterfazRMI() {
            /*
                            Sobrescribir opcionalmente los métodos que escribimos en la interfaz
            */
            @Override
            public int buscaLetra(String pal) throws RemoteException, InterruptedException {
                Tarea3A tA = new Tarea3A();
                int matches = tA.tareaA(pal);
                                
                return matches;
            };

            @Override
            public int buscaPalabra(String pal) throws RemoteException, IOException, InterruptedException {                
                Tarea3B tB = new Tarea3B();
                tB.tareaB(pal);
                int matches = tB.getApariciones();
                
                return matches;
            };

            @Override
            public String mostrarMenu() throws RemoteException {
                return "\n\n------------------\n\n[1] => Buscar letra\n[2] => Buscar palabra\n[3] => Salir\nElige: ";
            };            
        }, 0);
        this.registry = LocateRegistry.createRegistry(PUERTO);
        
       	//System.out.println("Servidor RMI escuchando en el puerto " + String.valueOf(PUERTO));
//        registry.bind("Calculadora", remote); // Registrar calculadora
        Naming.rebind("//localhost:8085/Calculadora",remote);        
    }
    
    public void apagaRMI() throws RemoteException, NotBoundException, MalformedURLException{
        this.registry.unbind("//localhost:8085/Calculadora");        
        Naming.unbind("//localhost:8085/Calculadora");
    }
}