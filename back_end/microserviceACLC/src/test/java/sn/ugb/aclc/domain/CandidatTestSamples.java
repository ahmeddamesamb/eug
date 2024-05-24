package sn.ugb.aclc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CandidatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Candidat getCandidatSample1() {
        return new Candidat().id(1L).nomCanditat("nomCanditat1").prenomCandidat("prenomCandidat1").emailCandidat("emailCandidat1");
    }

    public static Candidat getCandidatSample2() {
        return new Candidat().id(2L).nomCanditat("nomCanditat2").prenomCandidat("prenomCandidat2").emailCandidat("emailCandidat2");
    }

    public static Candidat getCandidatRandomSampleGenerator() {
        return new Candidat()
            .id(longCount.incrementAndGet())
            .nomCanditat(UUID.randomUUID().toString())
            .prenomCandidat(UUID.randomUUID().toString())
            .emailCandidat(UUID.randomUUID().toString());
    }
}
