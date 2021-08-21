import java.util.Scanner;
import java.util.Arrays;

public class Duke {
    public static void printLogoAndGreet() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Duke.");
        System.out.println(" How may I help you?");
    }

    public static void printTaskList(Task[] tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.length; i++) {
            System.out.println((i + 1) + ". " + "[" + tasks[i].getStatusIcon() +"] " + tasks[i].description);
        }
        System.out.println("____________________________________________________________");
    }

    public static void printGoodbye() {
        System.out.println("____________________________________________________________");
        System.out.println(" Goodbye! Hope to see you again soon.");
        System.out.println("____________________________________________________________");
    }

    public static void printTaskDone(Task[] tasks, int taskCount, String doneTask) {
        String[] doneSentence = doneTask.split(" ");
        if (doneSentence.length != 2) {
            System.out.println("____________________________________________________________");
            System.out.println(" Your command is of the wrong format! To mark a task as done, enter \"done {ID of done task}\"");
            System.out.println("____________________________________________________________");
            return;
        }

        int taskToMarkDone = Integer.parseInt(doneSentence[1]);
        if (taskToMarkDone > taskCount || taskToMarkDone <= 0) {
            System.out.println("____________________________________________________________");
            System.out.println(" Sorry, the task is not in the list! Try again.");
            System.out.println("____________________________________________________________");
        } else {
            tasks[taskToMarkDone - 1].markDone();
            System.out.println("____________________________________________________________");
            System.out.println(" Great! I have marked the following task as done: ");
            System.out.println("   [" + tasks[taskToMarkDone - 1].getStatusIcon() + "] " + tasks[taskToMarkDone - 1].description);
            System.out.println("____________________________________________________________");
        }
    }

    public static void printTaskAdded(Task[] tasks, int taskCount, String addedTask) {
        tasks[taskCount] = new Task(addedTask);
        System.out.println("____________________________________________________________");
        System.out.println(" I have added a task: " + addedTask);
        System.out.println("____________________________________________________________");
    }

    public static void main(String[] args) {
        printLogoAndGreet();

        String line;
        Scanner in = new Scanner(System.in);

        int taskCount = 0;
        Task[] tasks = new Task[100];
        line = in.nextLine();

        while (!line.equalsIgnoreCase("bye")) {
            if (line.equalsIgnoreCase("list")) {
                printTaskList(Arrays.copyOf(tasks, taskCount));
            } else if (line.toLowerCase().startsWith("done")) {
                printTaskDone(tasks, taskCount, line);
            } else {
                printTaskAdded(tasks, taskCount, line);
                taskCount++;
            }
            line = in.nextLine();
        }
        printGoodbye();
    }
}
