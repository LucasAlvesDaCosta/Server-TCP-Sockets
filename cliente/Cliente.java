
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//Prefira implementar a interface Runnable do que extender a classe Thread, pois neste caso utilizaremos apena o método run.
public class Cliente implements Runnable {

	private Socket cliente;

	public Cliente(Socket cliente) {
		this.cliente = cliente;
	}

	public static void main(String args[]) throws UnknownHostException, IOException {

		// para se conectar ao servidor, cria-se objeto Socket.
		// O primeiro parâmetro é o IP ou endereço da máquina que
		// se quer conectar e o segundo é a porta da aplicação.
		// Neste caso, usa-se o IP da máquina local (127.0.0.1)
		// e a porta da aplicação ServidorDeEco (12345).
		Socket socket = new Socket("10.8.233.90", 12345);

		/*
		 * Cria um novo objeto Cliente com a conexão socket para que seja executado em
		 * um novo processo. Permitindo assim a conexão de vários clientes com o
		 * servidor.
		 */
		Cliente c = new Cliente(socket);
		Thread t = new Thread(c);
		t.start();
	}

	public void run() {
		try {
			System.out.println("Conectado com o servidor: "+this.cliente.getInetAddress());
                        
                          Scanner ler = new Scanner(System.in);
			// Prepara o arquivo a ser lido
                          File entrada = new File ("gabarito.txt");
                          // criando um buffer para armazenar o conteudo do arquivo em forma de stream
                          BufferedInputStream ler2 = new BufferedInputStream(new FileInputStream(entrada));
                          int arq =0;
                          String Lista="";
                          String resp = "";
                    //leio o arquivo e gravo em uma str para enviar ao cliente
                     while((arq = ler2.read()) != -1){
                         Lista+=(char)arq;
                     }
                 	// Cria objeto para enviar a mensagem ao servidor
			OutputStream Saida;
			// Envia mensagem ao servidor
                          Saida = this.cliente.getOutputStream();
                          Saida.write(Lista.getBytes());
                          Saida.write(4);
                          Saida.flush();
		       // cria o objeto para receber a mensagem do cliente
                       InputStream Recive;
                        Recive = this.cliente.getInputStream();
                          char data = 0;
                          while((data = (char)Recive.read())!= 4){
                            if(data =='Ã'){// condição para corrigir o problema de acentuação utf-8
                                resp +="ã";
                            }    
                              else
                               if(data=='£'){
                                  resp +="";
                               }// ................................................................
                                else
                                 resp += (char)data;
                            } 
                            System.out.println("Resposta: \n"+resp);
   
                         System.out.println("SAIR DA APLICAÇÃO?");
                         if(ler.hasNextLine()){
                            this.cliente.close();
                            Recive.close();
                            Saida.close();
                            System.out.println("Fim do cliente!");   
                         } 
  	
		} 
                 catch (IOException e) {
	          System.out.println("Não foi possivel completar a ação devido a exeção: "+e.getMessage());
		 }
	}
}
