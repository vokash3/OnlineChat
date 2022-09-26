package ru.nitestalker.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WriteMsg extends Thread {

    private static final Logger LOG = LogManager.getLogger(WriteMsg.class);
    private BufferedReader reader;
    private BufferedWriter out;
    private String nickname;

    public WriteMsg(BufferedWriter out, String nickname) {
        this.out = out;
        this.nickname = nickname;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        while (true) {
            try {
//                System.out.print("Ввод >>> ");
                String message = reader.readLine();
                if (message.trim().isEmpty())
                    continue;
                if (message.equals("/exit")) {
                    out.write(message);
                    out.flush();
                    break;
                }
                out.write(nickname + ": " + message + "\n");
                out.flush();
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }

        }
    }
}
