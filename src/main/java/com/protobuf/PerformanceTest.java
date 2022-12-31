package com.protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.grpc.models.Person;
import com.json.JPerson;

public class PerformanceTest {

    public static void main(String[] args) {

        //json
        JPerson person = new JPerson();
        person.setName("sam");
        person.setAge(10);
        ObjectMapper mappper = new ObjectMapper();

        Runnable json = () -> {
            try {
                byte[] bytes = mappper.writeValueAsBytes(person);
                JPerson person1 = mappper.readValue(bytes, JPerson.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        //protobuf
        Runnable protobuf = () -> {
            Person sam = Person.newBuilder()
                    .setName("sam")
                    .setAge(10)
                    .build();

            byte[] bytes = sam.toByteArray();
            try {
                Person sam1 = Person.parseFrom(bytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        for (int i = 0; i < 5; i++) {
            runPerformanceTest(json, "JSON");
            runPerformanceTest(protobuf, "PROTO");
        }

    }

    private static void runPerformanceTest(Runnable runnable, String method) {
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 5_000_000; i++) {
            runnable.run();
        }
        long time2 = System.currentTimeMillis();

        System.out.println(method + " : " + (time2 - time1) + " ms");

    }


}
