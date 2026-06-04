package observer;

import model.Subscription;

public interface Observer {
    void update(Subscription subscription, boolean changed);
}