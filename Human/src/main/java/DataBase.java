import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "nErRSqHkwBFVE5u";

    public static void main(String[] args) {
        var connection = createDatabase(url, username, password);
        fillDatabaseFromCSV(connection, "C:\\Users\\79525\\IdeaProjects\\Human\\src\\main\\java\\Люди.csv");
        var loadFromBD = readDatabase(connection);
        loadFromBD.forEach(System.out::println);
    }

    public static Connection createDatabase(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Соединение установлено...");
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS newSchema").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS newSchema.People (id SERIAL PRIMARY KEY, typeOfPerson VARCHAR(30) NOT NULL ," +
                    "name VARCHAR(30) NOT NULL, surname VARCHAR(30) NOT NULL, yearOfBirth INT NOT NULL, universityOrCompany VARCHAR(30) NOT NULL," +
                    "courseOrWorkExperience INT NOT NULL)").execute();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public static void fillDatabaseFromCSV(Connection connection, String path) {
        try {
            var csv = new CSV();
            csv.readCSV(path);
            var people = csv.listOfPeople;
            for (int i = 0; i < people.size(); i++) {
                try {
                    if (people.get(i).getClass() == Student.class) {
                        connection.prepareStatement(String.format("INSERT INTO newSchema.People VALUES (%d, '%s', '%s', '%s', %d, '%s', %d)", i + 1,
                                "Студент", people.get(i).getName(), people.get(i).getSurname(), people.get(i).getYearOfBirth(), ((Student) people.get(i)).getUniversity(),
                                ((Student) people.get(i)).getCourse())).execute();
                    } else {
                        connection.prepareStatement(String.format("INSERT INTO newSchema.People VALUES (%d, '%s', '%s', '%s', %d, '%s', %d)", i + 1,
                                "Рабочий", people.get(i).getName(), people.get(i).getSurname(), people.get(i).getYearOfBirth(), ((Worker) people.get(i)).getCompany(),
                                ((Worker) people.get(i)).getExperience())).execute();
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
            var set = statement.executeQuery("SELECT typeOfPerson, name, surname, yearOfBirth, universityOrCompany, courseOrWorkExperience FROM newSchema.People");
            var loadFromBD = new ArrayList<Human>();
            while (set.next()) {
                if (set.getString("typeOfPerson").equals("Студент")) {
                    loadFromBD.add(new Student(set.getString("name"), set.getString("surname"), Integer.parseInt(set.getString("yearOfBirth")),
                            set.getString("universityOrCompany"), Integer.parseInt(set.getString("courseOrWorkExperience"))));
                } else {
                    loadFromBD.add(new Worker(set.getString("name"), set.getString("surname"), Integer.parseInt(set.getString("yearOfBirth")),
                            set.getString("universityOrCompany"), Integer.parseInt(set.getString("courseOrWorkExperience"))));
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
