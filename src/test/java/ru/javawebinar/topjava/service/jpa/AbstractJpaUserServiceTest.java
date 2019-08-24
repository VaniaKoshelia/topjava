package ru.javawebinar.topjava.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    protected JpaUtil jpaUtil;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        jpaUtil.clear2ndLevelHibernateCache();
    }
}
