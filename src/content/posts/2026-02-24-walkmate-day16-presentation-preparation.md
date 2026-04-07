---
title: "[Walkmate] Day 16 — 최종 발표 준비와 YOLO11→YOLO26 전환 회고"
slug: "walkmate-day16-presentation-preparation"
date: 2026-02-24
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "Walkmate 프로젝트 전체를 정리한 회고. 초기 구상, 관리자 화면, YOLO11 한계, YOLO26 전환, 출력 형식 오류로 인한 오탐, 위험도/음성 안내 로직까지 한 글로 묶음."
thumbnail: "/images/posts/walkmate/cover16.png"
tags:
  - walkmate
  - yolo11
  - yolo26
  - tflite
  - accessibility
readTime: 16
series: "Walkmate AI"
seriesOrder: 9
featured: false
draft: false
toc: true
---

> **"발표 자료를 만든다는 건 예쁘게 정리하는 일이 아니라, 어디서 틀렸고 어떻게 바로잡았는지 끝까지 설명하는 일에 가까웠다."**
>
> Day 16에는 최종 발표를 준비했지만, 실제로 가장 오래 붙들고 있었던 것은 `Walkmate`가 어떤 문제를 풀려고 했고, 그 과정에서 왜 `YOLO11`에서 `YOLO26`으로 넘어가야 했는지, 그리고 그 전환 과정에서 얼마나 많은 오탐과 시행착오를 겪었는지를 정리하는 일이었다.

---

## 🚀 Today's Mission & Results

발표용 슬라이드를 정리하면서 프로젝트 전체 흐름을 다시 엮었다. 특히 사진으로 남아 있던 개발 흔적들을 빠짐없이 정리해 보니, `잘 된 장면`보다 `잘못 감지되던 과정`이 더 많은 설명을 필요로 한다는 점이 분명해졌다.

| 목표 항목                    |  상태   | 비고                                            |
| :--------------------------- | :-----: | :---------------------------------------------- |
| 최종 발표 자료 제작          | ✅ 완료 | 기능보다 의사결정 흐름 중심으로 재구성          |
| Walkmate 전체 회고 정리      | ✅ 완료 | 기획, 모델 학습, 관리자 UI, 음성 안내까지 통합  |
| YOLO11→YOLO26 전환 근거 정리 | ✅ 완료 | 탐지 성능과 출력 형식 차이 문제까지 문서화      |
| 오탐 사례 아카이빙           | ✅ 완료 | 출력 텐서 처리 실수와 점자블록 오인식 사례 포함 |

---

## 1. 우리가 처음 그리고 있던 서비스

Walkmate의 출발점은 단순한 객체 탐지 앱이 아니었다. 보행 약자가 길 위에서 마주치는 위험 요소를 빠르게 인식하고, 그 결과를 음성으로 전달하고, 필요하면 운영자가 현장 데이터를 추적할 수 있는 구조를 만드는 것이 목표였다.

<img src="/images/posts/walkmate/gallery/wm-20.png" alt="Walkmate 최초 기대 화면 콘셉트" style="display:block; width:100%; max-width:720px; margin:1rem auto;" />

처음에는 위와 같은 화면을 기대했다. 사용자는 지금 어디를 보고 있는지, 어떤 위험 요소가 있는지, 어느 방향으로 움직여야 하는지를 한눈에 이해할 수 있어야 했다.

<img src="/images/posts/walkmate/gallery/wm-05.png" alt="Walkmate 서비스 구상도" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

구상도에서도 확인할 수 있듯, 앱 하나만 잘 만드는 것으로 끝나는 프로젝트가 아니었다. 사용자 단말, 객체 탐지 모델, 음성 안내, 서버 저장소, 관리자 화면이 전부 연결되어야 했다.

<img src="/images/posts/walkmate/gallery/wm-04.png" alt="Walkmate 로고 시안" style="display:block; width:100%; max-width:420px; margin:1rem auto;" />

로고 시안도 초반에 정리했다. 겉보기에는 작은 요소지만, 팀 안에서는 "이 프로젝트가 단순한 데모가 아니라 실제 서비스처럼 보여야 한다"는 합의를 상징하는 결과물이기도 했다.

---

## 2. 현장 데이터를 모으고 운영할 수 있는 구조부터 만들었다

모델이 좋아도 실제 보행 환경 데이터를 모으고 관리할 수 없으면 금방 막힌다. 그래서 추론 모델과 별개로, 이미지 업로드 경로와 관리자 화면 구조를 꽤 일찍부터 잡아 두었다.

<img src="/images/posts/walkmate/gallery/wm-01.png" alt="백엔드 파일 구조 설정 화면" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

