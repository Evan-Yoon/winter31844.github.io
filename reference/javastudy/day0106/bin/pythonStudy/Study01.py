value = 1000

print(f"가격 : {value}원"); #기본 출력
print(f"가격 : {value:6d}원"); # 전체 자리수 6자리 확보, 오른쪽 정렬
print(f"가격 : {value:<6d}원"); # 전체 자리수 6자리 확보, 왼쪽 정렬
print(f"가격 : {value:06d}원"); # 전체 자리수 6자리 확보, 빈자리 0으로 채우기

area = 3.14159265358979 * 10 * 10
r = 10
print("반지름이 {r}인 원의 넓이: {area:10.2f}"); 

name = "이재명"
job = "대통령"
print(f"{1:6d} | {name:<10} | {job:>10}");