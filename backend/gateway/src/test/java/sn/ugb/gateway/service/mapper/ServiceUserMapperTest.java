package sn.ugb.gateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ServiceUserMapperTest {

    private ServiceUserMapper serviceUserMapper;

    @BeforeEach
    public void setUp() {
        serviceUserMapper = new ServiceUserMapperImpl();
    }
}
