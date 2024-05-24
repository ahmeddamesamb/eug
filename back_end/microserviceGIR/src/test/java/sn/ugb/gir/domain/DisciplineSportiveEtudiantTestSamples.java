package sn.ugb.gir.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DisciplineSportiveEtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantSample1() {
        return new DisciplineSportiveEtudiant().id(1L).licenceSportiveYN(1).competitionUGBYN(1);
    }

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantSample2() {
        return new DisciplineSportiveEtudiant().id(2L).licenceSportiveYN(2).competitionUGBYN(2);
    }

    public static DisciplineSportiveEtudiant getDisciplineSportiveEtudiantRandomSampleGenerator() {
        return new DisciplineSportiveEtudiant()
            .id(longCount.incrementAndGet())
            .licenceSportiveYN(intCount.incrementAndGet())
            .competitionUGBYN(intCount.incrementAndGet());
    }
}
