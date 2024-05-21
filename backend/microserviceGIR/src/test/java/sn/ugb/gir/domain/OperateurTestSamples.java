package sn.ugb.gir.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OperateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Operateur getOperateurSample1() {
        return new Operateur().id(1L).libelle("libelle1").userLogin("userLogin1").codeOperateur("codeOperateur1");
    }

    public static Operateur getOperateurSample2() {
        return new Operateur().id(2L).libelle("libelle2").userLogin("userLogin2").codeOperateur("codeOperateur2");
    }

    public static Operateur getOperateurRandomSampleGenerator() {
        return new Operateur()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .userLogin(UUID.randomUUID().toString())
            .codeOperateur(UUID.randomUUID().toString());
    }
}
