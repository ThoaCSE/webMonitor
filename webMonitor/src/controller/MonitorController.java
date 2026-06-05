package controller;

import model.Subscription;
import model.SubscriptionManager;
import model.User;
import model.WebMonitor;
import strategy.WebComparison;
import java.util.ArrayList;

public class MonitorController {
    private SubscriptionManager subscriptionManager;
    private WebMonitor websiteMonitor;

    public MonitorController() {
        this.subscriptionManager = new SubscriptionManager();
        this.websiteMonitor = new WebMonitor();
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

    public void setComparisonStrategy(WebComparison strategy) {
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