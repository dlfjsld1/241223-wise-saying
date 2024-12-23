import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    public void run() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("== 명언 앱 ==");

        System.out.print("명언 : ");
        String saying = scanner.nextLine(); // 입력값 가져옴. 입력값이 없으면 기다림.
        System.out.print("작가 : ");
        String author = scanner.nextLine(); // 입력값 가져옴. 입력값이 없으면 기다림.
        System.out.println("1번 명언이 등룍되었습니다.");
        System.out.println("명령) 종료");
    }
}