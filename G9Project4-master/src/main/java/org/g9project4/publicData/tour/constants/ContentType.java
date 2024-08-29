package org.g9project4.publicData.tour.constants;

import java.util.List;

public enum ContentType {
    TourSpot(12L, "관광지", "spot"),
    CultureFacility(14L, "문화시설", "culture"),
    Festival(15L, "행사/공연/축제", "festival"),
    TourCourse(25L, "여행코스", "course"),
    Leports(28L, "레포츠", "leports"),
    Accommodation(32L, "숙박", "stay"),
    Shopping(38L, "쇼핑", "shopping"),
    Restaurant(39L, "음식점", "restaurant"),
    GreenTour(1L, "생태", "green");

    private final long id;
    private final String type;
    private final String name;

    ContentType(long id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static List<ContentType> getList() {
        return List.of(TourSpot, CultureFacility, Festival, TourCourse, Leports, Accommodation, Shopping, Restaurant, GreenTour);
    }
    //public static String getName
}
