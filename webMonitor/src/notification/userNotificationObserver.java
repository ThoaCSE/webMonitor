package notification;

import model.Subscription;
import model.User;
import observer.Observer;
import java.util.Objects;

public class userNotificationObserver implements Observer {
    private User user;
    private Notification notification;

    public userNotificationObserver(User user) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        userNotificationObserver that = (userNotificationObserver) o;
        return Objects.equals(user.getUserId(), that.user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId());
    }
}