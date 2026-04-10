#아이디는 5글자 이상이어야 합니다.
#아이디는 50글자를 넘어가면 안됩니다.
#비밀번호는 '!'를 포함해야 합니다.
#제약사항: "모든 조건을 통과해야하만 '가입 성공'이 출력되도록 if 안애 if 를 계속 넣어서 작성해보세요."
#훗날 쉽게 수정이 가능하도록 리팩토링이 깔끔하게 잘 되어야 하고, 누구나 쉽게 이해 가능한 코드가 되어야 합니다.

def register_user(user_id, password):
    MIN_ID_LENGTH = 5
    MAX_ID_LENGTH = 50
    REQUIRED_PWD_CHAR = '!'

    if len(user_id) >= MIN_ID_LENGTH:
        if len(user_id) <= MAX_ID_LENGTH:
            if REQUIRED_PWD_CHAR in password:
                print("가입 성공")
            else:
                print("비밀번호에 '!'를 포함해야 합니다.")
        else:
            print("아이디는 50글자를 넘어가면 안됩니다.")
    else:
        print("아이디는 5글자 이상이어야 합니다.")

#이번에는 추가사항으로 비밀번호는 8글자 이상이면서 최소 하나 숫자도 포함해야 합니다. 암호와 아이디가 동일하면 안됩니다.
def register_user_updated(user_id, password):
    MIN_ID_LENGTH = 5
    MAX_ID_LENGTH = 50
    MIN_PWD_LENGTH = 8
    REQUIRED_PWD_CHAR = '!'

    if len(user_id) >= MIN_ID_LENGTH:
        if len(user_id) <= MAX_ID_LENGTH:
            if user_id != password:
                if len(password) >= MIN_PWD_LENGTH:
                    if REQUIRED_PWD_CHAR in password:
                        if any(char.isdigit() for char in password):
                            print("가입 성공")
                        else:
                            print("비밀번호에 최소 하나의 숫자가 포함되어야 합니다.")
                    else:
                        print("비밀번호에 '!'를 포함해야 합니다.")
                else:
                    print("비밀번호는 8글자 이상이어야 합니다.")
            else:
                print("암호와 아이디가 동일하면 안됩니다.")
        else:
            print("아이디는 50글자를 넘어가면 안됩니다.")
    else:
        print("아이디는 5글자 이상이어야 합니다.")

#guard clause
def register_user_guard_clause(user_id, password):
    MIN_ID_LENGTH = 5
    MAX_ID_LENGTH = 50
    MIN_PWD_LENGTH = 8
    REQUIRED_PWD_CHAR = '!'

    if len(user_id) < MIN_ID_LENGTH:
        print("아이디는 5글자 이상이어야 합니다.")
        return

    if len(user_id) > MAX_ID_LENGTH:
        print("아이디는 50글자를 넘어가면 안됩니다.")
        return

    if user_id == password:
        print("암호와 아이디가 동일하면 안됩니다.")
        return

    if len(password) < MIN_PWD_LENGTH:
        print("비밀번호는 8글자 이상이어야 합니다.")
        return

    if REQUIRED_PWD_CHAR not in password:
        print("비밀번호에 '!'를 포함해야 합니다.")
        return

    if not any(char.isdigit() for char in password):
        print("비밀번호에 최소 하나의 숫자가 포함되어야 합니다.")
        return

    print("가입 성공")

#여기에 또 추가해줘. 추가사항: 아이디는 이메일 형식으로'@'가 있어야 합니다. 가입 실패 시 아이디 때문인지 비밀번호 때문인지 알려줘야 합니다.
def register_user_final(user_id, password):
    MIN_ID_LENGTH = 5
    MAX_ID_LENGTH = 50
    MIN_PWD_LENGTH = 8
    REQUIRED_PWD_CHAR = '!'

    # 아이디 검증
    if len(user_id) < MIN_ID_LENGTH:
        print("아이디 오류: 아이디는 5글자 이상이어야 합니다.")
        return

    if len(user_id) > MAX_ID_LENGTH:
        print("아이디 오류: 아이디는 50글자를 넘어가면 안됩니다.")
        return

    if '@' not in user_id:
        print("아이디 오류: 아이디는 이메일 형식으로 '@'를 포함해야 합니다.")
        return

    # 비밀번호 검증
    if user_id == password:
        print("비밀번호 오류: 암호와 아이디가 동일하면 안됩니다.")
        return

    if len(password) < MIN_PWD_LENGTH:
        print("비밀번호 오류: 비밀번호는 8글자 이상이어야 합니다.")
        return

    if REQUIRED_PWD_CHAR not in password:
        print("비밀번호에 '!'를 포함해야 합니다.")
        return

    if not any(char.isdigit() for char in password):
        print("비밀번호에 최소 하나의 숫자가 포함되어야 합니다.")
        return

    print("가입 성공")

#extract method로 함수 추출
def validate_id(user_id):
    MIN_ID_LENGTH = 5
    MAX_ID_LENGTH = 50
    if len(user_id) < MIN_ID_LENGTH:
        print("아이디 오류: 아이디는 5글자 이상이어야 합니다.")
        return False
    if len(user_id) > MAX_ID_LENGTH:
        print("아이디 오류: 아이디는 50글자를 넘어가면 안됩니다.")
        return False
    if '@' not in user_id:
        print("아이디 오류: 아이디는 이메일 형식으로 '@'를 포함해야 합니다.")
        return False
    return True

def validate_password(user_id, password):
    MIN_PWD_LENGTH = 8
    REQUIRED_PWD_CHAR = '!'
    if user_id == password:
        print("비밀번호 오류: 암호와 아이디가 동일하면 안됩니다.")
        return False
    if len(password) < MIN_PWD_LENGTH:
        print("비밀번호 오류: 비밀번호는 8글자 이상이어야 합니다.")
        return False
    if REQUIRED_PWD_CHAR not in password:
        print("비밀번호 오류: 비밀번호에 '!'를 포함해야 합니다.")
        return False
    if not any(char.isdigit() for char in password):
        print("비밀번호 오류: 비밀번호에 최소 하나의 숫자가 포함되어야 합니다.")
        return False
    return True

def register_user_extracted(user_id, password):
    if validate_id(user_id) and validate_password(user_id, password):
        print("가입 성공")