---
title: "인텔 특강 Day 3: 사용자 중심 디자인과 UI/UX 용어 정리"
slug: intel-uiux-special-lecture-day3
date: 2026-03-05
author: Evan Yoon
category: explore
subcategory: visit
description: |
  인텔 과정 중 3월 5일에 들은 UI/UX 디자인 특강 내용을 정리했다.
  이번 특강에서는 기능 중심 사고와 사용자 중심 사고의 차이, 디자인 씽킹 프로세스,
  그리고 실제 서비스 화면에서 자주 쓰이는 UX 법칙과 UI/UX 용어를 함께 다뤘다.
thumbnail: /images/posts/intel-uiux-special-lecture-day3/image6.jpg
tags:
  - intel-ai
  - ui-ux
  - design-thinking
  - usability
  - special-lecture
readTime: 13
featured: false
draft: false
toc: true
---

3월 5일 UI/UX 디자인 특강은 전날보다 조금 더 실무에 가까운 내용이 많았다. 단순히 화면을 예쁘게 만드는 방법보다는, 사용자가 실제로 어떤 문제를 겪고 있는지 먼저 보고 그에 맞게 화면과 기능을 설계해야 한다는 이야기가 중심이었다. 특히 `기능을 늘리는 관점`과 `사용자를 이해하는 관점`을 나눠서 설명한 부분이 기억에 남았다.

전반부는 사용자 중심 디자인과 디자인 씽킹 이야기였고, 후반부는 실제 화면 설계에서 자주 나오는 사용성 법칙과 UI/UX 용어 정리로 이어졌다. 개발을 할 때도 구현 가능성만 볼 게 아니라, 사용자가 어디에서 헷갈리고 어디에서 편하다고 느끼는지 같이 봐야 한다는 점을 다시 생각하게 됐다.

![3월 5일 특강 표지](/images/posts/intel-uiux-special-lecture-day3/image1.jpg)

## 기능 중심이 아니라 사용자 중심으로 보기

특강 초반에는 사용자 중심 사고가 왜 중요한지를 직관적인 예시로 설명했다. 제품이 기술적으로 정교해 보여도 사용자가 원하는 문제를 제대로 해결하지 못하면 좋은 제품이라고 보기 어렵다. 반대로 기능이 아주 많지 않아도 사용자의 상황과 맥락을 잘 이해하면 훨씬 편한 경험을 만들 수 있다.

![사용자 중심 사고의 출발점](/images/posts/intel-uiux-special-lecture-day3/image2.jpg)

강의에서는 기능 중심 사고와 사용자 중심 사고를 비교하면서 우리가 흔히 빠지기 쉬운 지점도 같이 짚었다. 기능 중심 사고는 "무엇을 더 넣을까"에 집중하고, 사용자 중심 사고는 "사용자가 왜 이 행동을 하려는가"를 먼저 본다. 전자는 기능이 계속 늘어나기 쉽고, 후자는 사용자가 목적을 더 빨리 이루게 만드는 방향으로 간다.

![기능 중심 사고와 사용자 중심 사고](/images/posts/intel-uiux-special-lecture-day3/image3.jpg)

금융 서비스 사례도 함께 나왔는데, 같은 송금이나 조회 기능을 제공하더라도 어떤 서비스는 기능 구조가 먼저 보이고 어떤 서비스는 사용자의 행동 흐름이 먼저 보인다. 이 차이가 곧 사용성 차이로 이어진다는 설명이 설득력 있었다. 기능은 비슷해 보여도 사용자가 느끼는 난이도와 만족도는 꽤 다를 수 있다.

![서비스 사례로 보는 사용자 중심 설계](/images/posts/intel-uiux-special-lecture-day3/image4.jpg)

가장 기억에 남았던 문장은 "잘 만든 UI도 잘못된 문제를 해결하고 있다면 실패한다"는 내용이었다. 화면 완성도보다 먼저 봐야 하는 건 문제 정의 자체라는 점을 다시 생각하게 했다. 결국 좋은 UX는 보기 좋은 결과물보다, 애초에 맞는 문제를 풀고 있는지에서 시작된다고 느꼈다.

![문제 정의의 중요성](/images/posts/intel-uiux-special-lecture-day3/image5.jpg)

## 디자인 씽킹 프로세스 정리

이어지는 내용은 디자인 씽킹이었다. 특강에서는 디자인 씽킹을 `공감 -> 정의 -> 아이데이션 -> 프로토타입 -> 테스트 -> 구현` 흐름으로 설명했다. 단순히 아이디어를 많이 내는 방법이 아니라, 사용자를 이해한 뒤 문제를 다시 정의하고 반복해서 검증하는 과정이라는 점이 핵심이었다.

![디자인 씽킹 프로세스](/images/posts/intel-uiux-special-lecture-day3/image6.jpg)

특히 전날 배운 더블 다이아몬드와 연결해서 설명한 부분이 좋았다. 사용자를 넓게 탐색하고 문제를 좁혀 정의한 뒤, 해결책을 다시 넓게 고민하고 마지막에 실제로 실행할 수 있는 방향으로 좁혀 가는 흐름이 자연스럽게 이어졌다. 처음부터 정답을 맞히는 것보다, 탐색하고 검증하는 과정을 반복하는 태도가 더 중요하다는 점이 분명하게 남았다.

