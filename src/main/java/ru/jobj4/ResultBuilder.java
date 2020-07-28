package ru.jobj4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ru.jobj4
 *
 * @author romanvohmin
 * @since 28.07.2020
 */
public class ResultBuilder {
    private final Storage storage;
    private final JsonParser jsonParser;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public ResultBuilder(Storage storage, JsonParser jsonParser) {
        this.storage = storage;
        this.jsonParser = jsonParser;
    }

    public void decrement() {
        int val;
        do {
            val = count.get();
        } while (!count.compareAndSet(val, val - 1));
    }

    public void execute() {
        count.set(storage.getKeys().size());
        while (count.get() != 0) {
            decrement();
            Callable<Boolean> task =
                    () -> {
                        jsonParser.generateNewJson();
                        return true;
                    };
            Future<Boolean> future = pool.submit(task);
            try {
                System.out.println("Статус окончания работы одного потока по новому json: " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
    }
}