백엔드 파일 구조를 먼저 정리한 이유는 이후 들어올 이미지, 탐지 결과, 위험도 분류 결과를 흩어지지 않게 관리하기 위해서였다.

<img src="/images/posts/walkmate/gallery/wm-28.png" alt="휴대폰에서 3초마다 찍은 사진을 AWS S3에 저장한 화면" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

현장 테스트에서는 휴대폰에서 3초마다 사진을 찍어 `AWS S3`에 올리는 방식으로 데이터를 남겼다. 이 기록이 있어야 모델이 어떤 장면에서 흔들렸는지 나중에 재검토할 수 있었다.

<img src="/images/posts/walkmate/gallery/wm-10.png" alt="관리자용 웹 대시보드 페이지" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-11.png" alt="관리자용 웹 마스터DB 페이지" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-12.png" alt="관리자용 웹 세부 항목 페이지" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

관리자 웹도 함께 만들었다. 위험 신고를 단순히 쌓아 두는 것이 아니라, 어떤 지점에서 무엇이 반복적으로 탐지되는지, 어떤 사진이 실제 위험 판단에 쓰일 만한지 검토할 수 있게 하려는 목적이었다. 현장성과 운영성을 같이 잡아야 서비스가 굴러간다는 걸 이 시점부터 의식했다.

---

## 3. YOLO11 단계: 점자블록을 세밀하게 나누려 했지만 너무 자주 흔들렸다

초기에는 `YOLO11` 계열 모델로 시작했다. 점자블록과 주변 장애물을 같이 다루면서도 모바일에서 돌아갈 수 있는 선을 찾으려다 보니, 경량 모델에 기대를 많이 걸었다.

<img src="/images/posts/walkmate/gallery/wm-02.png" alt="Roboflow에서 기존 모델을 검색한 화면" style="display:block; width:100%; max-width:700px; margin:1rem auto;" />

처음에는 `Roboflow`에서 기존 모델을 검토하며 어떤 클래스 구성이 현실적인지부터 확인했다. 이때만 해도 "기존 모델을 조금 손보면 되지 않을까"라는 기대가 있었다.

<img src="/images/posts/walkmate/gallery/wm-21.png" alt="점자블록을 네 종류로 학습시키고 정확도를 분석한 화면" style="display:block; width:100%; max-width:700px; margin:1rem auto;" />

우리는 점자블록을 `stop block broken`, `stop block normal`, `straight block broken`, `straight block normal`처럼 세밀하게 나눠 학습시켜 보았다. 아이디어 자체는 좋았지만, 실제 모바일 추론 환경에서는 클래스가 세분화될수록 안정성이 떨어졌다.

<img src="/images/posts/walkmate/gallery/wm-26.png" alt="초기 모델이 노란색 점자 블록을 감지한 장면" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-06.png" alt="YOLO11 nano 모델이 일부 점자 블록만 감지한 장면" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

초기 모델은 노란색 점자블록을 어느 정도 잡기는 했지만, 연속된 블록을 모두 읽지 못하고 일부만 감지하는 경우가 자주 나왔다. 한 장면 안에서도 어떤 블록은 잡고 어떤 블록은 놓치는 식이라, 실제 안내용으로 쓰기에는 불안정했다.

<img src="/images/posts/walkmate/gallery/wm-03.png" alt="직진 블록과 정지 블록, 파손 여부를 세밀하게 감지하는 데 실패한 화면" style="display:block; width:100%; max-width:700px; margin:1rem auto;" />

세부 클래스를 더 잘 나눌수록 좋은 모델이 될 거라 생각했지만 결과는 반대였다. 직진 블록인지 정지 블록인지, 파손 여부가 맞는지까지 한 번에 맞히려다 보니 모델이 장면을 안정적으로 해석하지 못했다.

<img src="/images/posts/walkmate/gallery/wm-22.png" alt="점자블록은 감지했지만 auto orient 설정 문제로 이상하게 표시된 화면" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

여기에 `auto orient` 설정 문제까지 겹쳤다. 감지는 되었는데 화면 좌표계가 틀어져 엉뚱한 위치에 박스가 그려지는 상황이 발생했다. 이때 깨달은 건, 모델 정확도만이 아니라 입력 이미지 전처리와 후처리까지 포함해 전체 파이프라인을 봐야 한다는 점이었다.

---

## 4. 그래서 YOLO11에서 YOLO26으로 갈아탔다

점자블록과 일반 장애물을 더 정밀하게 잡아야 했기 때문에, 우리는 결국 `YOLO11` 중심 접근을 접고 `YOLO26` 계열로 전환했다. 더 정확하게 잡히길 기대했지만, 여기서부터는 다른 종류의 문제가 시작됐다.

