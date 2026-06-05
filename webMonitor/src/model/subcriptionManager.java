package model;

import notification.userNotificationObserver;
import java.util.ArrayList;

public class subcriptionManager {
    private ArrayList<User> users;
    private ArrayList<Subscription> subscriptions;

    public subcriptionManager() {
        users = new ArrayList<>();
        subscriptions = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User findUser(String id) {
        for (User u : users) {
            if (u.getUserId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public Subscription findSubscription(String url) {
        for (Subscription s : subscriptions) {
            if (s.getUrl().equals(url)) {
                return s;
            }
        }
        return null;
    }

    public void addSubscriptionForUser(String userId, String url, int frequency, String channel) {
        User user = findUser(userId);
        if (user == null) return;

        Subscription sub = findSubscription(url);
        if (sub == null) {
            sub = new Subscription(url, frequency, channel);
            subscriptions.add(sub);
        }

        sub.addSubscriber(user);
        sub.attach(new userNotificationObserver(user));
    }

    public void removeSubscriptionForUser(String userId, String url) {
        User user = findUser(userId);
        if (user == null) return;

        Subscription sub = findSubscription(url);
        if (sub != null) {
            sub.removeSubscriber(user);

            userNotificationObserver observerToRemove = new userNotificationObserver(user);
            sub.detach(observerToRemove);

            if (sub.getUsers().isEmpty()) {
                subscriptions.remove(sub);
            }
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }
}