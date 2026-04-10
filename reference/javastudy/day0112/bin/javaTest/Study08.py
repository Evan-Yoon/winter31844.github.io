class Calculator:
    def area_circle(self, r: float) -> float: # ->는 반환형 표기
        print("Calculator 객체의 area_circle() 실행")
        return 3.14159 * r * r

import math

class Computer(Calculator):
    def area_circle(self, r: float) -> float:
        print("Computer 객체의 area_circle() 실행")
        return math.pi * r * r

r = 10

calculator = Calculator();
print("원 면적:", calculator.area_circle(r))

computer = Computer();
print("원 면적:", computer.area_circle(r))