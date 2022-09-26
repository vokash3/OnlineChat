package ru.nitestalker.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nitestalker.utils.Helper;
import ru.nitestalker.utils.NetworkUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static Socket clientSocket; // Общение
    private static ServerSocket server; // сам сервер
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static final Logger LOG = LogManager.getLogger(ChatServer.class);

    public static void main(String[] args) {

        try {
            try {
                final int port = getPort(args);
                LOG.info("Запуск чат-сервера на порту {}", port);
                server = new ServerSocket(port);
                LOG.info("Сервер запущен: " +
                                "\nЛокальный IP: " + server.getInetAddress() + "\n" +
                        "\nПорт: " + server.getLocalPort() + "\n" +
                                "\nПубличный IP: " + NetworkUtils.getPublicIp()); // TODO - внешний ip
                clientSocket = server.accept(); // ждёт подключений клиентов
                // после установки связи с клиентом - создание потоков io для общения
                LOG.debug("Подключился клиент: " + clientSocket.toString());
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // От клиента
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); // Клиенту

                    String clientMessage = in.readLine();
                    System.out.println(clientMessage);

                    out.write("TEST! Сервером принято сообщение от клиента " + clientSocket.toString() + ": " + clientMessage);
                    out.flush(); // Чистка буфера
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }
            } finally {
                server.close();
                LOG.info("Сервер закрыт!");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

    }

    private static int getPort(String... args) {
        if (args.length == 0)
            return Helper.PORT_DEFAULT;
        return Integer.parseInt(args[0]);
    }
}
