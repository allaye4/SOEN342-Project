package Implementation;

public class Client {
    String name;
    int age;
    boolean isUnderage;
    Client guardian;

    public Client(String name, int age) {
        this.name = name;
        this.age = age;
        this.isUnderage = age < 18;
    }
}