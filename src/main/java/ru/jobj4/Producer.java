package ru.jobj4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Producer {
    private final String url;
    private final Storage storage;
    private final JsonParser jsonParser;
    private final AtomicReference<Integer> count = new AtomicReference<>(0);
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public Producer(Storage storage, JsonParser jsonParser, String url) {
        this.storage = storage;
        this.jsonParser = jsonParser;
        this.url = url;
    }

    public void decrement() {
        int val;
        do {
            val = count.get();
        } while (!count.compareAndSet(val, val - 1));
    }

    public void producer() throws InterruptedException, ExecutionException {

        Thread init = new Thread(() -> {
            String json = jsonParser.getJsonFromUrl(url);
            jsonParser.getJsonArray(json);
        });
        init.start();
        init.join();

        count.set(storage.getQueue().size());
        while (count.get() != 0) {
            decrement();
            Callable<Boolean> task =
                    () -> {
                        try {
                            String json = storage.getQueue().poll();
                            String sourceDataUrl = jsonParser.getsourceDataUrl(json);
                            String tokenDataUrl = jsonParser.getTokenDataUrl(json);
                            Integer id = Integer.valueOf(jsonParser.getId(json));

                            storage.getKeys().offer(id);
                            storage.getStorage().putIfAbsent(id, new ConcurrentHashMap<>());

                            String innerJson1 = jsonParser.getJsonFromUrl(sourceDataUrl);
                            String urlType = jsonParser.getUrlType(innerJson1);
                            String videoUrl = jsonParser.getVideoUrl(innerJson1);

                            String innerJson2 = jsonParser.getJsonFromUrl(tokenDataUrl);
                            String value = jsonParser.getValue(innerJson2);
                            String ttl = jsonParser.getTtl(innerJson2);

                            if (storage.getStorage().containsKey(id)) {
                                storage.getStorage().get(id).put("id", String.valueOf(id));
                                storage.getStorage().get(id).put("urlType", urlType);
                                storage.getStorage().get(id).put("videoUrl", videoUrl);
                                storage.getStorage().get(id).put("value", value);
                                storage.getStorage().get(id).put("ttl", ttl);
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    };
            Future<Boolean> future = pool.submit(task);
            System.out.println("Статус окончания работы одного потока по одной видеокамере: " + future.get());
        }
        pool.shutdown();
    }
}
