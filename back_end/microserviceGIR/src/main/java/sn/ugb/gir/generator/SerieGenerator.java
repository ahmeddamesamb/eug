package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Serie;

public class SerieGenerator {
    private static final Faker faker = new Faker();

    public static Serie generateSerie() {
        Serie serie = new Serie();
        serie.setCodeSerie(faker.code().isbn10());
        serie.setLibelleSerie(faker.educator().course());
        serie.setSigleSerie(faker.lorem().characters(3));
        return serie;
    }
}
