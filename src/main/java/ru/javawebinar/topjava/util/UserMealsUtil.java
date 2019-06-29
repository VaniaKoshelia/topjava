package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {

    private static Map<LocalDate, Integer> caloriesPerDays = new HashMap<>();
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        getFilteredWithExceededCycles(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000)
        .stream().forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededRecursion(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealsWithExceed = new ArrayList<>();
        caloriesPerDays.merge(mealList.get(0).getDateTime().toLocalDate(), mealList.get(0).getCalories(), (a, b) -> a + b);
        if(mealList.size() > 1) {
            mealsWithExceed.addAll(getFilteredWithExceededRecursion(mealList.subList(1, mealList.size()), startTime, endTime, caloriesPerDay));
        }

        if(TimeUtil.isBetween(mealList.get(0).getDateTime().toLocalTime(), startTime, endTime)) {
            mealsWithExceed.add(new UserMealWithExceed(mealList.get(0).getDateTime(),
                    mealList.get(0).getDescription(),
                    mealList.get(0).getCalories(),
                    new AtomicBoolean(caloriesPerDays.get(mealList.get(0).getDateTime().toLocalDate()) >= caloriesPerDay)));
        }
        return mealsWithExceed;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, AtomicBoolean> excludedMap = new HashMap<>();
        Map<LocalDate, Integer> caloriesPerDays = new HashMap<>();
        List<UserMealWithExceed> mealWithExceeds = new ArrayList<>();
        mealList.stream()
                .forEach(m -> {
                    AtomicBoolean excluded = excludedMap.computeIfAbsent(m.getDateTime().toLocalDate(), meal -> new AtomicBoolean());
                    int cal = caloriesPerDays.merge(m.getDateTime().toLocalDate(), m.getCalories(), Integer::sum);
                    if(cal > caloriesPerDay) {
                        excluded.set(true);
                    }
                    if(TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime)) {
                        mealWithExceeds.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), excluded));
                    }
                });
        return mealWithExceeds;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate()))
                .values().stream()
                .flatMap(userMeals -> {
                    AtomicBoolean exceeded = new AtomicBoolean(userMeals.stream().mapToInt(m -> m.getCalories()).sum() > caloriesPerDay);
                    return userMeals
                            .stream()
                            .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                            .map(m -> new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), exceeded));
                })
        .collect(Collectors.toList());
    }

}