![디자인 씽킹과 더블 다이아몬드 비교](/images/posts/intel-uiux-special-lecture-day3/image7.jpg)

## 사용자 중심 프로덕트를 만드는 UX 법칙

이후 강의는 화면 설계에 바로 적용할 수 있는 사용성 법칙들로 이어졌다. 존 야블론스키의 UX 법칙을 중심으로 실제 서비스 화면을 같이 보여 주면서 설명했는데, 추상적인 개념으로만 듣는 게 아니라 실제 앱과 웹에서 어떻게 보이는지 함께 볼 수 있어서 이해가 쉬웠다.

![사용자 중심 프로덕트 섹션](/images/posts/intel-uiux-special-lecture-day3/image8.jpg)

![존 야블론스키의 UX 법칙](/images/posts/intel-uiux-special-lecture-day3/image9.jpg)

### 제이콥의 법칙

사용자는 새로운 서비스도 결국 익숙한 방식으로 사용하려는 경향이 있다. 그래서 완전히 새로운 인터랙션을 억지로 만들기보다, 사용자가 이미 익숙한 패턴을 따라가는 편이 더 자연스럽다. 넷플릭스, 웨이브, 쿠팡플레이 같은 OTT나 금융 앱 사례를 보면 비슷한 구조와 패턴이 반복되는 이유도 여기에 있다.

![제이콥의 법칙 사례](/images/posts/intel-uiux-special-lecture-day3/image10.jpg)

### 피츠의 법칙

클릭하거나 터치해야 하는 대상은 충분히 크고 가까워야 한다. 자주 누르는 버튼일수록 더 쉽게 닿는 위치에 있어야 하고, 모바일에서는 특히 엄지 손가락이 닿는 범위를 고려해야 한다. 자주 쓰는 액션이 화면 하단이나 손이 잘 닿는 곳에 배치되는 이유도 여기 있다.

![피츠의 법칙 기본 예시](/images/posts/intel-uiux-special-lecture-day3/image11.jpg)

![모바일에서의 피츠의 법칙](/images/posts/intel-uiux-special-lecture-day3/image12.jpg)

### 힉의 법칙

선택지가 많아질수록 사용자의 결정 시간은 길어진다. 그래서 모든 기능을 한 화면에 한꺼번에 보여 주기보다, 중요한 선택지만 먼저 보여 주고 나머지는 단계적으로 드러내는 편이 낫다. 서비스가 친절하다는 건 기능이 많은 게 아니라, 사용자가 지금 필요한 선택만 하게 만들어 주는 것이라는 설명이 인상적이었다.

![힉의 법칙 사례](/images/posts/intel-uiux-special-lecture-day3/image13.jpg)

### 밀러의 법칙

사람이 한 번에 처리할 수 있는 정보량에는 한계가 있다. 그래서 정보를 작은 묶음으로 나누고, 관련 있는 요소끼리 묶어 두어야 부담이 줄어든다. 콘텐츠 서비스나 설정 화면에서 비슷한 항목을 범주별로 나누는 이유도 결국 사용자가 덜 헷갈리게 하기 위해서다.

![밀러의 법칙과 청킹](/images/posts/intel-uiux-special-lecture-day3/image14.jpg)

### 피크엔드 법칙

사용자는 모든 과정을 똑같이 기억하지 않고, 가장 강했던 순간과 마지막 경험을 중심으로 전체를 기억한다. 그래서 가입 완료, 결제 완료, 추천 결과 확인처럼 마지막 단계의 감정이 중요하다. 마무리 순간에 깔끔한 피드백을 주면 전체 경험도 더 좋게 남을 수 있다.

![피크엔드 법칙 사례](/images/posts/intel-uiux-special-lecture-day3/image15.jpg)

### 폰 레스토프 효과

비슷한 정보가 나열될 때 하나만 두드러지면 그 요소가 가장 잘 기억된다. 그래서 핵심 버튼, 강조 배지, 프로모션 카드처럼 꼭 보여 줘야 하는 정보는 주변과 차이가 나게 처리하는 것이 효과적이다. 다만 모든 것을 강조하면 오히려 아무것도 눈에 들어오지 않기 때문에, 무엇을 먼저 보이게 할지 우선순위를 정하는 게 중요하다.

![폰 레스토프 효과 사례](/images/posts/intel-uiux-special-lecture-day3/image16.jpg)

### 도허티 임계

시스템 반응 속도는 사용자의 몰입과 직접 연결된다. 로딩이 있더라도 빠르게 반응이 오면 덜 답답하게 느끼고, 반대로 아무 반응이 없으면 서비스가 멈춘 것처럼 느끼기 쉽다. 그래서 스켈레톤 UI, 즉시 반응하는 애니메이션, 처리 중 상태 안내 같은 요소들이 중요하다는 설명이 이어졌다.