<img src="/images/posts/walkmate/gallery/wm-07.png" alt="YOLO26n 구성 로직" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

모델 교체 자체는 생각보다 빠르게 진행됐다. 하지만 `YOLO26`은 기존에 쓰던 모델과 출력 형식이 달랐고, 이 차이를 우리가 초반에 너무 가볍게 봤다.

<img src="/images/posts/walkmate/gallery/wm-17.png" alt="YOLO26nano로 바꿨지만 출력 형식 차이로 잘못 감지한 장면" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

정확도를 높이기 위해 `YOLO26nano`로 올렸는데도 결과가 이상했던 이유는 모델 자체의 성능보다 `출력 텐서 해석 방식`에 있었다. 클래스 축, 박스 좌표, confidence를 읽는 순서가 기존 코드와 맞지 않으면 모델이 멀쩡해도 엉뚱한 물체를 본다.

<img src="/images/posts/walkmate/gallery/wm-15.png" alt="모델 출력 형식을 잘못 입력해서 사물을 이상하게 추론한 화면" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-14.png" alt="모델 출력 형식을 잘못 입력하여 사물을 잘못 인식한 두 번째 사례" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

초반에는 "모델이 왜 이렇게 멍청해졌지?"라고 생각했지만, 사실 멍청했던 건 모델이 아니라 우리가 작성한 후처리 코드였다. 출력 형식을 한 번 잘못 해석하니 박스 위치와 클래스 결과가 줄줄이 무너졌다.

<img src="/images/posts/walkmate/gallery/wm-13.png" alt="모델 출력 형식을 맞춘 뒤 모든 사물을 제대로 인식한 장면" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

출력 형식을 다시 맞춘 뒤에는 결과가 확연히 달라졌다. 이 장면은 우리가 "모델을 바꾸는 것"보다 "모델이 뱉는 값을 정확히 읽는 것"이 먼저라는 사실을 가장 분명하게 확인한 순간이었다.

<img src="/images/posts/walkmate/gallery/wm-19.png" alt="출력 형식을 올바르게 맞췄지만 노란색 점자 블록을 벤치로 잘못 인식한 화면" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

물론 여기서 끝나지는 않았다. 출력 형식을 제대로 맞춘 뒤에도 노란색 점자블록을 벤치로 잘못 인식하는 장면이 나왔다. 이건 모델 파싱 문제가 아니라 데이터 편향과 클래스 경계 문제에 가까웠다. 즉, 코드 수정만으로 해결되지 않는 오탐도 분명히 있었다.

<img src="/images/posts/walkmate/gallery/wm-08.png" alt="YOLO26nano 모델이 출력 형식을 맞춘 뒤 물체를 정밀하게 감지하는 장면" style="display:block; width:100%; max-width:560px; margin:1rem auto;" />

그래도 최종적으로는 `YOLO26nano` 쪽이 더 안정적인 결과를 보여줬다. 완벽하다고 말할 수는 없지만, 최소한 "왜 틀리는지 설명할 수 없는 상태"에서는 벗어났다. 그 차이가 실제 프로젝트에서는 매우 컸다.

---

## 5. 탐지 결과를 사용자 안내로 바꾸는 로직

탐지는 시작일 뿐이다. Walkmate에서는 감지된 결과를 곧바로 사용자 음성 안내와 위험도 판단으로 바꿔야 했다. 이 과정이 없으면 박스가 잘 그려져도 서비스 가치는 거의 없다.

<img src="/images/posts/walkmate/gallery/wm-27.png" alt="YOLO 80개 물체를 한글로 매핑한 코드 화면" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

먼저 `YOLO` 클래스들을 한글 안내 문장에 맞게 매핑했다. 영어 클래스 이름을 그대로 읽어 주는 앱은 실제 사용자에게 거의 도움이 되지 않기 때문이다.

<img src="/images/posts/walkmate/gallery/wm-09.png" alt="감지 물체를 3단계 위험도로 분류한 코드 화면" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

탐지된 물체는 `3단계 위험도`로 나눴다. 모든 물체를 동일하게 읽어 주면 오히려 소음만 많아지기 때문에, 어떤 것은 즉시 경고하고 어떤 것은 참고 정보로만 주는 식의 층위가 필요했다.

<img src="/images/posts/walkmate/gallery/wm-16.png" alt="물체 인식과 위험도, 거리 인식 기반 음성 안내 출력 코드" style="display:block; width:100%; max-width:620px; margin:1rem auto;" />

이후 거리 판단과 연결해 음성 안내 문장을 구성했다. 결국 사용자가 듣게 되는 것은 코드가 아니라 "앞에 장애물이 있습니다", "조심해서 이동하세요" 같은 문장이기 때문에, 추론 결과를 사용자 언어로 번역하는 레이어가 중요했다.

