import org.jsoup.Jsoup;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var TARGET_URL = "https://faunistics.com/sfinksy/";
        var USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:106.0) Gecko/20100101 Firefox/106.0";
        var outputPath = "C:\\Users\\79525\\IdeaProjects\\workWithJsoup\\src\\main\\resources";
        Scanner in = new Scanner(System.in);
        try {
            var document = Jsoup.connect(TARGET_URL)
                    .userAgent(USER_AGENT)
                    .referrer("https://www.google.com")
                    .get();
            var elements = document.select("img");
            System.out.println("Введите формат изображений, которые вы хотите скачать с сайта: (например -> .jpg)");
            var format = in.nextLine();
            for (var el : elements) {
                var src = el.absUrl("src");
                if (src.substring(src.lastIndexOf(".")).equals(format)) {
                    System.out.println("Ссылка на изображение: " + src);
                    loadImages(src, outputPath);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadImages(String src, String outputPath) {
        try {
            var indexOfName = src.lastIndexOf("/");
            var name = src.substring(indexOfName);
            System.out.printf("Имя файла: %s\n", name);
            var url = new URL(src);
            var in = url.openStream();
            var out = new BufferedOutputStream(new FileOutputStream(outputPath + name));
            for (int i; (i = in.read()) != -1; ) {
                out.write(i);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
