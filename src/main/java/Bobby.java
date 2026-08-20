import java.util.Scanner;

public class Bobby {
    public static void main(String[] args) {
        String banner = "BBBB   OOO   BBBB  BBBB  Y   Y\n"
                + "B   B O   O  B   B B   B  Y Y\n"
                + "BBBB  O   O  BBBB  BBBB    Y\n"
                + "B   B O   O  B   B B   B   Y\n"
                + "BBBB   OOO   BBBB  BBBB    Y";
        String LINE = "____________________________________________________________";

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println(LINE);
        System.out.println("     Hello, I'm Bobby.");
        System.out.println("     What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Task[] tasks = new Task[100];
        int count = 0;

        while (!input.trim().equalsIgnoreCase("bye")) {
            System.out.println(LINE);
            if (input.trim().equalsIgnoreCase("list")) {
                if (tasks[0] == null) {
                    System.out.println("No tasks added yet.");
                } else {
                    System.out.println("Here are the tasks in your list:");
                }
                for (int i = 0; i < count; i++) {
                    System.out.println("     " + (i + 1) + ".[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
            } else if (input.trim().toLowerCase().startsWith("mark ")) {
                String taskNumber = input.trim().substring("mark ".length()).trim();
                try {
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index < 0 || index >= count) {
                        System.out.println("     Invalid task number.");
                    } else {
                        tasks[index].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[index].getDescription());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Invalid task number.");
                }
            } else if (input.trim().toLowerCase().startsWith("unmark ")) {
                String taskNumber = input.trim().substring("unmark ".length()).trim();
                try {
                    int index = Integer.parseInt(taskNumber) - 1;
                    if (index < 0 || index >= count) {
                        System.out.println("     Invalid task number.");
                    } else {
                        tasks[index].markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks[index].getDescription());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Invalid task number.");
                }
            } else {
                tasks[count] = new Task(input);
                count++;
                System.out.println("     added: " + input);
            }
            System.out.println(LINE);
            input = scanner.nextLine();
        }

        System.out.println(LINE);
        System.out.println("     Bye! Hope to see you again soon. ");
        System.out.println(LINE);
    }
}
