package model;

import observer.Observer;
import observer.Subject;

import java.util.ArrayList;

public class Subscription implements Subject {
    private String url;
    private int frequency;
    private String channel;
    private String lastContent;
    private ArrayList<Observer> observers;
    private ArrayList<User> users;
    private long lastCheckTimeSeconds;

    public Subscription(String url, int frequency, String channel) {
        this.url = url;
        this.frequency = frequency;
        this.channel = channel;
        this.lastContent = "";
        this.observers = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public String getUrl() { return url; }
    public int getFrequency() { return frequency; }
    public String getChannel() { return channel; }
    public String getLastContent() { return lastContent; }
    public long getLastCheckTimeSeconds() { return lastCheckTimeSeconds; }

    public void updateLastContent(String content) { this.lastContent = content; }

    public void setLastCheckTimeSeconds(long t) { this.lastCheckTimeSeconds = t; }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int getObserverCount() {
        return observers.size();
    }

    public void addSubscriber(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void removeSubscriber(User user) {
        users.remove(user);
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void notifyObservers(boolean changed) {
        for (Observer observer : observers) {
            observer.update(this, changed);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }
}