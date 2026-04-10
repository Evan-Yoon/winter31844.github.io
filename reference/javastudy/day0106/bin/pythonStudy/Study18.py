grade = 'B';

match grade:
    case 'A' | 'a':
        print("우수 회원")
    case 'B' | 'b':
        print("일반 회원")
    case _:
        print("손님")