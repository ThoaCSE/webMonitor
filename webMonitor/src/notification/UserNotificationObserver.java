package notification;

import model.Subscription;
import model.User;
import observer.Observer;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNotificationObserver that = (UserNotificationObserver) o;
        return Objects.equals(user.getUserId(), that.user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserId());
    }
}