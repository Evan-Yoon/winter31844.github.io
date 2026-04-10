def printItem(scores):
    for i in range(3):
        print(f"score[{i}] = {scores[i]}")

scores = [88, 95, 77];
sum1 = 0
for i in range(3):
    sum1 += scores[i]

print("총합 : ", sum1)

printItem(scores)