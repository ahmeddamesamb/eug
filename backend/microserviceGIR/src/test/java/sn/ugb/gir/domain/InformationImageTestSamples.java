package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InformationImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InformationImage getInformationImageSample1() {
        return new InformationImage()
            .id(1L)
            .description("description1")
            .cheminPath("cheminPath1")
            .cheminFile("cheminFile1")
            .enCoursYN("enCoursYN1");
    }

    public static InformationImage getInformationImageSample2() {
        return new InformationImage()
            .id(2L)
            .description("description2")
            .cheminPath("cheminPath2")
            .cheminFile("cheminFile2")
            .enCoursYN("enCoursYN2");
    }

    public static InformationImage getInformationImageRandomSampleGenerator() {
        return new InformationImage()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .cheminPath(UUID.randomUUID().toString())
            .cheminFile(UUID.randomUUID().toString())
            .enCoursYN(UUID.randomUUID().toString());
    }
}
