# import warnings

# from scipy import io
# warnings.filterwarnings("ignore")

# import sys
# import joblib

# MODEL_PATH = "src\\study03\\model.pkl";
# VECTORIZER_PATH = "src\\study03\\vectorizer.pkl";

# model = joblib.load(MODEL_PATH);
# vectorizer = joblib.load(VECTORIZER_PATH);

# text = sys.stdin.readline();
# if not text:
#     print("ERR_NO_INPUT");
#     sys.exit(2);

# x = vectorizer.transform([text]);
# pred = model.predict(x)[0];

# print(int(pred));

import sys
import io
import warnings

# 경고 무시
warnings.filterwarnings("ignore")

# 표준 입출력 UTF-8 설정
sys.stdin = io.TextIOWrapper(sys.stdin.detach(), encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8') # reconfigure로도 인코딩을 설정할 수 있지만, 위 방법이 더 호환성이 좋음

import joblib

# 경로 설정 (필요시 절대경로로 수정)
MODEL_PATH = "src/study03/model.pkl"
VECTORIZER_PATH = "src/study03/vectorizer.pkl"

try:
    model = joblib.load(MODEL_PATH)
    vectorizer = joblib.load(VECTORIZER_PATH)
except FileNotFoundError:
    print("DEBUG: 파일 경로를 찾을 수 없습니다.")
    sys.exit(1)

# 입력 받기
text = sys.stdin.readline().strip()
sys.stdout.flush()

if not text:
    sys.exit(0)

# 예측
x = vectorizer.transform([text])
pred = model.predict(x)[0]

print(int(pred))