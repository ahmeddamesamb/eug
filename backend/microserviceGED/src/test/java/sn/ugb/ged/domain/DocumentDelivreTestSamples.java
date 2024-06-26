package sn.ugb.ged.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentDelivreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentDelivre getDocumentDelivreSample1() {
        return new DocumentDelivre().id(1L).libelleDoc("libelleDoc1");
    }

    public static DocumentDelivre getDocumentDelivreSample2() {
        return new DocumentDelivre().id(2L).libelleDoc("libelleDoc2");
    }

    public static DocumentDelivre getDocumentDelivreRandomSampleGenerator() {
        return new DocumentDelivre().id(longCount.incrementAndGet()).libelleDoc(UUID.randomUUID().toString());
    }
}
