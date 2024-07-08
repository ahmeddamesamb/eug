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
import sn.ugb.gateway.domain.UserProfile;
import sn.ugb.gateway.repository.rowmapper.InfosUserRowMapper;
import sn.ugb.gateway.repository.rowmapper.ProfilRowMapper;
import sn.ugb.gateway.repository.rowmapper.UserProfileRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the UserProfile entity.
 */
@SuppressWarnings("unused")
class UserProfileRepositoryInternalImpl extends SimpleR2dbcRepository<UserProfile, Long> implements UserProfileRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProfilRowMapper profilMapper;
    private final InfosUserRowMapper infosuserMapper;
    private final UserProfileRowMapper userprofileMapper;

    private static final Table entityTable = Table.aliased("user_profile", EntityManager.ENTITY_ALIAS);
    private static final Table profilTable = Table.aliased("profil", "profil");
    private static final Table infoUserTable = Table.aliased("infos_user", "infoUser");

    public UserProfileRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProfilRowMapper profilMapper,
        InfosUserRowMapper infosuserMapper,
        UserProfileRowMapper userprofileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserProfile.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.profilMapper = profilMapper;
        this.infosuserMapper = infosuserMapper;
        this.userprofileMapper = userprofileMapper;
    }

    @Override
    public Flux<UserProfile> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserProfile> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserProfileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProfilSqlHelper.getColumns(profilTable, "profil"));
        columns.addAll(InfosUserSqlHelper.getColumns(infoUserTable, "infoUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(profilTable)
            .on(Column.create("profil_id", entityTable))
            .equals(Column.create("id", profilTable))
            .leftOuterJoin(infoUserTable)
            .on(Column.create("info_user_id", entityTable))
            .equals(Column.create("id", infoUserTable));
        String select = entityManager.createSelect(selectFrom, UserProfile.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserProfile> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserProfile> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<UserProfile> findAllByProfilId(Long profilId, Pageable pageable) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("profil_id"), Conditions.just(profilId.toString()));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<Long> countByUserProfileId(Long userProfileId) {
        return null;
    }


    @Override
    public Flux<UserProfile> findAllByInfoUserId(Long infoUserId, Pageable pageable) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("info_user_id"), Conditions.just(infoUserId.toString()));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Flux<UserProfile> findAllByEnCoursYN(Boolean enCoursYN, Pageable pageable) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("en_cours_yn"), Conditions.just(enCoursYN.toString()));
        return createQuery(pageable, whereClause).all();
    }



    @Override
    public Mono<Long> countByProfilId(Long profilId) {
        return db.sql("SELECT COUNT(*) FROM user_profile WHERE profil_id = :profilId")
            .bind("profilId", profilId)
            .map((row, rowMetadata) -> row.get(0, Long.class))
            .one();
    }

    @Override
    public Mono<Long> countByInfoUserId(Long infoUserId) {
        return db.sql("SELECT COUNT(*) FROM user_profile WHERE info_user_id = :infoUserId")
            .bind("infoUserId", infoUserId) // Vérifiez que "infoUserId" est correctement lié ici
            .map((row, rowMetadata) -> row.get(0, Long.class))
            .one();
    }

    @Override
    public Mono<Long> countByEncoursYN(Boolean enCoursYN) {
        return db.sql("SELECT COUNT(*) FROM user_profile WHERE en_cours_yn = :enCoursYN")
            .bind("enCoursYN", enCoursYN)
            .map((row, rowMetadata) -> row.get(0, Long.class))
            .one();
    }

    private UserProfile process(Row row, RowMetadata metadata) {
        UserProfile entity = userprofileMapper.apply(row, "e");
        entity.setProfil(profilMapper.apply(row, "profil"));
        entity.setInfoUser(infosuserMapper.apply(row, "infoUser"));
        return entity;
    }

    @Override
    public <S extends UserProfile> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
/*


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
import sn.ugb.gateway.domain.UserProfile;
import sn.ugb.gateway.repository.rowmapper.InfosUserRowMapper;
import sn.ugb.gateway.repository.rowmapper.ProfilRowMapper;
import sn.ugb.gateway.repository.rowmapper.UserProfileRowMapper;

*/
/**
 * Spring Data R2DBC custom repository implementation for the UserProfile entity.
 *//*

@SuppressWarnings("unused")
class UserProfileRepositoryInternalImpl extends SimpleR2dbcRepository<UserProfile, Long> implements UserProfileRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProfilRowMapper profilMapper;
    private final InfosUserRowMapper infosuserMapper;
    private final UserProfileRowMapper userprofileMapper;

    private static final Table entityTable = Table.aliased("user_profile", EntityManager.ENTITY_ALIAS);
    private static final Table profilTable = Table.aliased("profil", "profil");
    private static final Table infoUserTable = Table.aliased("infos_user", "infoUser");

    public UserProfileRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProfilRowMapper profilMapper,
        InfosUserRowMapper infosuserMapper,
        UserProfileRowMapper userprofileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UserProfile.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.profilMapper = profilMapper;
        this.infosuserMapper = infosuserMapper;
        this.userprofileMapper = userprofileMapper;
    }

    @Override
    public Flux<UserProfile> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UserProfile> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserProfileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProfilSqlHelper.getColumns(profilTable, "profil"));
        columns.addAll(InfosUserSqlHelper.getColumns(infoUserTable, "infoUser"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(profilTable)
            .on(Column.create("profil_id", entityTable))
            .equals(Column.create("id", profilTable))
            .leftOuterJoin(infoUserTable)
            .on(Column.create("info_user_id", entityTable))
            .equals(Column.create("id", infoUserTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UserProfile.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UserProfile> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UserProfile> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<UserProfile> findAllByProfilId(Long profilId, Pageable pageable) {
        return null;
    }

    @Override
    public Flux<UserProfile> findAllByInfoUserId(Long infoUserId, Pageable pageable) {
        return null;
    }

    @Override
    public Flux<UserProfile> findAllByEnCoursYN(Boolean enCoursYN, Pageable pageable) {
        return null;
    }

    private UserProfile process(Row row, RowMetadata metadata) {
        UserProfile entity = userprofileMapper.apply(row, "e");
        entity.setProfil(profilMapper.apply(row, "profil"));
        entity.setInfoUser(infosuserMapper.apply(row, "infoUser"));
        return entity;
    }

    @Override
    public <S extends UserProfile> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
*/
