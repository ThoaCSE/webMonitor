package controller;

import model.Subscription;
import model.SubscriptionManager;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        MonitorController controller = new MonitorController();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");
            System.out.println(" Website Monitor");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1. Add user");
            System.out.println("2. Add subscription");
            System.out.println("3. Show users and subscriptions");
            System.out.println("4. Watch changes");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addUserUI(controller, scanner);
                    break;
                case "2":
                    addSubscriptionUI(controller, scanner);
                    break;
                case "3":
                    showUsersAndSubscriptionsUI(controller);
                    break;
                case "4":
                    watchChangesUI(controller, scanner);
                    break;
                case "5":
                    running = false;
                    System.out.println("Exiting program. Auf wiedersehen :D");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose 1-5.");
            }
        }

        scanner.close();
    }

    private static void addUserUI(MonitorController controller, Scanner scanner) {
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter your user name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter your user email: ");
        String email = scanner.nextLine().trim();

        User user = new User(userId, name, email);
        controller.registerUser(user);

        System.out.println("User added.");
    }

    private static void addSubscriptionUI(MonitorController controller, Scanner scanner) {
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter the website URL: ");
        String url = scanner.nextLine().trim();

        System.out.print("Enter the frequency in seconds: ");
        int frequency = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter the channel (e.g. email): ");
        String channel = scanner.nextLine().trim();

        controller.registerSubscription(userId, url, frequency, channel);
        System.out.println("Subscription added for user " + userId + ".");
    }

    private static void showUsersAndSubscriptionsUI(MonitorController controller) {
        SubscriptionManager manager = controller.getSubscriptionManager();
        ArrayList<User> users = manager.getUsers();
        ArrayList<Subscription> subs = manager.getSubscriptions();

        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            System.out.println("Users:");
            for (User u : users) {
                System.out.println("- ID: " + u.getUserId() + " | Name: " + u.getName() + " | Email: " + u.getEmail());
            }
        }

        if (subs.isEmpty()) {
            System.out.println("No subscriptions created.");
        } else {
            System.out.println("Subscriptions:");
            for (Subscription s : subs) {
                System.out.print("- URL: " + s.getUrl()
                        + ", frequency: " + s.getFrequency() + "s"
                        + ", channel: " + s.getChannel()
                        + ", users: ");

                if (s.getUsers().isEmpty()) {
                    System.out.println("(no users)");
                } else {
                    for (User u : s.getUsers()) {
                        System.out.print(u.getUserId() + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void watchChangesUI(MonitorController controller, Scanner scanner) {
        System.out.println("Starting watching changes. Press ENTER to stop.");
        Thread stopThread = new Thread(() -> {
            scanner.nextLine();

        });
        stopThread.start();

        long start = System.currentTimeMillis();

        while (stopThread.isAlive()) {
            long now = System.currentTimeMillis();

            if ((now - start) >= 1000) {
                controller.checkAllSubscriptions();
                start = now;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Watch mode stopped.");
    }
}