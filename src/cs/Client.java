package cs;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import org.apache.xmlrpc.*;

public class Client {
    
    public static void main(String[] args) {
        int x1=0, x2=0;
        String c1 = "";
        try{
            XmlRpcClient rpcClient = new XmlRpcClient("http://localhost:8080/");
            Vector<Integer> params = new Vector<Integer>();
            Vector<Object> parameters = new Vector<>();
            int opc = 0; 
            Object procMenu = new Object();
            Scanner sc = new Scanner(System.in);
            System.out.println("Conexion exitosa");
            
            Object menu = rpcClient.execute("myServer.desplegaMenu", params);//Menu Sockets, RPC, RMI
            do{ 
                params.clear();
                System.out.println(menu);//Menu Sockets, RPC, RMI
               
                opc = Integer.parseInt(sc.nextLine());
                params.add(new Integer(opc));                
                procMenu = rpcClient.execute("myServer.solicitudMenu", params);
                
                System.out.println(procMenu);//Menu busca letra, busca palabra
            }while(procMenu.equals(new String("Opcion no valida")));
            params.clear();
            
            int opc2 = opc;
            
            opc = Integer.parseInt(sc.nextLine());// Lee la opcion del menu funcionalidad. 1 o 2
            while(opc < 1 || opc > 3){
                System.out.println("Opcion no valida. Vuelve a intentar");
                System.out.println(procMenu);
                opc = Integer.parseInt(sc.nextLine());// Lee la opcion del menu funcionalidad. 1 o 2
            }
            
            params.add(new Integer(opc));            
            
            if(opc2 == 1){
                params.add(new Integer(opc));
                params.add(new Integer(c1));            
                Object procOper = rpcClient.execute("myServer.procesamientoSCK", params);
                System.out.println(procOper);
            }else if(opc2 == 2){
                Object procOper = rpcClient.execute("myServer.pideLetraoPal", params);
                System.out.println(procOper);//Pide letra o palabra
                String pal = sc.nextLine();
                
                parameters.clear();
                parameters.add(new Integer(opc));
                parameters.add(new String(pal));
                
                /*params.add(new Integer(opc));
                params.add(new Integer(x1));
                params.add(new Integer(x2));*/
                procOper = rpcClient.execute("myServer.procesamientoRPC", parameters);
                System.out.println(procOper);
            }else if(opc2 == 3){
                Object procOper = rpcClient.execute("myServer.pideLetraoPal", params);
                System.out.println(procOper);//Pide letra o palabra
                String pal = sc.nextLine();//Lee la palabra o letra a buscar
                parameters.clear();
                parameters.add(new Integer(opc));
                parameters.add(new String(pal));
                
                procOper = rpcClient.execute("myServer.procesamientoRMI", parameters);
                System.out.println(procOper);
            }
            
            sleep(1000);
        }catch(Exception ex){
            System.err.println("Client: " + ex);
        }        
    }    
}
