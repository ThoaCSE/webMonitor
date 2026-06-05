# WebMonitor Exercise 6

This program is an exercise implementation for Exercise 6 using the Strategy design pattern.
It monitors website content changes via subscriptions and supports multiple comparison strategies.

## 1. Program overview

The application is built around a controller-driven monitor app with these goals:
- register users and website subscriptions
- fetch website content at a configured frequency
- compare latest content against previously fetched content
- notify subscribers only when content changes
- support different comparison strategies via the Strategy pattern

## 2. Package summary

### `src/controller`
- `Program.java`
  - main menu and user interface for adding users, creating subscriptions, displaying subscriptions, and starting watch mode.
  - prompts the user to select comparison strategy and target subscription to watch.
- `monitorController.java`
  - application controller that orchestrates subscriptions, users, and the website monitor.
  - exposes methods to register users, add/remove subscriptions, set the comparison strategy, and check subscriptions.

### `src/htmlpages`
- `dynamic.html`
  - a test page that switches between two layouts and updates visible content every second.
  - useful for validating that the monitor detects website changes.
- `static.html`
  - a fixed HTML test page with stable structure, size, and text.
  - useful for validating that the monitor does not incorrectly report changes.

### `src/model`
- `webMonitor.java`
  - downloads website content and delegates change detection to the selected comparison strategy.
  - includes HTTP request headers and encoding handling to reduce 403 blocks.
- `Subscription.java`
  - represents a website subscription, including URL, frequency, channel, last content, and observers.
- `subcriptionManager.java`
  - stores users and subscriptions.
  - manages adding/removing subscriptions and connecting users to observers.
- `User.java`
  - stores basic user information: ID, name, and email.

### `src/notification`
- `Notification.java`
  - prints notifications to the console when a website either changes or remains unchanged.
- `userNotificationObserver.java`
  - observer implementation that notifies one user when a subscribed website is checked.

### `src/observer`
- `Observer.java`
  - observer interface used by subscription observers.
- `Subject.java`
  - subject interface for attach/detach/notify operations.

### `src/strategy`
- `webComparision.java`
  - strategy interface used for website content comparison.
- `htmlComparision.java`
  - compares raw HTML strings.
- `sizeComparison.java`
  - compares content length only.
- `textComparision.java`
  - strips HTML tags and compares visible text.

## 3. Guide to run the website

Serve the HTML pages locally from the project root:

```bash
cd /workspaces/webMonitor/webMonitor
python3 -m http.server 8080 --directory src/htmlpages
```

Then open these local URLs in your browser:

- `http://localhost:8080/dynamic.html`
- `http://localhost:8080/static.html`

Use these URLs when adding subscriptions so the monitor can fetch local test pages.

## 4. Run the main app

### Run via IDE

1. Open the project in your Java IDE.
2. Set `controller.Program` as the main class.
3. Run the application.

### Run via terminal

Compile the source files and run the program:

```bash
cd /workspaces/webMonitor/webMonitor
javac -d out src/controller/*.java src/model/*.java src/notification/*.java src/observer/*.java src/strategy/*.java
java -cp out controller.Program
```

Once running, use the menu to add users and subscriptions, then choose option `4` to watch changes.
