package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.domain.Region;

import java.util.List;

public class RegionGenerator {
    public static Faker getFaker() {
        return faker;
    }

    static final Faker faker = new Faker();

    public static Region generateRegion(List<Pays> pays) {
        Region region = new Region();
        region.setLibelleRegion(faker.address().state());
        region.setPays(pays.get(faker.random().nextInt(pays.size())));
        return region;
    }
}
