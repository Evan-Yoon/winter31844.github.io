---
title: 'HR 출신 개발자의 첫 자동화 도구: 비품 발주 자동화 스크립트'
slug: office-supply-manager-python-automation
date: 2026-03-22
author: Evan Yoon
category: project
subcategory: personal-project
description: 총무 업무에서 매주 반복되던 비품 재고 확인과 발주서 작성을 Python으로 자동화한 과정. 외부 라이브러리 없이 표준 라이브러리만으로
  실무 자동화를 구현했습니다.
tags:
  - python
  - automation
  - hr
  - csv
  - side-project
  - personal-project
  - tool-review
readTime: 7
featured: false
draft: false
toc: true
---

## 왜 만들었나: 총무 담당자의 현실

HR/총무 업무를 오래 하다 보면 반복 작업의 무게를 체감하게 된다. 그 중 하나가 **비품 재고 관리**다.

매주 직접 창고를 돌며 A4 용지는 몇 박스 남았는지, 볼펜이 얼마나 떨어졌는지 확인하고, 그 결과를 표에 입력한 뒤 발주 요청서를 만들어 결재를 올린다. 개당 5~10분짜리 단순 작업이지만, 품목이 많아지면 30분이 훌쩍 넘어간다. 게다가 수기로 옮기는 과정에서 숫자를 잘못 적거나 품목을 빠뜨리는 실수도 종종 발생한다.

개발 공부를 시작하고 나서 처음 든 생각이 바로 이거였다.

> "이 작업, 자동화할 수 있지 않을까?"

## 프로젝트 개요

**Office Supply Manager**는 CSV 파일로 관리되는 비품 재고를 읽어서, 최소 재고 기준 이하로 떨어진 품목을 자동으로 감지하고 발주 요청서 파일을 생성해주는 Python 스크립트다.

