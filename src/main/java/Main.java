import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class Quote {
    int id;
    String saying;
    String author;

    public Quote(int id, String saying, String author) {
        this.id = id;
        this.saying = saying;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getSaying() {
        return saying;
    }

    public String getAuthor() {
        return author;
    }
}

class App {
    private static ArrayList<Quote> dic = new ArrayList<>();

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== 명언 앱 ==");
        int lastNo = 0;

        while(true) {
            System.out.print("명령) ");
            String command = scanner.nextLine(); // 입력값 가져옴. 입력값이 없으면 기다림.
            if(command.equals("종료")) {
                System.out.println("명언 앱을 종료합니다.");
                break;
            } else if (command.equals("등록")) {
                System.out.print("명언 : ");
                String saying = scanner.nextLine();
                System.out.print("작가 : ");
                String author = scanner.nextLine();
                Quote quote = new Quote(++lastNo, saying, author);
                dic.add(quote);
                System.out.println(lastNo + "번 명언이 등록되었습니다.");
            } else if (command.equals("목록")) {
                list();
            }
        }
    }

    public void list() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for(int i = dic.size() - 1; i >= 0; i--) {
            String line = dic.get(i).getId() + " / " + dic.get(i).getAuthor()  + " / " + dic.get(i).getSaying();
            System.out.println(line);
        }
    }
}

