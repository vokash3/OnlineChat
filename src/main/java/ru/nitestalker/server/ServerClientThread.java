package ru.nitestalker.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ServerClientThread extends Thread{

    private Socket socket; // сокет взаимодействия с клиентом
    private BufferedReader in;
    private BufferedWriter out;
    private static final Logger LOG = LogManager.getLogger(ServerClientThread.class);

    public ServerClientThread(Socket socket) throws IOException { // Если возникнут исключения они пробросятся на уровень выше
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // От клиента
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Клиенту
        start();
    }

    @Override
    public void run() {
        String clientMessage;
        while (true) {
            try {
                clientMessage = in.readLine();
                if(clientMessage.equals("/exit"))
                    break;
                // Рассылка сообщения всем участникам чата (клиентам сервера)
                for(ServerClientThread chatMembers : ChatServer.clients)
                    chatMembers.send(clientMessage);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {

        }
    }
}
