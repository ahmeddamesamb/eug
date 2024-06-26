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
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.repository.rowmapper.InfosUserRowMapper;
import sn.ugb.gateway.repository.rowmapper.UserRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the InfosUser entity.
 */
@SuppressWarnings("unused")
class InfosUserRepositoryInternalImpl extends SimpleR2dbcRepository<InfosUser, Long> implements InfosUserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final InfosUserRowMapper infosuserMapper;

    private static final Table entityTable = Table.aliased("infos_user", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("ugb_user", "e_user");

    public InfosUserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        InfosUserRowMapper infosuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InfosUser.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.infosuserMapper = infosuserMapper;
    }

    @Override
    public Flux<InfosUser> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InfosUser> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InfosUserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InfosUser.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InfosUser> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InfosUser> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<InfosUser> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<InfosUser> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<InfosUser> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private InfosUser process(Row row, RowMetadata metadata) {
        InfosUser entity = infosuserMapper.apply(row, "e");
        entity.setUser(userMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends InfosUser> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
