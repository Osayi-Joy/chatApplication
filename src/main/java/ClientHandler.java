import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements  Runnable{
    private String clientUserName;
    private Socket socket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String getClientUserName() {
        return clientUserName;
    }
    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadCastMessage(clientUserName + " has joined the chat.");
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }



    @Override
    public void run() {
    String messageFromClient;
    while (socket.isConnected()) {
        try{
            messageFromClient = bufferedReader.readLine();
            if (messageFromClient == null) throw new IOException();
            broadCastMessage(messageFromClient);
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
            break;
        }
    }
    }


    public void broadCastMessage(String messageToSend){
        for(ClientHandler client: clientHandlers){
            try{
                if(!client.clientUserName.equals(clientUserName)){
                    client.bufferedWriter.write(messageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

public void removeClient(){
        clientHandlers.remove(this);
        broadCastMessage(clientUserName + " has left the chat");
}

public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClient();
        try{
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
}


}