![도허티 임계 사례](/images/posts/intel-uiux-special-lecture-day3/image17.jpg)

### 시각적 위계와 게슈탈트 원리

이후에는 버튼의 크기, 색, 위치를 통해 정보의 중요도를 드러내는 시각적 위계와, 사람이 개별 요소보다 묶음과 패턴으로 화면을 인식하는 게슈탈트 원리도 함께 다뤘다. 결국 사용성은 설명 문장만으로 결정되는 게 아니라, 화면을 봤을 때 무엇이 먼저 보이는지와도 연결된다는 뜻이었다.

![시각적 위계](/images/posts/intel-uiux-special-lecture-day3/image18.jpg)

![게슈탈트 원리](/images/posts/intel-uiux-special-lecture-day3/image19.jpg)

## 후반부 정리: UI/UX 용어를 실제 화면과 연결해서 이해하기

특강 후반부에는 디자이너와 개발자가 자주 쓰는 UI/UX 용어를 사례 중심으로 정리했다. 용어만 따로 외우는 방식이 아니라, 실제 서비스 화면에서 어떤 요소를 가리키는지 같이 보여 줘서 훨씬 실용적이었다. 같은 화면을 두고도 서로 다른 단어를 쓰면 소통이 꼬이기 쉬운데, 이런 기본 용어 정리가 그 차이를 줄여 준다고 느꼈다.

![UI UX 용어 섹션](/images/posts/intel-uiux-special-lecture-day3/image21.jpg)

모바일 앱 유형에서는 네이티브 앱, 웹 앱, 하이브리드 앱의 차이를 비교했다. 또 적응형 디자인과 반응형 디자인의 차이도 같이 설명했는데, 기기나 화면 크기가 달라질 때 어떤 기준으로 화면이 바뀌는지 이해하는 데 도움이 됐다.

![모바일 앱 유형](/images/posts/intel-uiux-special-lecture-day3/image22.jpg)

![적응형 디자인과 반응형 디자인](/images/posts/intel-uiux-special-lecture-day3/image23.jpg)

기본 구성 요소로는 헤더와 푸터, GNB, LNB, 탭바, 브레드크럼이 소개됐다. 평소에는 그냥 지나치던 화면 요소들도 각각 역할이 분명하다는 점을 다시 정리할 수 있었다. 이런 용어를 정확히 알아두면 기획서나 피그마 시안, 개발 이슈를 볼 때도 훨씬 빠르게 소통할 수 있다.

![헤더와 푸터](/images/posts/intel-uiux-special-lecture-day3/image24.jpg)

![내비게이션 구성 요소](/images/posts/intel-uiux-special-lecture-day3/image25.jpg)

![브레드크럼](/images/posts/intel-uiux-special-lecture-day3/image26.jpg)

온보딩과 메뉴 구조, 페이지네이션, 팝업과 바텀시트, 토스트와 얼럿, 툴팁처럼 실제 서비스에서 자주 쓰는 인터페이스 요소들도 차례로 나왔다. 각각이 언제 어울리는지 구분해서 써야 한다는 점이 중요했다. 예를 들어 토스트는 가벼운 안내에 어울리지만, 사용자의 선택이 꼭 필요한 상황이라면 얼럿이 더 맞다.

![온보딩](/images/posts/intel-uiux-special-lecture-day3/image27.jpg)

![메뉴 유형](/images/posts/intel-uiux-special-lecture-day3/image28.jpg)

![페이지네이션](/images/posts/intel-uiux-special-lecture-day3/image29.jpg)

![팝업과 바텀시트](/images/posts/intel-uiux-special-lecture-day3/image30.jpg)

![토스트와 얼럿](/images/posts/intel-uiux-special-lecture-day3/image31.jpg)

![툴팁](/images/posts/intel-uiux-special-lecture-day3/image32.jpg)

## 느낀 점

이번 3월 5일 특강은 UI/UX가 단순히 화면을 꾸미는 일이 아니라, 문제를 푸는 방식이라는 점을 더 분명하게 보여 준 시간이었다. 특히 디자인 씽킹 파트에서는 기능을 먼저 정하기보다 사용자의 상황을 먼저 이해하는 태도가 왜 중요한지 다시 생각하게 됐다. 개발을 할 때도 구현 가능한 기능을 빨리 만드는 것만큼, 그 기능이 실제로 사용자의 불편을 줄이는지 확인하는 과정이 중요하다고 느꼈다.

또한 UX 법칙과 UI 용어 정리 파트는 바로 써먹을 수 있는 내용이 많았다. 버튼 위치, 정보 양, 로딩 피드백, 강조 방식처럼 작아 보이는 결정들이 실제 사용 경험을 크게 바꾼다는 점도 인상 깊었다. 앞으로 화면을 만들거나 기능을 설계할 때도 "무엇을 넣을까"보다 "사용자가 더 쉽게 이해하고 움직일 수 있을까"를 먼저 생각해야겠다는 생각이 들었다.

## 첨부 파일

- [260305 UI/UX 디자인 특강 PPT](/files/intel-uiux-special-lecture-2026-03-05.pptx)
