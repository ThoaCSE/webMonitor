package notification;

import model.Subscription;
import model.User;

public class Notification {

    public void notifyUser(User user, Subscription sub, boolean changed) {
        if (changed) {
            System.out.println("Notification for " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println("Website updated: " + sub.getUrl());
            System.out.println("Channel: " + sub.getChannel());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("Notification for " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println("Nothing changes: " + sub.getUrl());
            System.out.println("Channel: " + sub.getChannel());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        }
    }
}