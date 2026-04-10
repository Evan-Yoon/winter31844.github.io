import random

num = random.randint(1, 6)
match num:
    case 1:
        print("1번 나옴")
    case 2:
        print("2번 나옴")
    case 3:
        print("3번 나옴")
    case 4:
        print("4번 나옴")
    case 5:
        print("5번 나옴")
    case _: # default case
        print("6번 나옴")
