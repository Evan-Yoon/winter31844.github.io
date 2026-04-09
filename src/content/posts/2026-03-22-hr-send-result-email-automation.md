---
title: '채용 결과 메일 자동으로 보내기 — Python SMTP'
slug: hr-send-result-email-automation
date: 2026-03-22
author: Evan Yoon
category: project
subcategory: personal-project
description: HR 담당자가 지원자 100명에게 합격/불합격 메일을 일일이 보내던 2시간짜리 작업을 Python SMTP 자동화로 1분으로
  줄인 프로젝트.
tags:
  - python
  - smtp
  - automation
  - hr
  - rpa
  - side-project
  - personal-project
  - tool-review
readTime: 8
featured: false
draft: false
toc: true
---

## 채용 담당자의 하루

공채 서류 전형이 끝나면 채용 담당자에게는 꽤 번거로운 일이 기다린다. 지원자 한 명 한 명에게 결과를 통보하는 메일을 보내는 일이다.

합격자에게는 다음 전형 안내를, 불합격자에게는 정중한 탈락 안내를 보낸다. 형식은 정해져 있지만 이름을 바꾸고, 결과 문구를 바꾸고, 주소를 복사하고, 발송 버튼을 누르는 과정을 지원자 수만큼 반복해야 한다. 지원자가 50명이면 50번, 100명이면 100번.

채용 담당 경험이 있는 입장에서 이 작업이 얼마나 소모적인지 잘 알고 있었다. 그래서 만들었다.

## 프로젝트 개요

**hr-send-result**는 CSV 파일에 담긴 지원자 명단을 읽어, 합격/불합격 결과에 따라 개인화된 이메일을 자동으로 일괄 발송하는 Python 자동화 스크립트다.

