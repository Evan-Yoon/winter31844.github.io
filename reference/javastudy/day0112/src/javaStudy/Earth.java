package javaStudy;

public class Earth {
    static final double EARTH_RADIUS = 6400; // 지구 반지름 / 상수는 대문자로 작성

    static final double EARTH_SURFACE_AREA; // 지구 표면적

    static {
        EARTH_SURFACE_AREA = 4 * Math.PI * EARTH_RADIUS * EARTH_RADIUS;
    } // static은 처음부터 컴파일할때 데이터를 불러온다.
}
