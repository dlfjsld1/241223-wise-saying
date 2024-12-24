import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

class App {
    Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Quote> dic = new ArrayList<>();
    private int lastId = 0;

    public void run() {
        //테스트 명언 데이터1
        Quote quoteTestOne = new Quote(++lastId, "꿈을 지녀라. 그러면 어려운 현실을 이길 수 있다.", "월트 디즈니");
        Quote quoteTestTwo = new Quote(++lastId, "꿈을 지니지마라. 그러면 어려운 현실을 이길 수 없다.", "반전된 월트 디즈니");
        dic.add(quoteTestOne);
        dic.add(quoteTestTwo);
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
        this.lastId++;
    }

    //등록
    public void register() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();
        addLastNo();
        Quote quote = new Quote(lastId, content, author);
        dic.add(quote);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));
    }

    //파일저장
//    private void saveQuoteFile(Quote quote) {
//        try {
//          Path path = Paths.get("db/wiseSaying", quote.getId() + ".json");
//          Files.createDirectories(path.getParent());
//          String json = gson.toJson(quote);
//          Files.writeString(path, json);
//        } catch(Exception e) {
//            System.out.println("명언 저장이 실패했습니다: %s".formatted(e));
//        }
//    }

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
                    System.out.println("명언(기존) : %s".formatted(qNow.getContent()));
                    System.out.print("명언 : ");
                    String newSaying = scanner.nextLine();
                    qNow.setContent(newSaying);
                    System.out.println("작가(기존) : %s".formatted(qNow.getAuthor()));
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
        boolean isFound = false;
        try {
            int id = Integer.parseInt(strId);
            for(int i = dic.size() -1; i >= 0; i--) {
                if (dic.get(i).getId() == id) {
                    isFound = true;
                    dic.remove(i);
                    System.out.println(id + "번 명언이 삭제되었습니다.");
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

    //목록
    public void list() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for(int i = dic.size() - 1; i >= 0; i--) {
            String line = "%d / %s / %s".formatted(dic.get(i).getId(), dic.get(i).getAuthor(), dic.get(i).getContent());
            System.out.println(line);
        }
    }
}

class Quote {
    private int id;
    private String content;
    private String author;

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
