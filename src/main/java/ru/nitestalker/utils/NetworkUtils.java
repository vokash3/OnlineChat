package ru.nitestalker.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nitestalker.server.ChatServer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkUtils {

    private static final Logger LOG = LogManager.getLogger(NetworkUtils.class);

    public static String getPublicIp() {
        StringBuilder ipSB = new StringBuilder();
        try {
            URL url = new URL(Helper.URL_ETH0);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream stream = connection.getInputStream()) {
                    new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                            .lines()
                            .forEach(ipSB::append);
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return ipSB.toString();
    }
}
