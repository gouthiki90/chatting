package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    Scanner sc;
    BufferedReader reader;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077); // IP 주소와 포트
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("안녕\n"); // 메시지 끝이라는 것을 알려줌 \n
            // 버퍼에 담았음
            writer.flush(); // 버퍼에 다 안 차서 내려줌
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
