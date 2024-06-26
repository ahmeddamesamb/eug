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
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;
import sn.ugb.gateway.repository.rowmapper.BlocFonctionnelRowMapper;
import sn.ugb.gateway.repository.rowmapper.UserProfileBlocFonctionnelRowMapper;
import sn.ugb.gateway.repository.rowmapper.UserProfileRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the UserProfileBlocFonctionnel entity.
 */
@SuppressWarnings("unused")
class UserProfileBlocFonctionnelRepositoryInternalImpl
    extends SimpleR2dbcRepository<UserProfileBlocFonctionnel, Long>
    implements UserProfileBlocFonctionnelRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserProfileRowMapper userprofileMapper;
    private final BlocFonctionnelRowMapper blocfonctionnelMapper;
    private final UserProfileBlocFonctionnelRowMapper userprofileblocfonctionnelMapper;

    private static final Table entityTable = Table.aliased("user_profile_bloc_fonctionnel", EntityManager.ENTITY_ALIAS);
    private static final Table userProfilTable = Table.aliased("user_profile", "userProfil");
    private static final Table blocFonctionnelTable = Table.aliased("bloc_fonctionnel", "blocFonctionnel");

    public UserProfileBlocFonctionnelRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserProfileRowMapper userprofileMapper,
        BlocFonctionnelRowMapper blocfonctionnelMapper,
        UserProfileBlocFonctionnelRowMapper userprofileblocfonctionnelMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(UserProfileBlocFonctionnel.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userprofileMapper = userprofileMapper;
        this.blocfonctionnelMapper = blocfonctionnelMapper;
        this.userprofileblocfonctionnelMapper = userprofileblocfonctionnelMapper;
    }

    @Override
    public Flux<UserProfileBlocFonctionnel> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserProfileBlocFonctionnel> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserProfileBlocFonctionnelSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserProfileSqlHelper.getColumns(userProfilTable, "userProfil"));
        columns.addAll(BlocFonctionnelSqlHelper.getColumns(blocFonctionnelTable, "blocFonctionnel"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userProfilTable)
            .on(Column.create("user_profil_id", entityTable))
            .equals(Column.create("id", userProfilTable))
            .leftOuterJoin(blocFonctionnelTable)
            .on(Column.create("bloc_fonctionnel_id", entityTable))
            .equals(Column.create("id", blocFonctionnelTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserProfileBlocFonctionnel.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserProfileBlocFonctionnel> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserProfileBlocFonctionnel> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private UserProfileBlocFonctionnel process(Row row, RowMetadata metadata) {
        UserProfileBlocFonctionnel entity = userprofileblocfonctionnelMapper.apply(row, "e");
        entity.setUserProfil(userprofileMapper.apply(row, "userProfil"));
        entity.setBlocFonctionnel(blocfonctionnelMapper.apply(row, "blocFonctionnel"));
        return entity;
    }

    @Override
    public <S extends UserProfileBlocFonctionnel> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
