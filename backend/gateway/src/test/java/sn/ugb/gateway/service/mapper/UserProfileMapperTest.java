package sn.ugb.gateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserProfileMapperTest {

    private UserProfileMapper userProfileMapper;

    @BeforeEach
    public void setUp() {
        userProfileMapper = new UserProfileMapperImpl();
    }
}
