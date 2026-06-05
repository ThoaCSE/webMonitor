package controller;

import model.Subscription;
import model.subcriptionManager;
import model.User;
import model.webMonitor;
import strategy.webComparision;
import java.util.ArrayList;

public class monitorController {
    private subcriptionManager subscriptionManager;
    private webMonitor websiteMonitor;

    public monitorController() {
        this.subscriptionManager = new subcriptionManager();
        this.websiteMonitor = new webMonitor();
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

    public subcriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    public void setComparisonStrategy(webComparision strategy) {
        this.websiteMonitor.setComparisonStrategy(strategy);
    }

    public void checkAllSubscriptions(boolean suppressNoChange) {
        checkSubscriptions(subscriptionManager.getSubscriptions(), suppressNoChange);
    }

    public void checkSubscription(Subscription subscription, boolean suppressNoChange) {
        ArrayList<Subscription> targets = new ArrayList<>();
        targets.add(subscription);
        checkSubscriptions(targets, suppressNoChange);
    }

    private void checkSubscriptions(ArrayList<Subscription> subscriptions, boolean suppressNoChange) {
        long nowTime = System.currentTimeMillis() / 1000;

        for (Subscription s : subscriptions) {
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

                if (changed || !suppressNoChange) {
                    s.notifyObservers(changed);
                }

            } catch (Exception e) {
                System.out.println("Error checking " + s.getUrl() + ": " + e.getMessage());
                System.out.println("----------------------------------------------------");
            }
        }
    }
}