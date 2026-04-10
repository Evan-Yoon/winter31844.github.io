subject = "자바 프로그래밍";

location = subject.find("프로그래밍");
substring = subject[location:];
print(substring);

location = subject.find("자바");
if location != -1:
    print("자바와 관련된 책.")
else :
    print("자바와 관련 없는 책.")

result = ("파이썬" in subject);
if result :
    print("자바와 관련된 책.")
else :
    print("자바와 관련 없는 책.") # in 연산자 사용