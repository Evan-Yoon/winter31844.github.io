ssn = "940608-1234567"
gender = ssn[7];

match gender:
    case '1' | '3':
        print("남자")
    case '2' | '4':
        print("여자")
    case _:
        print("잘못된 주민등록번호")