package Lab_7;

import java.io. *;
import java.net. *;
import java.util.LinkedList;

public class Crawler {
    private LinkedList<URLDepthPair> done;
    private LinkedList<URLDepthPair> doing;
    private URLDepthPair start_pair;

    private final String URL_INDICATOR = "a href=\"";
    private final String END_URL = "\"";

    public Crawler(String url, int depth){
        start_pair = new URLDepthPair(url, depth);
        done = new LinkedList<>();
        doing = new LinkedList<>();
        doing.add(start_pair);
    }

    public LinkedList<URLDepthPair> getSites(){
        return done;
    }

    // Делаем GET запрос, получаем страницу
    public InputStream make_get_access(URLDepthPair pair){
        Socket socket;
        try{
            socket = new Socket(pair.getHost(), 80);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        try{
            socket.setSoTimeout(3000);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        String path = pair.getPath();
        String host = pair.getHost();
        OutputStream outputStream;
        try{
            outputStream = socket.getOutputStream();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        printWriter.println("GET " + path + " HTTP/1.1");
        printWriter.println("Host: " + host);
        printWriter.println("Connection: close");
        printWriter.println();

        InputStream inputStream;
        try{
            inputStream = socket.getInputStream();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return inputStream;
    }

    // Ищет все вложенные ссылки на странице
    public void search(){
        while (!doing.isEmpty()){
            InputStream inputStream = make_get_access(doing.get(0));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String newUrl = "";
            while (true){
                String line;
                try{
                    line = bufferedReader.readLine();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    break;
                }
                if (line.equals("")) break;
                int startIndex = 0;
                int beginIndex = 0;
                int endIndex = 0;
                while (true){
                    startIndex = line.indexOf(URL_INDICATOR, startIndex);
                    if (startIndex == -1) {
                        break;
                    }
                    startIndex += URL_INDICATOR.length();
                    beginIndex = startIndex;
                    endIndex = line.indexOf(END_URL, startIndex);
                    startIndex = endIndex;
                    newUrl = line.substring(beginIndex, endIndex);
                }
            }
            if (!newUrl.equals("")){
                URLDepthPair pair = new URLDepthPair(newUrl, doing.get(0).getDepth()+1);
                done.add(doing.get(0));
                doing.add(pair);
            }
            done.add(doing.get(0));
            doing.remove(0);
        }
    }


    public static void main(String[] args){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] mas = null;
        try{
            mas = bufferedReader.readLine().split(" ");
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        int depth = 0;
        String url;
        if (mas.length != 2) {
            System.out.println("usage: java Crawler <URL><depth>");
            System.out.println(mas[0]);
            System.exit(1);
        } else {
            try {
                depth = Integer.parseInt(mas[1]);
            } catch (NumberFormatException e) {
                System.out.println("usage: java Crawler <URL><depth>");
                System.exit(1);
            }
            url = mas[0];
            Crawler crawler = new Crawler(url, depth);
            crawler.search();
            System.out.println(crawler.getSites());
        }
    }

}
