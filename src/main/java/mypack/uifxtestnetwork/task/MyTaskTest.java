package mypack.uifxtestnetwork.task;

import javafx.concurrent.Task;
import mypack.uifxtestnetwork.MController;

import java.util.concurrent.LinkedBlockingQueue;

public class MyTaskTest extends Task<Void> {


    private MController context;

    private int targetCount = 10;

    private int crCount;

    private LinkedBlockingQueue<Object> qJob;

    public MyTaskTest(MController context) {
        qJob = new LinkedBlockingQueue<>();
        crCount = 0;
        this.context = context;
    }

    @Override
    protected Void call() throws Exception {


        while (true) {
            Object o = qJob.take();
            crCount++;

            updateProgress(crCount, targetCount);
            if (crCount > targetCount) {
                break;
            }
        }


        return null;
    }


    //todo:logic here
    @Override
    protected void updateProgress(long workDone, long max) {
        super.updateProgress(workDone, max);
        updateMessage("update to "+ workDone);

    }

    public void addJob() {
        qJob.add(new Object());
    }
}
