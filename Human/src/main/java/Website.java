import org.jsoup.Jsoup;
import java.util.ArrayList;

public class Website {
    private static String target_url = "https://urfu.ru/ru/students/study/brs-rating/?tx_urfubrs_top%5Binstitute%5D=4&tx_urfubrs_top%5Byear%5D=2021&tx_urfubrs_top%5Bcourse%5D=2&tx_urfubrs_top%5Bqualification%5D=Бакалавр&tx_urfubrs_top%5Bfamilirizationtype%5D=Очная&tx_urfubrs_top%5Baction%5D=index&tx_urfubrs_top%5Bcontroller%5D=List&cHash=45700bd70e1175c331b7035d93e93fa5";
    private static String user_agent = "Mozilla/5.0 (Windows NT 6.1; rv:106.0) Gecko/20100101 Firefox/106.0";

    public static ArrayList<ArrayList<String>> ParseSite() {
        var result = new ArrayList<ArrayList<String>>();
        try {
            var document = Jsoup.connect(target_url)
                    .userAgent(user_agent)
                    .referrer("https://www.google.com")
                    .get();
            System.out.println("Соединение с сайтом установлено...");
            var elements = document.select("#c2041 > table > tbody > tr > td").textNodes();
            var num = 0;
            for (var elem : elements) {
                try {
                    num = Integer.parseInt(elem.text());
                    if (num != 84) {
                        result.add(new ArrayList<>());
                    } else {
                        result.get(result.size() - 1).add(elem.text());
                    }
                } catch (Exception e) {
                    result.get(result.size() - 1).add(elem.text());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Парсинг закончен.");
        return result;
    }

    public static ArrayList<Human> makePeople(ArrayList<ArrayList<String>> listOfPeople) {
        var result = new ArrayList<Human>();
        for (var person : listOfPeople) {
            result.add(new Student(person.get(0).split(" ")[1], person.get(0).split(" ")[0], (int) ((Math.random() * (2005 - 1998)) + 1998), person.get(1), Double.parseDouble(person.get(4))));
        }
        return result;
    }

    public static ArrayList<Integer> makeStatisticPoint(ArrayList<ArrayList<String>> listOfPeople)
    {
        var result = new ArrayList<Integer>();
        var firstCount = 0;
        var secondCount = 0;
        var thirdCount = 0;
        var fourthCount = 0;
        var fifthCount = 0;
        var value = 0.0;
        for (var person: listOfPeople)
        {
            value = Double.parseDouble(person.get(4));
            if (value > 75.0 && value < 80.0)
            {
                firstCount += 1;
            } else if (value > 80.0 && value < 85.0) {
                secondCount += 1;
            } else if (value > 85.0 && value < 90.0) {
                thirdCount += 1;
            } else if (value > 90.0 && value < 95.0) {
                fourthCount += 1;
            } else if (value > 95.0 && value < 100.0) {
                fifthCount += 1;
            }
        }
        result.add(firstCount);
        result.add(secondCount);
        result.add(thirdCount);
        result.add(fourthCount);
        result.add(fifthCount);
        return result;
    }

    public static void main(String[] args) {
        var information = ParseSite();
        System.out.println(makePeople(information));
    }
}
