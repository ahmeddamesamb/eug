package sn.ugb.gir.generator;

import com.github.javafaker.Faker;
import sn.ugb.gir.domain.Operation;
import sn.ugb.gir.domain.TypeOperation;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class OperationGenerator {
    private static final Faker faker = new Faker();

    public static Operation generateOperation(List<TypeOperation> typeOperationList) {
        Operation operation = new Operation();
        operation.setDateExecution(Instant.parse(LocalDate.now().toString()));
        operation.setEmailUser(faker.internet().emailAddress());
        operation.setDetailOperation(faker.lorem().sentence());
        operation.setInfoSysteme(faker.lorem().sentence());
        operation.setTypeOperation(typeOperationList.get(faker.random().nextInt(typeOperationList.size())));
        return operation;
    }
}
