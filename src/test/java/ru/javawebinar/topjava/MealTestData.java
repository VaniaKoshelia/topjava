package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int LUNCH_ID = START_SEQ + 2;
    public static final int BREAKFAST_ID = START_SEQ + 3;

    public static final LocalDateTime LUNCH_DATE_TIME = LocalDateTime.of(2015, Month.MAY, 30, 10, 0);
    public static final LocalDateTime BREAKFAST_DATE_TIME = LocalDateTime.of(2015, Month.MAY, 30, 11, 0);

    public static final Meal LUNCH = new Meal(LUNCH_ID, LUNCH_DATE_TIME, "Lunch", 500);
    public static final Meal BREAKFAST = new Meal(BREAKFAST_ID, BREAKFAST_DATE_TIME, "Breakfast", 500);

}
