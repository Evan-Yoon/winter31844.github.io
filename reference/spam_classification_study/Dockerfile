# 1. 파이썬 3.10이 설치된 가벼운 리눅스(Debian)에서 시작합니다.
FROM python:3.10-slim

# 2. 컨테이너 내부의 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 3. 라이브러리 설치를 위해 필요한 시스템 도구들을 설치합니다.
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# 4. 로컬의 requirements.txt를 컨테이너 안으로 복사합니다.
COPY requirements.txt .

# 5. 적힌 라이브러리들을 한꺼번에 설치합니다.
RUN pip install --no-cache-dir -r requirements.txt

# 6. 현재 폴더의 모든 파일(데이터, 코드 등)을 컨테이너 안으로 복사합니다.
COPY . .

# 7. 주피터 노트북이 사용할 포트(8888)를 열어줍니다.
EXPOSE 8888

# 8. 컨테이너가 시작되면 주피터 랩을 실행하도록 설정합니다.
CMD ["jupyter", "lab", "--ip=0.0.0.0", "--port=8888", "--no-browser", "--allow-root", "--NotebookApp.token=''"]