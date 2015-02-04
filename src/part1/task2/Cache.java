package part1.task2;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {

    private static final long DEFAULT_MILLIS_UNTIL_EXPIRATION = 60*60*1000; // 60 minutes
    private long millisUntilExpiration = DEFAULT_MILLIS_UNTIL_EXPIRATION;
    private Map<String, EntryCache> entryCacheMap = new LinkedHashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock read = readWriteLock.readLock();
    private final Lock write = readWriteLock.writeLock();
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    private Runnable removeFromCache = new Runnable() {
        public void run() {
            cleanUp();
        }
    };

    public class EntryCache {
        private String time;
        private long timestamp;

        public EntryCache(String time, long timestamp) {
            this.time = time;
            this.timestamp = timestamp;
        }

        String getTime() {
            return time;
        }

        void setTime(String time) {
            this.time = time;
        }

        long getTimestamp() {
            return timestamp;
        }

        void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public Cache() {
    }

    public Cache(long millisUntilExpiration) {
        this.millisUntilExpiration = millisUntilExpiration;
    }

    public void put(String key) {
        write.lock();
        try {
            if (entryCacheMap.get(key) == null) {
                EntryCache value = new EntryCache(getCurrentTime(), System.currentTimeMillis());
                entryCacheMap.put(key, value);

                System.out.format("Message \"%s\" saved at %s\n", key, value.getTime());
                service.scheduleAtFixedRate(removeFromCache, millisUntilExpiration, millisUntilExpiration, TimeUnit.MILLISECONDS);

            } else System.out.format("Message \"%s\" was not saved because already exist\n", key);
        } finally {
            write.unlock();
        }
    }

    public EntryCache getEntry(String key) {
        read.lock();
        EntryCache e = null;
        try {
            e = entryCacheMap.get(key);
        } finally {
            read.unlock();
        }
        return e;
    }

    public boolean isInCache(String key) {
        return getEntry(key) == null;
    }

    public void cleanUp() {
        List<String> list = new ArrayList<>(entryCacheMap.keySet());
        for (ListIterator<String> iterator = list.listIterator(list.size()); iterator.hasPrevious(); ) {
            String key = iterator.previous();
            EntryCache e = entryCacheMap.get(key);
            long now = System.currentTimeMillis();
            long liveTime = e.getTimestamp() + millisUntilExpiration;
            if (liveTime < now) {
                System.out.format("%s: \"%s\" (saved at %s) DELETED\n", getCurrentTime(), key, entryCacheMap.get(key).getTime());
                read.lock();
                iterator.remove();
                read.unlock();
                break;
            }
        }
    }

    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }
}
