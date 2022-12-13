import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

public abstract class Human {
    private final String name;
    private final String surname;
    private final int yearOfBirth;

    public Human(String name, String surname, int yearOfBirth) {
        this.name = name;
        this.surname = surname;
        this.yearOfBirth = yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }
}

class Student extends Human {
    private final String academicGroup;
    private final double numberOfPoints;


    public Student(String name, String surname, int yearOfBirth, String academicGroup, double numberOfPoints) {
        super(name, surname, yearOfBirth);
        this.academicGroup = academicGroup;
        this.numberOfPoints = numberOfPoints;
    }

    public String getAcademicGroup() {
        return academicGroup;
    }

    public double getNumberOfPoints() {
        return numberOfPoints;
    }

    public static String findTheMostPopularAcademicGroup(ArrayList<Student> students) {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        for (var student : students) {
            if (dictionary.containsKey(student.getAcademicGroup())) {
                var count = dictionary.get(student.getAcademicGroup()) + 1;
                dictionary.remove(student.getAcademicGroup());
                dictionary.put(student.getAcademicGroup(), count);
            } else {
                dictionary.put(student.getAcademicGroup(), 1);
            }
        }
        var maxCount = 0;
        var popularAcademicGroup = "";
        for (var entry : dictionary.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                popularAcademicGroup = entry.getKey();
            }
        }
        return String.format("Самая популярная академическая группа: %s", popularAcademicGroup);
    }

    public String toString() {
        return String.format("%s %s %s г. - академическая группа: %s, количество баллов: %s", getName(), getSurname(), getYearOfBirth(), getAcademicGroup(), getNumberOfPoints());
    }
}

class Worker extends Human {
    private final String company;
    private final double salary;

    public Worker(String name, String surname, int yearOfBirth, String company, double salary) {
        super(name, surname, yearOfBirth);
        this.company = company;
        this.salary = salary;
    }

    public String getCompany() {
        return company;
    }

    public double getSalary() {
        return salary;
    }

    public static String findTheMostPopularCompany(ArrayList<Worker> workers) {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        for (var worker : workers) {
            if (dictionary.containsKey(worker.getCompany())) {
                var count = dictionary.get(worker.getCompany()) + 1;
                dictionary.remove(worker.getCompany());
                dictionary.put(worker.getCompany(), count);
            } else {
                dictionary.put(worker.getCompany(), 1);
            }
        }
        var maxCount = 0;
        var popularCompany = "";
        for (var entry : dictionary.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                popularCompany = entry.getKey();
            }
        }
        return String.format("Самая популярная среди работников компания: %s", popularCompany);
    }

    public String toString() {
        return String.format("%s %s %s г. - компания: %s, зарплата: %s тыс. р.", getName(), getSurname(), getYearOfBirth(), getCompany(), getSalary());
    }
}

class Generator {
    private final String[] names = new String[]{"Павел", "Сергей", "Андрей", "Леонид", "Антон"};
    private final String[] surnames = new String[]{"Климов", "Глазов", "Толстой", "Руков", "Куров"};
    private final String[] companies = new String[]{"Газпром", "Яндекс", "Контур", "Аэрофлот", "Альфа банк"};
    private ArrayList<Human> listOfHuman;

    public ArrayList<Human> getListOfHuman() {
        return listOfHuman;
    }

    public Generator(int countOfHuman) {
        listOfHuman = new ArrayList<Human>();
        var studentDecimalFormat = new DecimalFormat("#.##");
        var workerDecimalFormat = new DecimalFormat("#.###");
        int isStudentOrWorker = 0;
        for (int i = 1; i < countOfHuman + 1; i++) {
            isStudentOrWorker = (int) (Math.random() * ((1 - 0) + 1)); // 0 - ученик, 1 - учитель
            var indexes = new int[3];
            for (int j = 0; j < 3; j++)
                indexes[j] = (int) (Math.random() * (4 - 0)) + 1;
            if (isStudentOrWorker == 0)
                listOfHuman.add(new Student(names[indexes[0]], surnames[indexes[1]], (int) (Math.random() * (2005 - 1998)) + 1998, "РИ-" + String.valueOf((int) (Math.random() * (499999 - 100000)) + 100000), Double.parseDouble(studentDecimalFormat.format((Math.random() * (100.00 - 1.00)) + 1.00).replace(',', '.'))));
            else {
                listOfHuman.add(new Worker(names[indexes[0]], surnames[indexes[1]], (int) (Math.random() * (2000 - 1970)) + 1970, companies[indexes[2]], Double.parseDouble(workerDecimalFormat.format((Math.random() * (100.000 - 10.000)) + 10.000).replace(',', '.'))));
            }
        }
    }

