import java.util.Scanner;

public class Bobby {
    public static void main(String[] args) {
        String banner = "BBBB   OOO   BBBB  BBBB  Y   Y\n"
                + "B   B O   O  B   B B   B  Y Y\n"
                + "BBBB  O   O  BBBB  BBBB    Y\n"
                + "B   B O   O  B   B B   B   Y\n"
                + "BBBB   OOO   BBBB  BBBB    Y";
        String LINE = "------------------------------------------";

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println(LINE);
        System.out.println("     Hello, I'm Bobby.");
        System.out.println("     What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.trim().equalsIgnoreCase("bye")) {
            System.out.println(LINE);
            System.out.println("     " + input);
            System.out.println(LINE);
            input = scanner.nextLine();
        }

        System.out.println(LINE);
        System.out.println("     Bye! Hope to see you again soon. ");
        System.out.println(LINE);
    }
}
