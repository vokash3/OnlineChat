package ru.nitestalker.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nitestalker.server.ChatServer;
import ru.nitestalker.utils.Helper;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // Ввод
    private static BufferedReader in; // поток чтения из clientSocket
    private static BufferedWriter out; // поток записи в сокет
    private static final Logger LOG = LogManager.getLogger(ChatClient.class);

    public static void main(String[] args) {
        String[] checkedArgs = getAddressAndPort(args);
        String nickname;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                final String ip = checkedArgs[0];
                final int port = Integer.parseInt(checkedArgs[1]);
                clientSocket = new Socket(ip, port);
                LOG.info("Соединение с сервером {}:{} установлено", ip, port);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                while (true) {
                    System.out.print("Введите ваш никнейм: ");
                    nickname = reader.readLine();
                    if (nickname.trim().isEmpty())
                        continue;
                    else break;
                }
                ReadMsg fromServerThread = new ReadMsg(in); // Поток чтения сообщений с сервера
                WriteMsg toServerThread = new WriteMsg(out, nickname); // Поток отправки сообщений на сервер

                fromServerThread.start();
                toServerThread.start();
                fromServerThread.join();
                toServerThread.join();

            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            } finally {
                clientSocket.close();
                in.close();
                out.close();
                LOG.debug("Соединение с сервером и потоки I/O закрыты ...");
            }
        } catch (IOException e) {
            LOG.error("Ошибка: " + e.getMessage());
        }
    }

    private static String[] getAddressAndPort(String... args) {
        if (args.length == 0)
            return new String[]{Helper.IP_ADDRESS_DEFAULT, String.valueOf(Helper.PORT_DEFAULT)};
        else if (args.length == 2)
            return args;
        else {
            LOG.error("Ошибка в указании аргументов! Формат: IP_SERVER PORT");
            System.exit(1);
        }
        return args;
    }
}
