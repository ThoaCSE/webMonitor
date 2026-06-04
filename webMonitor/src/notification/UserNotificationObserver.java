package notification;

import model.Subscription;
import model.User;
import observer.Observer;

public class UserNotificationObserver implements Observer {
    private User user;
    private Notification notification;

    public UserNotificationObserver(User user) {
        this.user = user;
        this.notification = new Notification();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void update(Subscription subscription, boolean changed) {
        notification.notifyUser(user, subscription, changed);
    }

}