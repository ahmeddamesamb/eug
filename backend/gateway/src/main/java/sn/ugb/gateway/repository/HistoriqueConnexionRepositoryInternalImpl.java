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
import sn.ugb.gateway.domain.HistoriqueConnexion;
import sn.ugb.gateway.repository.rowmapper.HistoriqueConnexionRowMapper;
import sn.ugb.gateway.repository.rowmapper.InfosUserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the HistoriqueConnexion entity.
 */
@SuppressWarnings("unused")
class HistoriqueConnexionRepositoryInternalImpl
    extends SimpleR2dbcRepository<HistoriqueConnexion, Long>
    implements HistoriqueConnexionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InfosUserRowMapper infosuserMapper;
    private final HistoriqueConnexionRowMapper historiqueconnexionMapper;

    private static final Table entityTable = Table.aliased("historique_connexion", EntityManager.ENTITY_ALIAS);
    private static final Table infoUserTable = Table.aliased("infos_user", "infoUser");

    public HistoriqueConnexionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InfosUserRowMapper infosuserMapper,
        HistoriqueConnexionRowMapper historiqueconnexionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HistoriqueConnexion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.infosuserMapper = infosuserMapper;
        this.historiqueconnexionMapper = historiqueconnexionMapper;
    }

    @Override
    public Flux<HistoriqueConnexion> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HistoriqueConnexion> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HistoriqueConnexionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InfosUserSqlHelper.getColumns(infoUserTable, "infoUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(infoUserTable)
            .on(Column.create("info_user_id", entityTable))
            .equals(Column.create("id", infoUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HistoriqueConnexion.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HistoriqueConnexion> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HistoriqueConnexion> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private HistoriqueConnexion process(Row row, RowMetadata metadata) {
        HistoriqueConnexion entity = historiqueconnexionMapper.apply(row, "e");
        entity.setInfoUser(infosuserMapper.apply(row, "infoUser"));
        return entity;
    }

    @Override
    public <S extends HistoriqueConnexion> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
