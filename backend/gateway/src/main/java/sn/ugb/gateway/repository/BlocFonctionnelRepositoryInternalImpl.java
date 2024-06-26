package sn.ugb.gateway.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.BlocFonctionnel;
import sn.ugb.gateway.repository.rowmapper.BlocFonctionnelRowMapper;
import sn.ugb.gateway.repository.rowmapper.ServiceUserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the BlocFonctionnel entity.
 */
@SuppressWarnings("unused")
class BlocFonctionnelRepositoryInternalImpl
    extends SimpleR2dbcRepository<BlocFonctionnel, Long>
    implements BlocFonctionnelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ServiceUserRowMapper serviceuserMapper;
    private final BlocFonctionnelRowMapper blocfonctionnelMapper;

    private static final Table entityTable = Table.aliased("bloc_fonctionnel", EntityManager.ENTITY_ALIAS);
    private static final Table serviceTable = Table.aliased("service_user", "service");

    public BlocFonctionnelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ServiceUserRowMapper serviceuserMapper,
        BlocFonctionnelRowMapper blocfonctionnelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(BlocFonctionnel.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.serviceuserMapper = serviceuserMapper;
        this.blocfonctionnelMapper = blocfonctionnelMapper;
    }

    @Override
    public Flux<BlocFonctionnel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<BlocFonctionnel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BlocFonctionnelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ServiceUserSqlHelper.getColumns(serviceTable, "service"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(serviceTable)
            .on(Column.create("service_id", entityTable))
            .equals(Column.create("id", serviceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, BlocFonctionnel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<BlocFonctionnel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<BlocFonctionnel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private BlocFonctionnel process(Row row, RowMetadata metadata) {
        BlocFonctionnel entity = blocfonctionnelMapper.apply(row, "e");
        entity.setService(serviceuserMapper.apply(row, "service"));
        return entity;
    }

    @Override
    public <S extends BlocFonctionnel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
