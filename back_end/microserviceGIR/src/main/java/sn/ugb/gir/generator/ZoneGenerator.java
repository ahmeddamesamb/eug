package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Zone;

public class ZoneGenerator {
    private static final Faker faker = new Faker();

    public static Zone generateZone() {
        Zone zone = new Zone();
        zone.setLibelleZone(faker.lorem().word());
        return zone;
    }
}
