package org.qlcc.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RequestTable getRequestTableSample1() {
        return new RequestTable()
            .id(1)
            .status("status1")
            .userId("userId1")
            .title("title1")
            .message("message1")
            .reply("reply1")
            .note("note1");
    }

    public static RequestTable getRequestTableSample2() {
        return new RequestTable()
            .id(2)
            .status("status2")
            .userId("userId2")
            .title("title2")
            .message("message2")
            .reply("reply2")
            .note("note2");
    }

    public static RequestTable getRequestTableRandomSampleGenerator() {
        return new RequestTable()
            .id(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .reply(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString());
    }
}