- **GitHub**: [Evan-Yoon/hr-send-result](https://github.com/Evan-Yoon/hr-send-result)
- **언어**: Python 3.x
- **이메일 서버**: 네이버 SMTP (port 587, TLS)
- **외부 의존성**: `python-dotenv` 하나

## 구조

```
hr-send-result/
├── email_automation.py   # 핵심 자동화 스크립트
├── .env                  # 발신자 이메일 / 앱 비밀번호 (버전 관리 제외)
├── applicants.csv        # 지원자 명단 (버전 관리 제외)
└── README.md
```

민감한 파일인 `.env`와 `applicants.csv`는 `.gitignore`에 포함시켜 절대 저장소에 올라가지 않도록 했다.

## 핵심 로직

### 지원자 데이터 구조

`applicants.csv`는 세 개의 컬럼으로 구성된다.

```csv
name,email,result
에반,wlgus1805@naver.com,합격
김지원,jiwon@example.com,불합격
이민준,minjun@example.com,합격
```

`result` 컬럼의 값이 `합격`인지 아닌지에 따라 발송되는 메일 내용이 달라진다.

### SMTP 연결 및 메일 발송

```python
import smtplib
import csv
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from dotenv import load_dotenv
import os

load_dotenv()
SENDER_EMAIL = os.getenv("SENDER_EMAIL")
APP_PASSWORD = os.getenv("APP_PASSWORD")

def send_result_emails():
    with smtplib.SMTP("smtp.naver.com", 587) as server:
        server.starttls()
        server.login(SENDER_EMAIL, APP_PASSWORD)

        with open("applicants.csv", encoding="utf-8-sig") as f:
            reader = csv.DictReader(f)
            for row in reader:
                name = row["name"]
                email = row["email"]
                result = row["result"]

                subject, body = build_message(name, result)

                msg = MIMEMultipart()
                msg["From"] = SENDER_EMAIL
                msg["To"] = email
                msg["Subject"] = subject
                msg.attach(MIMEText(body, "plain", "utf-8"))

                server.sendmail(SENDER_EMAIL, email, msg.as_string())
                print(f"[발송 완료] {name} ({email}) - {result}")
```

`smtplib.SMTP`를 `with` 문으로 열면 예외 발생 시에도 연결이 자동으로 닫힌다. `starttls()`로 TLS 암호화를 시작한 뒤 로그인하고, CSV를 한 줄씩 읽으며 메일을 발송한다.

### 합격/불합격 메시지 분기

```python
def build_message(name, result):
    if result == "합격":
        subject = f"[제로웍스] {name}(합격)님, 서류 전형 합격을 축하드립니다."
        body = f"""안녕하세요 {name}(합격)님,

제로웍스 채용 전형에 지원해 주셔서 진심으로 감사드리며,
서류 전형 합격 소식을 전해드립니다. 축하합니다!

보내주신 소중한 지원서를 통해 {name}(합격)님이 갖추신 훌륭한 역량과 잠재력을 확인할 수 있어
아주 기쁜 마음으로 다음 전형에 모시게 되었습니다.
이어지는 면접 전형과 관련된 세부 일정 및 안내 사항은 빠른 시일 내에 별도로 안내 드릴 예정입니다.

다시 한번 제로웍스에 보여주신 관심에 깊이 감사드리며,
면접에서 뵙고 더 많은 이야기를 나눌 수 있기를 기대하겠습니다.

감사합니다.
제로웍스 경영지원팀 드림"""
    else:
        subject = f"[제로웍스] {name}님, 서류 전형 결과 안내"
        body = f"""안녕하세요 {name}님,

제로웍스 채용 전형에 지원해 주셔서 진심으로 감사드립니다.

아쉽게도 이번 서류 전형에서 함께하지 못하게 되었습니다.
귀하의 앞날에 좋은 일들이 가득하기를 진심으로 바랍니다.

감사합니다.
제로웍스 경영지원팀 드림"""

    return subject, body
```

f-string으로 이름을 동적으로 삽입해 개인화된 메시지를 만든다. 템플릿이 코드 안에 있어서 수정도 간편하다.

## 보안 설계

메일 자동화 도구에서 가장 신경 써야 하는 부분이 **자격증명 관리**다.

네이버 계정 비밀번호를 코드에 직접 넣는 것은 매우 위험하다. 코드가 GitHub에 올라가는 순간 계정이 탈취될 수 있다. 이를 방지하기 위해 두 가지 조치를 취했다.

**1. 앱 비밀번호 사용**

네이버 2단계 인증을 활성화한 뒤 발급받은 16자리 앱 전용 비밀번호를 사용한다. 실제 계정 비밀번호와 완전히 분리되기 때문에, 이 비밀번호가 유출되더라도 계정 자체는 안전하다.

**2. `.env` 파일로 분리**

```
SENDER_EMAIL=your_email@naver.com
APP_PASSWORD=xxxxxxxxxxxxxxxx
```

자격증명은 `.env` 파일에만 존재하고, `.gitignore`로 버전 관리에서 제외했다. 코드 어디에도 실제 이메일 주소나 비밀번호가 등장하지 않는다.

## 실제 동작 확인

직접 테스트로 내 이메일에 발송해봤다. 지원자 명단에 내 이름과 이메일을 합격 처리로 입력하고 스크립트를 실행한 결과다.

![실제 수신된 합격 통보 메일](/images/posts/hr-send-result/test-email.png)

메일 제목, 이름 치환, 본문 내용 모두 의도한 대로 출력됐다. 실제 채용 담당자가 보내는 메일과 구별이 안 될 정도로 자연스럽게 개인화된다.

## 에러 핸들링

```python
try:
    send_result_emails()
except FileNotFoundError:
    print("[오류] applicants.csv 파일을 찾을 수 없습니다.")
except smtplib.SMTPAuthenticationError:
    print("[오류] 이메일 인증에 실패했습니다. .env 파일을 확인해주세요.")
except Exception as e:
    print(f"[오류] 예상치 못한 오류가 발생했습니다: {e}")
```

CSV 파일 누락, 인증 실패, 그 외 예외 상황을 나눠서 처리했다. 특히 `SMTPAuthenticationError`를 별도로 잡아서 `.env` 파일 확인을 유도하도록 했다. 처음 세팅할 때 앱 비밀번호 오타로 막히는 경우가 많기 때문이다.

## 전후 비교

| 항목 | 수동 발송 | 자동화 후 |
|------|----------|----------|
| 지원자 100명 기준 소요 시간 | 약 2시간 | 약 1분 |
| 오탈자 발생 가능성 | 높음 (반복 복붙) | 없음 (템플릿 고정) |
| 발송 누락 가능성 | 있음 | 없음 (CSV 전체 순회) |
| 담당자 집중력 소모 | 매우 높음 | 없음 |

## 확장 가능성

현재 CSV + 네이버 SMTP 조합이지만 구조를 크게 바꾸지 않고 아래처럼 확장할 수 있다.

- **발신 서버 교체**: Gmail, Outlook 등 다른 SMTP 서버로 변경 가능
- **HTML 템플릿**: `MIMEText("plain")` → `MIMEText("html")`로 바꾸면 디자인된 메일 발송 가능
- **데이터 소스 교체**: CSV → Google Sheets API 또는 DB 연동으로 업그레이드 가능
- **첨부파일 추가**: `MIMEBase`를 이용해 안내문 PDF 첨부 가능

## 마무리

이 프로젝트를 만들면서 가장 크게 느낀 건 **도메인 지식이 자동화의 방향을 결정한다**는 점이다.

"합격/불합격 메일을 보낸다"는 단순한 요구사항 안에는 여러 디테일이 숨어있다. 이름을 어떻게 치환할지, 메시지 톤을 어떻게 맞출지, 어떤 정보가 CSV에 필요한지. 이런 것들은 채용 업무를 직접 해본 사람만이 제대로 설계할 수 있다.

개발 공부를 시작하며 "내가 아는 업무를 코드로 풀면 어떻게 될까"라는 질문을 계속 던지고 있다. 이 프로젝트는 그 질문에 대한 두 번째 답이다. 특히 이런 자동화는 시간을 줄이는 것만큼이나 발송 누락, 이름 오기입, 템플릿 실수처럼 반복 업무에서 자주 생기는 운영 리스크를 줄여준다는 점에서 의미가 있었다.
