package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyServerSocket {

    // 리스너 (연결 받기) - 메인 스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트;
    // 서버는 메시지를 받아서 보내기(클라이언트 수마다 스레드 수 달라짐)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000);
            // while 돌리기, 연결 계속 받아야 하기 때문
            고객리스트 = new Vector<>(); // 동기화 처리

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

    class 고객전담스레드 implements Runnable { // 모든 정보는 이 클래스가 들고 있음

        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin = true;

        // 내부에 버퍼를 가져야 하기 때문에 내부 클래스 생성
        public 고객전담스레드(Socket socket) {
            this.socket = socket;
            try {
                // socket new 될 때마다 만들어짐
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // 여기에다 와일 달면 메인 스레드가 갇힘

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                String inputData = reader.readLine(); // 읽기 대기하다가 연결 해제하면 스트림 연결 해제됨
                while (isLogin) {
                    System.out.println("form 클라이언트 : " + inputData);

                    // 메시지 받았으니까 고객 리스트에 담긴 소켓으로 모든 클라이언트에게 메시지를 for문으로 전송
                    for (고객전담스레드 t : 고객리스트) { // 왼쪽 : 컬렉션 타입, 오른쪽 : 컬렉션
                        // for 돌 때 t에 리스트가 담김
                        t.writer.write(inputData + "\n");
                        t.writer.flush();
                        // 고객리스트.get(i).writer.write(inputData + "\n");
                    }
                }

            } catch (Exception e) {
                System.out.println("통신 실패 : " + e.getMessage());
                try {
                    isLogin = false; // while 빠져나감
                    고객리스트.remove(this); // 오류가 난 리스트를 날리기
                    reader.close(); // 부하를 줄이기 위한 바로 삭제
                    writer.close();
                    socket.close();
                } catch (Exception e1) {
                    System.out.println("연결해제 실패 :" + e1.getMessage());
                }
            }

        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}
