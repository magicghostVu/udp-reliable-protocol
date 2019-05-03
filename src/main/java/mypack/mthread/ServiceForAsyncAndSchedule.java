package mypack.mthread;

import java.util.concurrent.*;

public class ServiceForAsyncAndSchedule {
    private static ServiceForAsyncAndSchedule ourInstance = new ServiceForAsyncAndSchedule();

    public static ServiceForAsyncAndSchedule getInstance() {
        return ourInstance;
    }


    private ExecutorService threadPoolForAsync;

    private ScheduledExecutorService threadPoolForSchedule;


    private ServiceForAsyncAndSchedule() {
        threadPoolForAsync = Executors.newSingleThreadExecutor();
        threadPoolForSchedule = Executors.newSingleThreadScheduledExecutor();
    }

    public ScheduledFuture<?> scheduleFixRate(Runnable r, long initDelay, long period, TimeUnit tu) {
        return threadPoolForSchedule.scheduleAtFixedRate(r, initDelay, period, tu);
    }


}
