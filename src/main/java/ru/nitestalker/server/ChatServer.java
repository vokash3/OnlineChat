package ru.nitestalker.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nitestalker.utils.Helper;
import ru.nitestalker.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {
    private static Socket clientSocket; // Общение
    private static ServerSocket server; // сам сервер
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    private static final Logger LOG = LogManager.getLogger(ChatServer.class);

    public static final List<ServerClientThread> clients = new LinkedList<>();

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
                while (true) {
                    clientSocket = server.accept(); // ждёт подключений клиентов
                    clients.add(new ServerClientThread(clientSocket)); // Добавляет подключение в список
                    LOG.info("Подключился клиент: " + clientSocket.toString());
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
