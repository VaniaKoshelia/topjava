package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal, int userId) {
        return service.create(meal, userId);
    }

    public void delete(Meal meal, int userId) {
        service.delete(meal.getId(), userId);
    }

    public Meal get(int id, int userId) {
        return service.get(id, userId);
    }

    public void update(Meal meal, int userId) {
        service.update(meal, userId);
    }

    public List<MealTo> getAll(int userId) { return MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()); }

}