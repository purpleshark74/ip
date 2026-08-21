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
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
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
                        System.out.println("       " + tasks[index]);
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
                        System.out.println("       " + tasks[index]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("     Invalid task number.");
                }
            } else if (input.trim().toLowerCase().startsWith("todo ")) {
                tasks[count] = new Todo(input.trim().substring("todo ".length()).trim());
                count++;
                printAddedTask(tasks, count);
            } else if (input.trim().toLowerCase().startsWith("deadline ")) {
                String[] parts = input.trim().substring("deadline ".length()).split(" /by ", 2);
                if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                    System.out.println("     Please use: deadline DESCRIPTION /by DEADLINE");
                } else {
                    tasks[count] = new Deadline(parts[0].trim(), parts[1].trim());
                    count++;
                    printAddedTask(tasks, count);
                }
            } else if (input.trim().toLowerCase().startsWith("event ")) {
                String eventDetails = input.trim().substring("event ".length());
                String[] fromParts = eventDetails.split(" /from ", 2);
                String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
                if (fromParts.length < 2 || toParts.length < 2 || fromParts[0].trim().isEmpty()
                        || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                    System.out.println("     Please use: event DESCRIPTION /from START /to END");
                } else {
                    tasks[count] = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
                    count++;
                    printAddedTask(tasks, count);
                }
            } else {
                tasks[count] = new Todo(input.trim());
                count++;
                printAddedTask(tasks, count);
            }
            System.out.println(LINE);
            input = scanner.nextLine();
        }

        System.out.println(LINE);
        System.out.println("     Bye! Hope to see you again soon. ");
        System.out.println(LINE);
    }

    /**
     * Prints confirmation after the most recently added task.
     *
     * @param tasks the task list
     * @param count the number of tasks stored
     */
    private static void printAddedTask(Task[] tasks, int count) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks[count - 1]);
        System.out.println("     Now you have " + count + " tasks in the list.");
    }
}
