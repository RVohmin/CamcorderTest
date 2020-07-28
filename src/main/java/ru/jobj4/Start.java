package ru.jobj4;

import java.util.concurrent.ExecutionException;

/**
 * ru.jobj4
 *
 * @author romanvohmin
 * @since 28.07.2020
 */
public class Start {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Storage storage = new Storage();
        String url = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5/";
        JsonParser jsonParser = new JsonParser(storage);
        Producer producer = new Producer(storage, jsonParser, url);
        producer.producer();
        ResultBuilder resultBuilder = new ResultBuilder(storage, jsonParser);
        resultBuilder.execute();
        storage.getResult().forEach(System.out::println);
    }
}
