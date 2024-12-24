import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class App {
    Scanner scanner = new Scanner(System.in);
    Path lastIdPath = Path.of("db/wiseSaying", "lastId.txt");

    public void run() {
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
            } else if(command.equals("빌드")) {
                build();
            } else if(command.equals("초기화")) {
                reset();
            }
        }
    }

    //마지막에 추가한 번호 추가
    public void addLastId() {
        try {
        int addId = getLastId() + 1;
            Files.writeString(lastIdPath, Integer.toString(addId));
        } catch(Exception e) {
            System.out.println("오류가 발생했습니다. %s".formatted(e));
        }
    }

    //마지막에 추가한 번호 조회
    public int getLastId() throws Exception {
        //db/wiseSaying/lastId.txt가 있는지 확인
        if  (!Files.exists(lastIdPath)) {
            //없으면 새로 생성해 리턴
            try {
                Files.createDirectories(lastIdPath.getParent());
                Files.writeString(lastIdPath, "0");
                return 0;
            } catch (Exception e) {
                System.out.println("오류가 발생했습니다. %s".formatted(e));
                throw new Exception(e);
            }
        } else {
            //있으면 읽어서 리턴
            try {
                String lastNoStr = Files.readString(lastIdPath);
                return Integer.parseInt(lastNoStr);
            } catch(Exception e) {
                System.out.println("오류가 발생했습니다. %s".formatted(e));
                throw new Exception(e);
            }
        }
    }

    //등록
    public void register() {
        try {
            System.out.print("명언 : ");
            String content = scanner.nextLine();
            System.out.print("작가 : ");
            String author = scanner.nextLine();
            addLastId();
            int lastId = getLastId();
            Quote quote = new Quote(lastId, content, author);
            saveQuoteFile(quote);
            System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));
        } catch(Exception e) {
            System.out.println("명언 등록에 실패했습니다: %s".formatted(e));
        }
    }

    //파일저장
    private void saveQuoteFile(Quote quote) {
        try {
            Path path = Path.of("db/wiseSaying", quote.getId() + ".json");
            Files.createDirectories(path.getParent());
            Gson gson = new Gson();
            String json = gson.toJson(quote);
            Files.writeString(path, json);
        } catch(Exception e) {
            System.out.println("명언 저장이 실패했습니다: %s".formatted(e));
        }
    }

    //수정
    public void revise(String command) {
        String strId = command.replace("수정?id=", "");
        Gson gson = new Gson();
        try {
            int id = Integer.parseInt(strId);
            boolean isFound = false;
            Path filePath = Path.of("db/wiseSaying", "%d.json".formatted(id));
            if(Files.exists(filePath)) {
                isFound = true;
                String JsonStr = Files.readString(filePath);
                Quote quoteNow = gson.fromJson(JsonStr, Quote.class);
                System.out.println("명언(기존) : %s".formatted(quoteNow.getContent()));
                System.out.print("명언 : ");
                String newSaying = scanner.nextLine();
                quoteNow.setContent(newSaying);
                System.out.println("작가(기존) : %s".formatted(quoteNow.getAuthor()));
                System.out.print("작가 : ");
                String newAuthor = scanner.nextLine();
                quoteNow.setAuthor(newAuthor);

                //수정된 내용을 다시 파일러 작성해 덮어쓰기.
                String updatedQuote = gson.toJson(quoteNow);
                Files.writeString(filePath, updatedQuote);
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
            Path filePath = Paths.get("db/wiseSaying", "%d.json".formatted(id));

            //파일이 존재하면 삭제
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println(id + "번 명언이 삭제되었습니다.");
            } else {
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
        //파일 경로 지정
        Path path = Path.of("db/wiseSaying");
        //디렉토리 내 파일 읽기
        Gson gson = new Gson();
        List<Quote> quotes = new ArrayList<>();
        try {
            Files.list(path)
                    .filter(eachPath -> eachPath.toString().endsWith(".json"))
                    .forEach(eachPath -> {
                       try {
                           //JSON 파일 내용을 읽고 객체로 변환
                           String jsonQuote = Files.readString(eachPath);
                           //JSON 파일 읽고 객체화
                           Quote quote = gson.fromJson(jsonQuote, Quote.class);
                           quotes.add(quote);
                       } catch(Exception e) {
                           System.out.println("오류가 발생했습니다. %s".formatted(e));
                       }
                    });

        } catch(Exception e) {
            System.out.println("오류가 발생했습니다. %s".formatted(e));
        }
        //목록 출력
        for(int i = quotes.size() - 1; i >= 0; i--) {
            String line = "%d / %s / %s".formatted(quotes.get(i).getId(), quotes.get(i).getAuthor(), quotes.get(i).getContent());
            System.out.println(line);
        }
    }

    //빌드
    public void build() {
        List<Quote> dataList = new ArrayList<>();
        Path jsonPaths =Path.of("db/wiseSaying");
        Path dataPath = Path.of("db/wiseSaying/data.json");
        Gson gson = new Gson();
        if(!Files.exists(jsonPaths)) {
            System.out.println("명언이 존재하지 않습니다.");
            return;
        }
        try {
           Files.list(jsonPaths)
                   .filter(path -> path.toString().endsWith(".json") && !path.endsWith("data.json"))
                   .forEach(eachPath -> {
                       try {
                            String jsonString = Files.readString(eachPath);
                            Quote quote = gson.fromJson(jsonString, Quote.class);
                            dataList.add(quote);
                       } catch(Exception e) {
                           System.out.println("오류가 발생했습니다: %s".formatted(e));
                       }
                   });
           dataList.sort(Comparator.comparingInt(Quote::getId));
           String dataJson = gson.toJson(dataList);
           Files.writeString(dataPath, dataJson);
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch(Exception e) {
            System.out.println("오류가 발생했습니다: %s".formatted(e));
        }
    }

    //초기화
    public void reset() {
        Path path = Path.of("db/wiseSaying");
        try(Stream<Path> files = Files.list(path)) {
            files.forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    System.err.println("파일 삭제 실패: " + file + ": " + e);
                }
            });
            System.out.println("초기화 완료");
        } catch(Exception e) {
            System.err.println("파일 삭제 실패: " + e);
        }
    }
}

