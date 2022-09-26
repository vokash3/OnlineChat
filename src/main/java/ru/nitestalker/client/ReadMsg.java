package ru.nitestalker.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadMsg extends Thread {

    private static final Logger LOG = LogManager.getLogger(ReadMsg.class);

    private BufferedReader in;


    public ReadMsg(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {

        String msg;

        while (true) {
            try {
                msg = in.readLine();
                System.out.println(msg);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
