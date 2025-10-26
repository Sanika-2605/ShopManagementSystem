package util;


public class AutoSaveThread extends Thread {
private final DataStore store = DataStore.getInstance();
private volatile boolean running = true;
private int intervalSec;


public AutoSaveThread(int intervalSec) {
this.intervalSec = intervalSec;
setDaemon(true);
}


public void shutdown() { running = false; }


@Override
public void run() {
while (running) {
try {
Thread.sleep(intervalSec * 1000L);
store.saveAll();
System.out.println("[AutoSave] Data saved.");
} catch (Exception e) {
System.err.println("[AutoSave] Failed to save: " + e.getMessage());
}
}
}
}
