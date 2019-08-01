package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(LUNCH_ID, USER_ID);
        assertEquals(meal, LUNCH);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(LUNCH_ID, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(LUNCH_ID, USER_ID);
        assertEquals(service.getAll(USER_ID), Collections.emptyList());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(LUNCH_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> userMeals = service.getBetweenDateTimes(LocalDateTime.MIN, LUNCH_DATE_TIME, USER_ID);
        assertEquals(userMeals, Arrays.asList(LUNCH));
    }

    @Test
    public void getAll() {
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertEquals(adminMeals, Arrays.asList(BREAKFAST));
    }

    @Test
    public void update() {
        Meal updated = new Meal(LUNCH);
        updated.setCalories(100);
        updated.setDescription("test");
        service.update(updated, USER_ID);
        assertEquals(service.get(LUNCH_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = new Meal(LUNCH);
        updated.setCalories(100);
        updated.setDescription("test");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal created = new Meal(null, LocalDateTime.now(), "test", 300);
        service.create(created, USER_ID);
        assertEquals(service.getAll(USER_ID), Arrays.asList(LUNCH, created));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateMealCreate() throws Exception {
        service.create(new Meal(null, LUNCH_DATE_TIME, "test", 0), USER_ID);
    }
}