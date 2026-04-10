season = ["spring", "summer", "fall", "winter"];

print("seaon[0] : ", season[0]);
print("seaon[1] : ", season[1]);
print("seaon[2] : ", season[2]);
print("seaon[3] : ", season[3]);

season[1] = "여름";
print("season[1] : ", season[1]);

scores = [83, 90, 77];
sum = 0;
for i in range(3):
    sum += scores[i]
print("총합 : ", sum);
avg = sum / 3;
print("평균 : ", avg);