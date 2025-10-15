import java.io.*;
import java.util.*;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String title;
    String priority;
    boolean done;
    String dueDate;

    Task(int id, String title, String priority, String dueDate) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.done = false;
        this.dueDate = dueDate;
    }

    void display() {
        System.out.printf("[%d] %s | Priority: %s | Due: %s | Status: %s\n",
                id, title, priority, (dueDate.isEmpty() ? "N/A" : dueDate), done ? "Done" : "Pending");
    }
}

public class TodoList {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static final String DATA_FILE = "tasks.dat";
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        loadTasks();

        while (true) {
            System.out.println("\n--- To-Do List Menu ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task as Done");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    markTaskDone();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    saveTasks();
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();

        System.out.print("Enter priority (Low/Medium/High): ");
        String priority = scanner.nextLine();
        if (!priority.equalsIgnoreCase("Low") && !priority.equalsIgnoreCase("Medium") && !priority.equalsIgnoreCase("High")) {
            priority = "Low";
        }

        System.out.print("Enter due date (optional, format YYYY-MM-DD or leave empty): ");
        String dueDate = scanner.nextLine();

        Task t = new Task(nextId++, title, priority, dueDate);
        tasks.add(t);
        System.out.println("Task added.");
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\nYour Tasks:");
        for (Task t : tasks) {
            t.display();
        }
    }

    private static void markTaskDone() {
        System.out.print("Enter task ID to mark as done: ");
        int id = scanner.nextInt();
        for (Task t : tasks) {
            if (t.id == id) {
                t.done = true;
                System.out.println("Task marked as done.");
                return;
            }
        }
        System.out.println("Task ID not found.");
    }

    private static void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        int id = scanner.nextInt();
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            Task t = it.next();
            if (t.id == id) {
                it.remove();
                System.out.println("Task deleted.");
                return;
            }
        }
        System.out.println("Task ID not found.");
    }

    private static void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasks() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            tasks = (ArrayList<Task>) ois.readObject();
            nextId = tasks.stream().mapToInt(t -> t.id).max().orElse(0) + 1;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}

