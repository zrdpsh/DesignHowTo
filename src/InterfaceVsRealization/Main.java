package InterfaceVsRealization;

public class Main {
    public static void main(String[] args) {

        Storage ds = new DatabaseStorage();
        ds.save("one");
        ds.save("two");
        ds.save("three");

        String firstString = ds.retrieve(1);
        String secondString = ds.retrieve(2);
        String thirdString = ds.retrieve(3);
    }
}
