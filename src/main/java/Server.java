import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println(clientHandler.getClientUserName() + " has connected to the chat.");
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            closeEverything(serverSocket);
        }
    }

public void closeEverything(ServerSocket serverSocket){
        try{
          if(serverSocket != null){
              serverSocket.close();
          }
        } catch (IOException e){
            e.printStackTrace();
        }
}

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(5555);
        Server server = new Server(serverSocket1);
        server.startServer();
    }
}
