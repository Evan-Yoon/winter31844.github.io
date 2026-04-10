package javaStudy;

public class Study07 {
    public static void main(String[] args) {
        int apple = 1;
        double pieceUnit = 0.1;
        int number = 7;

        double result = apple - number * pieceUnit;
        System.out.println("사과 1개에서 남은 양: " + result); // 0.3 / 예상은 0.3이지만 실제로는 0.29999999999999993
        // 실수는 2진수로 정확히 표현되지 않는 경우가 있어 오차가 발생할 수 있음

        //해결 방법 1: BigDecimal 클래스 사용
        /*BigDecimal appleBD = new BigDecimal("1");
        BigDecimal pieceUnitBD = new BigDecimal("0.1");
        BigDecimal numberBD = new BigDecimal("7");
        BigDecimal resultBD = appleBD.subtract(numberBD.multiply(pieceUnitBD));
        System.out.println("사과 1개에서 남은 양 (BigDecimal): " + resultBD);*/
        
        //해결 방법 2: 정수로 계산
        int intPieceUnit = 1; // 0.1을 10배한 값
        int intApple = apple * 10; // 1을 10배한 값
        int intResult = intApple - number * intPieceUnit;
        System.out.println("사과 1개에서 남은 양 (정수 계산): " + (double)intResult / 10);

        //해결 방법 3: 반올림
        double roundedResult = Math.round(result * 10) / 10.0; // 소수점 첫째 자리에서 반올림
        System.out.println("사과 1개에서 남은 양 (반올림): " + roundedResult);
        
        //해결 방법 4: 소수점 자리수 제한
        System.out.printf("사과 1개에서 남은 양 (소수점 자리수 제한): %.1f%n", result);

        //실수 계산 시 오차에 주의하고, 필요에 따라 적절한 해결 방법을 선택해야 함
        //특히 금융 계산에서는 BigDecimal 사용 권장
        //참고: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html
        //참고: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Math.html#round(double)
        //참고: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/PrintStream.html#printf(java.lang.String,java.lang.Object...)
    }