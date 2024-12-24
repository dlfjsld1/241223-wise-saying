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
    String content;
    String author;

    public Quote(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

class App {
    private static ArrayList<Quote> dic = new ArrayList<>();
    private int lastNo = 0;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("== 명언 앱 ==");
        while(true) {
            System.out.print("명령) ");
            String command = scanner.nextLine(); // 입력값 가져옴. 입력값이 없으면 기다림.
            if(command.equals("종료")) {
                System.out.println("명언 앱을 종료합니다.");
                break;
            } else if (command.equals("등록")) {
                register();
            } else if (command.equals("목록")) {
                list();
            } else if(command.startsWith("수정")) {
                revise(command);
            } else if(command.startsWith("삭제")) {
                delete(command);
            }
        }
    }

    //마지막에 추가한 번호 추가
    public void addLastNo() {
        this.lastNo++;
    }

    //등록
    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();
        addLastNo();
        Quote quote = new Quote(lastNo, content, author);
        dic.add(quote);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(lastNo));
    }

    //수정
    public void revise(String command) {
        String strId = command.replace("수정?id=", "");
        try {
            int id = Integer.parseInt(strId);
            boolean isFound = false;
            for(int i = dic.size() -1; i >= 0; i--) {
                Quote qNow = dic.get(i);
                if (qNow.getId() == id) {
                    isFound = true;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("명언(기존) : " + qNow.getContent());
                    System.out.print("명언 : ");
                    String newSaying = scanner.nextLine();
                    qNow.setContent(newSaying);
                    System.out.println("작가(기존) : " + qNow.getAuthor());
                    System.out.print("작가 : ");
                    String newAuthor = scanner.nextLine();
                    qNow.setAuthor(newAuthor);
                    break;
                }
            }
            if (!isFound) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            }
        } catch(Exception e) {
            System.out.println("올바르지 않은 id입니다.");
        }
    }

    //삭제
    public void delete(String command) {
        String strId = command.replace("삭제?id=", "");
        try {
            int id = Integer.parseInt(strId);
            for(int i = dic.size() -1; i >= 0; i--) {
                if (dic.get(i).getId() == id) {
                    dic.remove(i);
                    System.out.println(id + "번 명언이 삭제되었습니다.");
                    break;
                }
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            }
        } catch(Exception e) {
            System.out.println("올바르지 않은 id입니다.");
        }
    }

    //목록
    public void list() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for(int i = dic.size() - 1; i >= 0; i--) {
            String line = dic.get(i).getId() + " / " + dic.get(i).getAuthor()  + " / " + dic.get(i).getContent();
            System.out.println(line);
        }
    }
}

