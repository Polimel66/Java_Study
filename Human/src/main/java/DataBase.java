import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "nErRSqHkwBFVE5u";

    public static void main(String[] args) {
        var connection = createDatabase(url, username, password);
        //fillDatabaseFromCSV(connection, "C:\\Users\\79525\\Documents\\GitHub\\Java_Study\\Human\\ПробаЛюди111.csv");
//        var website = new Website();
        fillDatabaseFromWebsite(connection, Website.MakePeople(Website.ParseSite()));
        var loadFromBD = readDatabase(connection);
        var csv = new CSV();
        csv.makeCSVFromList("ФайлИзБДиСайта", loadFromBD);
        loadFromBD.forEach(System.out::println);
    }

    public static Connection createDatabase(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Соединение установлено...");
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS newSchema").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS newSchema.People (id SERIAL PRIMARY KEY, typeOfPerson VARCHAR(30) NOT NULL ," +
                    "name VARCHAR(30) NOT NULL, surname VARCHAR(30) NOT NULL, yearOfBirth INT NOT NULL, academicGroupOrCompany VARCHAR(30) NOT NULL," +
                    "numberOfPointsOrSalary VARCHAR(30) NOT NULL)").execute();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static void fillDatabaseFromWebsite(Connection connection, ArrayList<Human> people)
    {
        try {
            for (int i = 0; i < people.size(); i++) {
                connection.prepareStatement(String.format("INSERT INTO newSchema.People VALUES (%d, '%s', '%s', '%s', %d, '%s', %s)", i + 1,
                        "Студент", people.get(i).getName(), people.get(i).getSurname(), people.get(i).getYearOfBirth(), ((Student) people.get(i)).getAcademicGroup(),
                        Double.toString(((Student) people.get(i)).getNumberOfPoints()))).execute();
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException();
        }
        System.out.println("База данных заполнена.");
    }
    public static void fillDatabaseFromCSV(Connection connection, String path) {
        try {
            var csv = new CSV();
            csv.readCSV(path);
            var people = csv.listOfPeople;
            for (int i = 0; i < people.size(); i++) {
                try {
                    if (people.get(i).getClass() == Student.class) {
                        connection.prepareStatement(String.format("INSERT INTO newSchema.People VALUES (%d, '%s', '%s', '%s', %d, '%s', %s)", i + 1,
                                "Студент", people.get(i).getName(), people.get(i).getSurname(), people.get(i).getYearOfBirth(), ((Student) people.get(i)).getAcademicGroup(),
                                Double.toString(((Student) people.get(i)).getNumberOfPoints()))).execute();
                    } else {
                        connection.prepareStatement(String.format("INSERT INTO newSchema.People VALUES (%d, '%s', '%s', '%s', %d, '%s', %s)", i + 1,
                                "Рабочий", people.get(i).getName(), people.get(i).getSurname(), people.get(i).getYearOfBirth(), ((Worker) people.get(i)).getCompany(),
                                Double.toString(((Worker) people.get(i)).getSalary()))).execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        System.out.println("База данных заполнена.");
    }

    public static ArrayList<Human> readDatabase(Connection connection) {
        try {
            var statement = connection.createStatement();
            var set = statement.executeQuery("SELECT typeOfPerson, name, surname, yearOfBirth, academicGroupOrCompany, numberOfPointsOrSalary FROM newSchema.People");
            var loadFromBD = new ArrayList<Human>();
            while (set.next()) {
                if (set.getString("typeOfPerson").equals("Студент")) {
                    loadFromBD.add(new Student(set.getString("name"), set.getString("surname"), Integer.parseInt(set.getString("yearOfBirth")),
                            set.getString("academicGroupOrCompany"), Double.parseDouble(set.getString("numberOfPointsOrSalary"))));
                } else {
                    loadFromBD.add(new Worker(set.getString("name"), set.getString("surname"), Integer.parseInt(set.getString("yearOfBirth")),
                            set.getString("academicGroupOrCompany"), Double.parseDouble(set.getString("numberOfPointsOrSalary"))));
                }
            }
            System.out.println("База данных считана.");
            return loadFromBD;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
