package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    // 스레드
    BufferedWriter writer;
    // 스레드
    BufferedReader reader;
    // 키보드에 받아서 쓸 스레드
    Scanner sc;

    public MyClientSocket() {

        String username;

        try {
            socket = new Socket("localhost", 2000);

            // 객체 생성
            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 새로운 스레드는 읽기 전용
            new Thread(new 읽기전담스레드()).start();

            // 메인 스레드가 쓰기 전용, 새로운 스레드가 먼저. 와일 돌아야 되기 때문
            // 최초 username 전송 프로토콜
            System.out.println("아이디를 입력하세요."); // 최초에 보낸 메시지는 아이디가 되도록 프로토콜 생성
            username = sc.nextLine(); // 아이디를 유저네임으로 받기

            while (true) {
                String keyboardInputData = sc.nextLine();
                writer.write(keyboardInputData + "\n");
                writer.flush(); // 버퍼에 담긴 것을 스트림으로 흘려보내기, 플러쉬 때 통신 시작
                System.out.println(username + "이 서버로 전송되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class 읽기전담스레드 implements Runnable { // 내부 클래스로 하면 전역 변수 쓸 수 있음

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메시지 : " + inputData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
