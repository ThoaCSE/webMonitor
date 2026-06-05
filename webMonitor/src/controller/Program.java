package controller;

import model.Subscription;
import model.User;
import model.subcriptionManager;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        monitorController controller = new monitorController();
        try (Scanner scanner = new Scanner(System.in)) {
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
        }
    }

    private static void addUserUI(monitorController controller, Scanner scanner) {
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

    private static void addSubscriptionUI(monitorController controller, Scanner scanner) {
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

    private static void showUsersAndSubscriptionsUI(monitorController controller) {
        subcriptionManager manager = controller.getSubscriptionManager();
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

    private static void watchChangesUI(monitorController controller, Scanner scanner) {
        if (controller.getSubscriptionManager().getSubscriptions().isEmpty()) {
            System.out.println("No subscriptions available to watch.");
            return;
        }

        System.out.println("--- Choose Website Comparison Strategy ---");
        System.out.println("1. Identical content size");
        System.out.println("2. Identical HTML content");
        System.out.println("3. Identical text content");
        System.out.print("Select strategy: ");

        String strategyChoice = scanner.nextLine().trim();
        switch (strategyChoice) {
            case "1":
                controller.setComparisonStrategy(new strategy.sizeComparison());
                System.out.println("Using size comparison.");
                break;
            case "3":
                controller.setComparisonStrategy(new strategy.textComparision());
                System.out.println("Using text comparison.");
                break;
            default:
                controller.setComparisonStrategy(new strategy.htmlComparision());
                System.out.println("Using HTML comparison.");
                break;
        }

        Subscription selected = selectWatchTarget(controller, scanner);

        System.out.println("Starting watching changes. Press ENTER to stop.");
        Thread stopThread = new Thread(() -> scanner.nextLine());
        stopThread.start();

        long start = System.currentTimeMillis();
        while (stopThread.isAlive()) {
            long now = System.currentTimeMillis();
            if ((now - start) >= 1000) {
                if (selected == null) {
                    controller.checkAllSubscriptions(false);
                } else {
                    controller.checkSubscription(selected, false);
                }
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

    private static Subscription selectWatchTarget(monitorController controller, Scanner scanner) {
        subcriptionManager manager = controller.getSubscriptionManager();
        ArrayList<Subscription> subs = manager.getSubscriptions();

        System.out.println("--- Select subscription to watch ---");
        System.out.println("0. All subscriptions");
        for (int i = 0; i < subs.size(); i++) {
            Subscription sub = subs.get(i);
            System.out.println((i + 1) + ". " + sub.getUrl() + " (frequency: " + sub.getFrequency() + "s, channel: " + sub.getChannel() + ")");
        }
        System.out.print("Choose a target: ");

        String choice = scanner.nextLine().trim();
        if (choice.equals("0")) {
            return null;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < subs.size()) {
                return subs.get(index);
            }
        } catch (NumberFormatException ignored) {
        }

        System.out.println("Invalid selection. Defaulting to all subscriptions.");
        return null;
    }

}

