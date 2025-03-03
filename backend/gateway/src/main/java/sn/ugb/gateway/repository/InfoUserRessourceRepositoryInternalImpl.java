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
import sn.ugb.gateway.domain.InfoUserRessource;
import sn.ugb.gateway.domain.Ressource;
import sn.ugb.gateway.repository.rowmapper.InfoUserRessourceRowMapper;
import sn.ugb.gateway.repository.rowmapper.InfosUserRowMapper;
import sn.ugb.gateway.repository.rowmapper.RessourceRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the InfoUserRessource entity.
 */
@SuppressWarnings("unused")
class InfoUserRessourceRepositoryInternalImpl
    extends SimpleR2dbcRepository<InfoUserRessource, Long>
    implements InfoUserRessourceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InfosUserRowMapper infosuserMapper;
    private final RessourceRowMapper ressourceMapper;
    private final InfoUserRessourceRowMapper infouserressourceMapper;

    private static final Table entityTable = Table.aliased("info_user_ressource", EntityManager.ENTITY_ALIAS);
    private static final Table infosUserTable = Table.aliased("infos_user", "infosUser");
    private static final Table ressourceTable = Table.aliased("ressource", "ressource");

    public InfoUserRessourceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InfosUserRowMapper infosuserMapper,
        RessourceRowMapper ressourceMapper,
        InfoUserRessourceRowMapper infouserressourceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InfoUserRessource.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.infosuserMapper = infosuserMapper;
        this.ressourceMapper = ressourceMapper;
        this.infouserressourceMapper = infouserressourceMapper;
    }

    @Override
    public Flux<InfoUserRessource> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InfoUserRessource> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InfoUserRessourceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InfosUserSqlHelper.getColumns(infosUserTable, "infosUser"));
        columns.addAll(RessourceSqlHelper.getColumns(ressourceTable, "ressource"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(infosUserTable)
            .on(Column.create("infos_user_id", entityTable))
            .equals(Column.create("id", infosUserTable))
            .leftOuterJoin(ressourceTable)
            .on(Column.create("ressource_id", entityTable))
            .equals(Column.create("id", ressourceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InfoUserRessource.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InfoUserRessource> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InfoUserRessource> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<Ressource> findAllByInfoUserId(Long infoUserId, Pageable pageable) {
        return null;
    }

    private InfoUserRessource process(Row row, RowMetadata metadata) {
        InfoUserRessource entity = infouserressourceMapper.apply(row, "e");
        entity.setInfosUser(infosuserMapper.apply(row, "infosUser"));
        entity.setRessource(ressourceMapper.apply(row, "ressource"));
        return entity;
    }

    @Override
    public <S extends InfoUserRessource> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
