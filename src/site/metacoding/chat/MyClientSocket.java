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
            // 스캐너 달기
            sc = new Scanner(System.in);
            // 키보드로부터 입력 받는 부분
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    while (true) {
                        String data = sc.nextLine();
                        writer.write(data + "\n");
                        System.out.println(data);

                        if (data.equals("stop")) {
                            break;
                        }
                        writer.flush(); // 버퍼에 다 안 차서 내려줌
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String inputData = reader.readLine();
                System.out.println("받은 메시지" + inputData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