- **GitHub**: [Evan-Yoon/office-supply-manager](https://github.com/Evan-Yoon/office-supply-manager)
- **언어**: Python 3.x
- **외부 의존성**: 없음 (표준 라이브러리만 사용)

외부 패키지를 전혀 쓰지 않은 건 의도적인 선택이었다. 사무직 환경에서 Python 환경이 갖춰진 PC가 드물기 때문에, `pip install` 없이 바로 실행될 수 있어야 한다고 생각했다.

## 구조

```
office-supply-manager/
├── main.py            # 핵심 자동화 스크립트
├── inventory.csv      # 재고 데이터
└── 비품발주요청서_2026-03-22.txt  # 생성된 발주서 샘플
```

딱 하나의 Python 파일로 구성된 단순한 프로젝트다. 작은 도구일수록 진입 장벽이 낮아야 한다는 철학이 반영되어 있다.

## 핵심 로직

### 재고 데이터 구조

`inventory.csv`는 아래와 같은 구조로 관리된다.

```csv
품목명,현재수량,최소수량,단가
A4용지,3,10,15000
볼펜,5,20,500
포스트잇,2,10,3000
종이컵,50,100,8000
커피원두,1,3,25000
HDMI케이블,2,5,12000
```

각 컬럼의 의미:
- **현재수량**: 지금 창고에 있는 수량
- **최소수량**: 이 수준 이하면 발주가 필요하다고 간주하는 기준
- **단가**: 품목 단가 (발주 예상 비용 산출에 사용)

### 발주 대상 탐지

```python
import csv
from datetime import datetime

def check_inventory(filename):
    reorder_list = []
    with open(filename, encoding='utf-8-sig') as f:
        reader = csv.DictReader(f)
        for row in reader:
            current = int(row['현재수량'])
            minimum = int(row['최소수량'])
            if current < minimum:
                needed = minimum - current
                reorder_list.append({
                    '품목명': row['품목명'],
                    '현재수량': current,
                    '발주수량': needed,
                    '단가': int(row['단가']),
                    '예상금액': needed * int(row['단가'])
                })
    return reorder_list
```

핵심은 `current < minimum` 조건 하나다. 현재 수량이 최소 기준을 밑돌면 `minimum - current`만큼 발주 수량으로 계산해서 리스트에 담는다.

`utf-8-sig` 인코딩을 쓴 것도 실무적인 이유에서다. Excel로 저장된 CSV는 BOM(Byte Order Mark)이 붙어 있는 경우가 많아, 일반 `utf-8`로 읽으면 첫 번째 컬럼명에 이상한 문자가 붙는 문제가 생긴다.

### 발주서 자동 생성

```python
def generate_order_sheet(reorder_list):
    today = datetime.now().strftime('%Y-%m-%d')
    filename = f'비품발주요청서_{today}.txt'

    with open(filename, 'w', encoding='utf-8-sig') as f:
        f.write(f'비품 발주 요청서\n')
        f.write(f'작성일: {today}\n')
        f.write(f'부서: 총무팀\n')
        f.write('=' * 40 + '\n')

        total = 0
        for item in reorder_list:
            f.write(f"\n품목: {item['품목명']}\n")
            f.write(f"  현재 재고: {item['현재수량']}개\n")
            f.write(f"  발주 수량: {item['발주수량']}개\n")
            f.write(f"  예상 금액: {item['예상금액']:,}원\n")
            total += item['예상금액']

        f.write('\n' + '=' * 40 + '\n')
        f.write(f'총 예상 금액: {total:,}원\n')

    return filename
```

실행하면 `비품발주요청서_2026-03-22.txt` 같은 이름으로 파일이 생성된다. 날짜가 파일명에 자동으로 들어가기 때문에 기록 관리도 된다. 금액에 `:,` 포맷을 적용해서 `15,000원` 형태로 출력되도록 했다.

## 실제로 어떻게 쓰이나

사용 흐름은 이렇다:

1. 창고 실사 후 `inventory.csv`의 **현재수량** 컬럼만 업데이트
2. `python main.py` 실행
3. 생성된 `.txt` 파일을 결재 시스템에 첨부하거나 메신저로 공유

기존에 수작업으로 Excel을 만들고 계산기를 두드리던 과정이, CSV 수정 후 커맨드 하나로 끝난다. 총무 담당자가 Python을 몰라도 CSV만 수정할 줄 알면 된다.

## 만들면서 배운 것

### 1. 실무 자동화의 첫 번째 조건은 "낮은 진입 장벽"

아무리 좋은 도구를 만들어도 쓰는 사람이 어려움을 느끼면 의미가 없다. 총무 담당자가 `pip install`을 알아야 한다는 조건이 붙는 순간 이 도구의 실용성은 크게 떨어진다. 표준 라이브러리만 쓴 결정은 이 관점에서 가장 중요한 설계 선택이었다.

### 2. CSV는 생각보다 강력하다

Excel 파일을 직접 다루는 `openpyxl` 같은 라이브러리를 쓰지 않고도 CSV 하나로 충분했다. 실무에서 데이터를 다루는 첫 번째 선택지로 CSV가 여전히 유효하다는 걸 다시 확인했다.

### 3. 코드보다 데이터 구조가 먼저다

스크립트 자체는 단순하다. 하지만 `inventory.csv`의 컬럼 설계를 어떻게 하느냐에 따라 코드의 복잡도가 크게 달라진다. 데이터를 잘 정의해두면 코드는 자연스럽게 단순해진다.

## 마무리

이 프로젝트는 거창한 기술 스택도, 화려한 UI도 없다. Python 파일 하나, CSV 파일 하나, 그리고 텍스트 출력이 전부다.

하지만 "내가 겪었던 비효율을 해결한다"는 목적이 명확했기 때문에 완성도 있는 결과물이 나올 수 있었다. HR/총무 업무 경험이 없었다면 어떤 데이터가 필요하고 어떤 출력이 의미 있는지를 설계하지 못했을 것이다.

도메인 지식과 개발 능력이 만났을 때 진짜 쓸모 있는 도구가 나온다. 그 사실을 작은 스크립트 하나로 다시 한번 확인했다. 비품 관리처럼 작아 보이는 업무도 방식이 통일되면 재고 부족, 중복 구매, 요청 누락 같은 운영 피로를 줄일 수 있다는 점에서 이런 자동화의 가치가 분명했다.
