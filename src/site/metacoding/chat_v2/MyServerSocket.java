package site.metacoding.chat_v2;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServerSocket {

    // 리스너 (연결 받기) - 메인 스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;
    // 서버는 메시지를 받아서 보내기(클라이언트 수마다 스레드 수 달라짐)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            // while 돌리기, 연결 계속 받아야 하기 때문
            고객리스트 = new ArrayList<>();

            while (true) { // 소켓이 사라지기 때문에 컬렉션에 담아야 함
                Socket socket = serverSocket.accept(); // main 스레드
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket); // heap에 들어감
                고객리스트.add(t); // 소켓 담기
                System.out.println("고객리스트 크기 : " + 고객리스트.size());
                new Thread(t).start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class 고객전담스레드 implements Runnable {

        Socket socket;

        // 내부에 버퍼를 가져야 하기 때문에 내부 클래스 생성
        public 고객전담스레드(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
