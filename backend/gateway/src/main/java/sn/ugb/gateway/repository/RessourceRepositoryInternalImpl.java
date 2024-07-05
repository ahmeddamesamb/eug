package sn.ugb.gateway.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;

import org.springframework.data.domain.Page;
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
import sn.ugb.gateway.domain.Ressource;
import sn.ugb.gateway.repository.rowmapper.RessourceRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the Ressource entity.
 */
@SuppressWarnings("unused")
class RessourceRepositoryInternalImpl extends SimpleR2dbcRepository<Ressource, Long> implements RessourceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RessourceRowMapper ressourceMapper;

    private static final Table entityTable = Table.aliased("ressource", EntityManager.ENTITY_ALIAS);

    public RessourceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RessourceRowMapper ressourceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Ressource.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.ressourceMapper = ressourceMapper;
    }

    @Override
    public Flux<Ressource> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Ressource> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RessourceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Ressource.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Ressource> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Ressource> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }



private Ressource process(Row row, RowMetadata metadata) {
        Ressource entity = ressourceMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Ressource> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
