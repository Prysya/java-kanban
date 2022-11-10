package main;

import main.server.HttpTaskServer;
import main.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }
}
