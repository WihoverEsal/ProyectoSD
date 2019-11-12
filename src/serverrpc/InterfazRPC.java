package serverrpc;

import funciontareas.*;
import java.io.IOException;

public class InterfazRPC {
    public int buscaLetra(String pal) throws InterruptedException{
        Tarea3A tA = new Tarea3A();
        int matches = tA.tareaA(pal);
                                
        return matches;
    }
    
    public int buscaPalabra(String pal) throws InterruptedException, IOException{
        Tarea3B tB = new Tarea3B();
        tB.tareaB(pal);
        int matches = tB.getApariciones();
                
        return matches;
    }
    
    public String desplegaMenu(){
        return "1.- Busca Letra\n2.- Busca Palabra\n3.-Salir\n\n"
                + "Presiona el numero correspondiente a la opción: ";
    }
}
