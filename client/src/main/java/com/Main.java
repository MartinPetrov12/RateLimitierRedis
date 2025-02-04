package com;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        RequestSender requestSender = new RequestSender();
        requestSender.spamRequests();
    }
}