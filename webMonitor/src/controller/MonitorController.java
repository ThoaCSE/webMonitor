package controller;

import model.*;
import notification.Notification;

import java.util.ArrayList;

public class MonitorController {
    private SubscriptionManager subscriptionManager;
    private WebsiteMonitor websiteMonitor;

    public MonitorController() {
        subscriptionManager = new SubscriptionManager();
        websiteMonitor = new WebsiteMonitor();
    }

    public void registerUser(User user) {
        subscriptionManager.addUser(user);
    }

    public void registerSubscription(String userId, String url, int frequency, String channel) {
        subscriptionManager.addSubscriptionForUser(userId, url, frequency, channel);
    }

    public void cancelSubscription(String userId, String url) {
        subscriptionManager.removeSubscriptionForUser(userId, url);
    }

    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    public void checkAllSubscriptions() {

        long nowTime = System.currentTimeMillis() / 1000;

        for (Subscription s : subscriptionManager.getSubscriptions()) {
            long lastTime = s.getLastCheckTimeSeconds();
            int frequency = s.getFrequency();

            if (nowTime - lastTime < frequency) {
                continue;
            }

            s.setLastCheckTimeSeconds(nowTime);

            try {
                String newContent = websiteMonitor.downloadContent(s.getUrl());

                boolean changed = false;
                if (s.getLastContent().isEmpty()) {
                    s.updateLastContent(newContent);
                } else if (websiteMonitor.hasChanged(s.getLastContent(), newContent)) {
                    changed = true;
                    s.updateLastContent(newContent);
                }

                s.notifyObservers(changed);

            } catch (Exception e) {
                System.out.println("Error checking " + s.getUrl() + ": " + e.getMessage());
                System.out.println("----------------------------------------------------");
            }
        }
    }

    private ArrayList<User> getUsers() {
        return subscriptionManager.getUsers();
    }
}