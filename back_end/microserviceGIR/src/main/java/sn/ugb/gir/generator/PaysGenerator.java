package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Pays;

public class PaysGenerator {
    private static final Faker faker = new Faker();

    public static Pays generatePays() {
        Pays pays = new Pays();
        pays.setLibellePays(faker.address().country());
        pays.setPaysEnAnglais(faker.address().country());
        pays.setNationalite(faker.demographic().demonym());
        pays.setCodePays(faker.address().countryCode());
        pays.setuEMOAYN(faker.bool().bool() ? 1 : 0);
        pays.setcEDEAOYN(faker.bool().bool() ? 1 : 0);
        pays.setrIMYN(faker.bool().bool() ? 1 : 0);
        pays.setAutreYN(faker.bool().bool() ? 1 : 0);
        pays.setEstEtrangerYN(faker.bool().bool() ? 1 : 0);
        return pays;
    }
}
