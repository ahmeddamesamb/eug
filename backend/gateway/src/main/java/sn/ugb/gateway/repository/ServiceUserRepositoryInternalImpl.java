package sn.ugb.gateway.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.ServiceUser;
import sn.ugb.gateway.repository.rowmapper.ServiceUserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the ServiceUser entity.
 */
@SuppressWarnings("unused")
class ServiceUserRepositoryInternalImpl extends SimpleR2dbcRepository<ServiceUser, Long> implements ServiceUserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ServiceUserRowMapper serviceuserMapper;

    private static final Table entityTable = Table.aliased("service_user", EntityManager.ENTITY_ALIAS);

    public ServiceUserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ServiceUserRowMapper serviceuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ServiceUser.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.serviceuserMapper = serviceuserMapper;
    }

    @Override
    public Flux<ServiceUser> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ServiceUser> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ServiceUserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ServiceUser.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ServiceUser> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ServiceUser> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private ServiceUser process(Row row, RowMetadata metadata) {
        ServiceUser entity = serviceuserMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ServiceUser> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
