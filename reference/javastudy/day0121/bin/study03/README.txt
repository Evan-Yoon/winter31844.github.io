<감정 분석 앱>

[프로젝트 개요]

이 프로젝트는 텍스트 데이터 수집부터 AI 모델 학습, 그리고 활용까지의 전 과정을 다루는
Full-Cycle 감성 분석 솔루션입니다. JavaFX를 이용한 직관적인 데이터 라벨링 도구(Data Labeling Tool)와,
Python Scikit-learn 기반의 감성 분석 인공지능(AI) 모델 학습 파트로 구성되어 있습니다.

[주요 기능 및 특징]

1. 데이터 라벨링 (JavaFX)
   - 사용자 친화적 UI: Google Gemini와 협업하여 가독성 높은 Card UI 및 감각적인 컬러 디자인 적용
   - 실시간 저장: 라벨링 결과가 파일에 즉시 기록되어 안정성 확보

2. 데이터 증강 및 AI 학습 (Python)
   - 문제 해결(Troubleshooting):
     기존 10,000개 데이터셋(`sentiment_10000_ko.csv`)으로 학습 시,
     '좋아요'와 같은 명백한 긍정 단어를 부정으로 예측하는 편향성 확인.
   - 데이터 생성(Data Generation):
     `make9999Sentence.py`를 제작하여 긍정/부정/중립 패턴을 가진 문장 10,000개를
     자동 생성(`generated_sentiment_9999.csv`).
   - 모델 재학습(Retraining):
     총 20,000개(`sentiment_20000_ko.csv`)로 데이터셋을 확장하여
     Google Colab 환경에서 재학습 진행 (`language_label.ipynb`).
   - 성능 개선: 데이터 불균형 해소 및 감성 예측 정확도 상승.

3. 모델 연동
   - 학습된 `model.pkl`과 `vectorizer.pkl`을 활용하여 실시간 문장 감성 예측 가능 (`emotionModelRunner.py`)


[파일 구성 (총 13개)]

1. Java Application (UI & Logic)
   - AppMain.java                : JavaFX 애플리케이션 실행 및 초기화
   - rootController.java         : 버튼 클릭 이벤트 핸들링 및 화면 전환 로직 제어
   - root.fxml                   : FXML 기반의 레이아웃 정의 파일
   - application.css             : UI 스타일 및 디자인 속성 정의
   - FileManager.java            : 데이터 파일 입출력(CRUD) 처리 핵심 클래스
   - Main.java / MainApp.java    : 데이터 테스트 및 디버깅용 실행 파일

2. AI & Data Processing
   - language_label.ipynb        : 데이터 전처리 및 모델 학습 (Google Colab)
   - make9999Sentence.py         : 데이터 증강을 위한 문장 생성 스크립트
   - emotionModelRunner.py       : 학습 모델 테스트용 실행 파일
   - model.pkl / vectorizer.pkl  : 학습된 AI 모델 및 벡터라이저 객체
   - sentiment_20000_ko.csv 등   : 학습용 원본/증강 데이터셋

[개발 환경 및 실행 방법]

1. Java 환경
   - JDK 11 이상, JavaFX 라이브러리 필요
   - 실행: AppMain.java Run

2. Python 환경
   - Python 3.x, Scikit-learn, Pandas, Joblib 필요
   - 모델 학습: Google Colab 또는 로컬 Jupyter Notebook에서 `language_label.ipynb` 실행
   - 예측 테스트: `python emotionModelRunner.py` 실행

[개발자 코멘트]

초기 모델이 긍정 단어('좋아요' 등)를 부정으로 오분류하는 문제를 발견하여,
Python 스크립트로 부족한 데이터를 직접 생성(Data Augmentation)하고
총 20,000개의 데이터셋으로 확장하여 모델 성능을 획기적으로 개선하였습니다.