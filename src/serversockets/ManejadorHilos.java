package serversockets;

import funciontareas.Tarea3A;
import funciontareas.Tarea3B;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;

public class ManejadorHilos implements Runnable {
    
    private DataInputStream dis;
    private DataOutputStream dos;
    private FileWriter fileW;
    private Socket sk;
    private int id;

    public ManejadorHilos(Socket sk, int id) {
        this.sk = sk;
        this.id = id;
    }
    
    @Override 
    public void run(){
        System.out.println("conexion " + id + " recibida--->"+ sk.getInetAddress().getHostName());
        try {                       
            dis = new DataInputStream(sk.getInputStream());
            dos = new DataOutputStream((sk.getOutputStream()));
            
            while(true){
            dos.writeUTF("Elige una opcion:");
            dos.writeUTF("1.- Buscar una letra");
            dos.writeUTF("2.- Buscar una palabra");
            dos.writeUTF("3.- Salir");                       
            
            int opc = dis.readInt();
            switch(opc){
                case 1:
                    dos.writeUTF("Letra a buscar: ");
                    Tarea3A p = new Tarea3A();
                    dos.writeInt(p.tareaA(dis.readUTF()));
                    //System.out.println(dis.readInt());                    
                break;
                case 2:
                    dos.writeUTF("Palabra a buscar: ");
                    Tarea3B h = new Tarea3B();
                    h.tareaB(dis.readUTF());
                    dos.writeInt(h.getApariciones());
                    //System.out.println(dis.readInt());                    
                break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    dos.writeUTF("La opción ingresada no es válida");
                break;
            }
            }

        } catch (IOException ex) {
            System.out.println("Terminado " + ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ManejadorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
