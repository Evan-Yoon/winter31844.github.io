class Calculator:
    def poweron(self):
        print("전원을 켭니다.")

    def poweroff(self):
        print("전원을 끕니다.")

    def plus(self, x, y):
        return x + y

    def divide(self, x, y):
        return x / y

myCal = Calculator()
myCal.poweron()
result1 = myCal.plus(5, 6)
print("result1:", result1)

x = 10
y = 4
result2 = myCal.divide(x, y)
print("result2:", result2)
myCal.poweroff()