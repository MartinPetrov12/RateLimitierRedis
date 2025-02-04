package com;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Service
public class RequestSender {
    public RequestSender() {
    }

    public void spamRequests() throws InterruptedException, IOException {
        Random rand = new Random();
        while(true) {
            int sleepTimer = rand.nextInt(400, 1000);
            Thread.sleep(rand.nextInt(400,1000));
            System.out.println("ok");
            System.out.println(sleepTimer);
            URL url = new URL("http://localhost:8081/sendRequest");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: Too many requests. Try again later :)");
            }
        }
    }
}
