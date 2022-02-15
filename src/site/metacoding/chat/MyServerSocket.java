package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    ServerSocket serverSocket; // 시스템 콜, 리스너(연결 = 세션)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    public MyServerSocket() {
        try {
            // 1. 서버 소켓 생성(리스너)
            // 잘 알려진 포트 : 0~1023
            serverSocket = new ServerSocket(1077); // 소켓 객체 생성
            System.out.println("서버 소켓이 1077번으로 생성됨");

            socket = serverSocket.accept(); // while 돌면서 대기(랜덤 포트), 데몬, while 돌면서 대기
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 읽는 버퍼 달았음
            while (true) {
                String inputData = reader.readLine(); // 데이터 읽기
                System.out.println("받은 메시지" + inputData);
                System.out.println("클라이언트 연결됨");

                if (inputData.equals("stop")) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("통신 오류 발생" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { // 메인 스레드가 데몬이 됨
        new MyServerSocket();
        System.out.println("메인 종료");
    }
}