    public void printHumans() {
        for (var human : listOfHuman)
            System.out.println(human.toString());
    }
}

class Handler {
    public static void makeSortByDateOfBirth(ArrayList<Human> humans) {
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        for (var human : humans) {
            if (dictionary.containsKey(human.getYearOfBirth())) {
                var hums = String.format("%s, %s %s (%s г.)", dictionary.get(human.getYearOfBirth()), human.getName(), human.getSurname(), human.getYearOfBirth());
                dictionary.remove(human.getYearOfBirth());
                dictionary.put(human.getYearOfBirth(), hums);
            } else {
                dictionary.put(human.getYearOfBirth(), String.format("%s %s (%s г.), ", human.getName(), human.getSurname(), human.getYearOfBirth()));
            }
        }
        var years = dictionary.keySet();
        var yearsList = new ArrayList<Integer>(years);
        Collections.sort(yearsList);
        System.out.print("Отсортированные люди по году рождения: ");
        for (var year : yearsList) {
            System.out.print(dictionary.get(year));
        }
    }
}

class CSV {
    ArrayList<Human> listOfPeople = new ArrayList<>();

    public void readCSV(String path) {
        var file = new File(path);
        try {
            var lines = Files.readAllLines(file.toPath());
            for (var i = 1; i <= lines.size() - 1; i++) {
                var line = lines.get(i).split(";");
                if (line[0].equals("Студент")) {
                    listOfPeople.add(new Student(line[1], line[2], Integer.parseInt(line[3]), line[4], Double.parseDouble(line[5])));
                } else {
                    listOfPeople.add(new Worker(line[1], line[2], Integer.parseInt(line[3]), line[4], Double.parseDouble(line[5])));
                }
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException();
        }
    }

    public void makeRandomCSV(String nameCSV, int countOfHuman) throws FileNotFoundException {
        var generator = new Generator(countOfHuman);
        var csvList = new ArrayList<String[]>();
        csvList.add(new String[]{"Тип человека", "Имя", "Фамилия", "Год рождения", "Академическая группа/Компания", "Количество баллов/Зарплата"});
        for (var hum : generator.getListOfHuman()) {
            if (hum.getClass() == Student.class) {
                csvList.add(new String[]{"Студент", hum.getName(), hum.getSurname(), Integer.toString(hum.getYearOfBirth()),
                        ((Student) hum).getAcademicGroup(), Double.toString(((Student) hum).getNumberOfPoints())});
            } else {
                csvList.add(new String[]{"Рабочий", hum.getName(), hum.getSurname(), Integer.toString(hum.getYearOfBirth()),
                        ((Worker) hum).getCompany(), Double.toString(((Worker) hum).getSalary())});
            }
        }
        var newFileCSV = new File(nameCSV);
        try (PrintWriter pw = new PrintWriter(newFileCSV)) {
            csvList.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }

    public void makeCSVFromList(String nameCSV, ArrayList<Human> people){
        var csvList = new ArrayList<String[]>();
        csvList.add(new String[]{"Тип человека", "Имя", "Фамилия", "Год рождения", "Академическая группа/Компания", "Количество баллов/Зарплата"});
        for (var hum : people) {
            if (hum.getClass() == Student.class) {
                csvList.add(new String[]{"Студент", hum.getName(), hum.getSurname(), Integer.toString(hum.getYearOfBirth()),
                        ((Student) hum).getAcademicGroup(), Double.toString(((Student) hum).getNumberOfPoints())});
            } else {
                csvList.add(new String[]{"Рабочий", hum.getName(), hum.getSurname(), Integer.toString(hum.getYearOfBirth()),
                        ((Worker) hum).getCompany(), Double.toString(((Worker) hum).getSalary())});
            }
        }
        var newFileCSV = new File(nameCSV);
        try (PrintWriter pw = new PrintWriter(newFileCSV)) {
            csvList.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String convertToCSV(String[] data) {
        return String.join(";", data);
    }

    public void printHumans() {
        for (var human : listOfPeople)
            System.out.println(human.toString());
    }
}

class console {
    public static void main(String[] args) throws FileNotFoundException {
        var b = new CSV();
        b.makeRandomCSV("ПробаЛюди111.csv", 5);
        b.readCSV("C:\\Users\\79525\\Documents\\GitHub\\Java_Study\\Human\\ПробаЛюди111.csv");
        b.printHumans();

    }
}




