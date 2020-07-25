import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TheadPoolLearning {

    public static void main(String[] args) {


        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,100, TimeUnit.DAYS,new ArrayBlockingQueue<Runnable>(10));
        executor.execute(new Thread());


        Executors.newScheduledThreadPool(10);


    }
}

