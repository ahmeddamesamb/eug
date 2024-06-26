package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DisciplineSportiveEtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantSample1() {
        return new DisciplineSportiveEtudiant().id(1L);
    }

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantSample2() {
        return new DisciplineSportiveEtudiant().id(2L);
    }

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantRandomSampleGenerator() {
        return new DisciplineSportiveEtudiant().id(longCount.incrementAndGet());
    }
}