<img src="/images/posts/walkmate/gallery/wm-18.png" alt="사용자용 안내 중 페이지" style="display:block; width:100%; max-width:420px; margin:1rem auto;" />

사용자 안내 화면도 너무 많은 정보를 한 번에 보여주지 않도록 구성했다. 큰 사진이나 복잡한 텍스트보다, 지금 위험한지와 어느 쪽으로 가야 하는지가 우선이었다.

---

## 6. 발표에서 가장 강조한 장면은 '잘된 결과'보다 '틀린 결과'였다

발표를 준비하면서 오히려 더 확신하게 된 건, Walkmate의 핵심은 멋진 데모 화면 몇 장이 아니라 왜 틀렸는지 추적하고 고친 과정 자체에 있었다는 점이다.

- `YOLO11` 단계에서는 점자블록 세분화 자체가 모바일 추론 안정성과 충돌했다.
- `YOLO26` 단계에서는 모델 변경보다 출력 형식 변경이 더 큰 난관이었다.
- 출력 형식을 맞춘 뒤에도 점자블록을 벤치로 보는 식의 오탐이 남았고, 이건 데이터 품질과 클래스 설계 문제를 다시 보게 만들었다.
- 결국 모델 선택, 후처리, 위험도 분류, 음성 안내, 관리자 검토 화면이 한 세트로 움직여야 했다.

이 프로젝트를 통해 배운 건 "더 최신 모델을 쓰면 끝난다"가 아니라, 모델 버전이 바뀌는 순간 주변 코드와 데이터 해석 방식도 함께 다시 검증해야 한다는 사실이었다. 이번 글에서 사진을 전부 넣은 이유도 그 때문이다. 잘 나온 결과만 보면 간단해 보이지만, 실제로는 그 앞에 훨씬 많은 실패 화면이 있었다.

---

## 7. 팀 프로젝트다운 흔적들도 같이 남겨 두었다

기술 얘기만 남기면 프로젝트가 너무 매끈하게 보일 수 있다. 실제 현장에서는 밤늦게까지 모델 테스트를 돌리고, 잘못 감지된 사진을 다시 보고, 발표 자료를 고치면서 버티는 시간이 더 길었다. 그리고 정말 감사하게도 지호님 남편분께서 직접 두쫀쿠의 변형 버전인 두쫀초랑 휘낭시에를 만들어서 주셨다. 덕분에 팀원들과 함께 힘내서 발표를 준비할 수 있었다.

<img src="/images/posts/walkmate/gallery/wm-23.png" alt="팀 프로젝트 중 함께 먹은 두쫀초 사진 1" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-24.png" alt="팀 프로젝트 중 함께 먹은 두쫀초 사진 2" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

<img src="/images/posts/walkmate/gallery/wm-25.png" alt="팀 프로젝트 중 함께 먹은 휘낭시에 사진" style="display:block; width:100%; max-width:520px; margin:1rem auto;" />

이 사진들은 기능 설명과 직접 연결되지는 않지만, Walkmate가 실제로는 사람들 사이의 협업과 체력전 위에서 굴러갔다는 걸 보여주는 기록이라 남겨 두었다. 문제를 해결한 건 모델 하나가 아니라 끝까지 붙들고 있던 팀이었다.

---

## 📥 첨부 파일: 결과 보고서

최종 발표 자료는 아래 파일에 정리해 두었다.

[결과보고서\_3팀(공부많이된다)\_260225.pptx](/files/walkmate-final-report-team3.pptx)

---

## 💭 Reflections

발표 자료를 만들며 가장 크게 느낀 점은, 프로젝트 후반부의 문서화가 단순한 정리 작업이 아니라 사실상 마지막 디버깅이라는 것이다. 사진을 한 장씩 다시 보니, "모델이 안 좋았다"라고 뭉뚱그릴 수 있는 문제가 거의 없었다. 입력 전처리, 출력 파싱, 클래스 정의, 데이터 수집, 사용자 안내 문구까지 전부 얽혀 있었다.

Walkmate를 다시 한다면 처음부터 더 큰 모델을 쓰기보다, `어떤 장면에서 왜 틀리는지 설명 가능한 구조`를 먼저 만들 것이다. 이번에 `YOLO11`에서 `YOLO26`으로 바꾼 경험은 성능 향상보다 검증 방식의 중요성을 더 크게 남겼다.

**Next Plan:** 최종 발표 이후에는 점자블록 데이터 품질을 다시 손보고, 벤치/블록 오인식처럼 남아 있는 클래스 경계 문제를 줄이는 방향으로 후속 개선을 검토할 예정이다.
