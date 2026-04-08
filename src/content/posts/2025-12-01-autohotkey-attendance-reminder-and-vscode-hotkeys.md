---
title: "출석 체크를 안 잊기 위해 만든 AutoHotkey 알림, 그리고 VS Code 핫키까지"
slug: autohotkey-attendance-reminder-and-vscode-hotkeys
date: 2025-12-01
author: Evan Yoon
category: explore
subcategory: new-tech
description: "AI for Future Workforce 과정의 출석 체크를 놓치지 않기 위해 AutoHotkey로 시작 프로그램 알림을 만들고, 그 흐름으로 VS Code용 핫키까지 적용해본 기록."
thumbnail: /images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/cover.png
tags:
  - autohotkey
  - productivity
  - windows
  - vscode
  - automation
  - intel-ai
readTime: 9
series: Intel AI for Future Workforce
seriesOrder: 2
featured: false
draft: false
toc: true
---

# 출석 체크를 안 잊기 위해 만든 AutoHotkey 알림, 그리고 VS Code 핫키까지

AI for Future Workforce 과정을 들으면서, 생각보다 많은 사람들이 수업 자체는 성실하게 참여하고도 **정해진 시간에 입실 체크와 퇴실 체크를 누르지 않아서 지각이나 결석 처리**가 되는 상황을 자주 봤다.

이 과정은 매일 **오전 9시 10분 입실 체크**, **오후 5시 50분 퇴실 체크**를 해야 출석이 인정된다. 문제는 다들 수업은 잘 듣고 있는데, 체크 버튼을 누르는 순간만 놓치는 일이 생각보다 자주 생긴다는 점이었다.

처음에는 "왜 이렇게 자주 까먹지?" 싶었는데, 막상 나도 정신없이 노트북 켜고 자리 정리하고 강의 자료 열다 보면 충분히 놓칠 수 있겠다는 생각이 들었다. 특히 아침에는 더 그랬다.

그래서 생각한 게 아주 단순했다.

**어차피 다들 오면 노트북부터 켜니까, 컴퓨터를 켜자마자 출석 체크를 떠올리게 만드는 팝업이 뜨면 되지 않을까?**

이 아이디어에서 시작해서 처음으로 **AutoHotkey**를 만져보게 됐다.

![출석 체크 화면](/images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/attendance-check.png)

## 왜 하필 AutoHotkey였을까

찾아보니 Windows에서 간단한 자동화나 단축키 설정, 팝업 띄우기 같은 걸 할 때 AutoHotkey를 많이 쓴다고 했다. 나는 그 전까지 이름만 들어봤지 실제로 써본 적은 없었는데, 찾아보니 생각보다 진입장벽이 낮아 보였다.

검색하자마자 눈에 띈 글이 하나 있었고, 그 글을 그대로 따라 해보기로 했다.

처음에는 "오, 이거 생각보다 쉬운가?" 싶었다. 그런데 막상 따라 하다 보니 어느 순간부터 문법 오류가 나기 시작했다. 분명히 글에 나온 대로 적었는데 작동을 안 했다.

![예전 문법 오류 화면](/images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/old-syntax-error.png)

## 왜 자꾸 오류가 났는지 뒤늦게 알았다

처음에는 내가 오타를 냈나 싶었다. 괄호가 빠졌나, 큰따옴표가 잘못됐나, 저장을 잘못했나 하나씩 의심했다.

그런데 찾아보니 문제는 내 실수가 아니라 **참고한 글의 문법이 예전 버전 기준**이라는 점이었다. AutoHotkey는 버전에 따라 문법 차이가 꽤 있는데, 내가 설치한 환경은 최신 방식이었고, 블로그 글은 예전 방식으로 작성되어 있었다.

뒤늦게 작성일을 보니 **2019년 글**이었다.

![2019년 작성일 화면](/images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/blog-date-2019.png)

이걸 보고 조금 허탈하면서도 오히려 이해가 됐다. 따라 한 문법이 틀린 게 아니라, **지금은 더 이상 그대로 쓰지 않는 문법**이었던 것이다.

그때 느꼈다. 검색해서 나오는 글을 무조건 믿고 따라 하기보다, 특히 자동화 도구나 개발 도구처럼 버전 변화가 큰 것들은 **글의 작성 시점과 현재 버전 차이**를 꼭 봐야 한다는 것을.

## 최신 문법으로 바꾸니 정말 간단했다

막상 최신 문법으로 보니 오히려 더 단순했다.

내가 최종적으로 만든 출석 체크용 스크립트는 아래 한 줄이다.

```ahk
MsgBox "QR SCAN"
```

처음에는 뭔가 복잡할 줄 알았는데, 정말 이 한 줄이면 됐다.  
컴퓨터를 켰을 때 "QR SCAN"이라는 메시지 박스만 떠도 목적은 충분했다. 중요한 건 화려한 기능이 아니라 **까먹지 않게 만드는 트리거**였기 때문이다.

이 스크립트를 `출석체크.ahk`로 저장하고, 실행 파일로도 만들어 두었다.

## 시작 프로그램으로 넣으니 효과가 확실했다

그다음 단계는 이 스크립트를 **시작 앱**에 넣는 것이었다.  
이렇게 해두면 노트북을 켤 때마다 메시지 박스가 자동으로 뜬다.

![시작 앱 등록 화면](/images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/startup-apps.png)

실제로 등록한 뒤에는 아래처럼 팝업이 잘 떴다.

![팝업 메시지 박스](/images/posts/autohotkey-attendance-reminder-and-vscode-hotkeys/popup-msgbox.png)

이건 정말 별것 아닌 기능인데, 체감 효과는 컸다.  
사람은 생각보다 "몰라서" 못 하는 경우보다, **알지만 순간적으로 놓쳐서** 실수하는 경우가 많다. 이 팝업은 그 순간을 막아주는 장치였다.

나 혼자만 쓰기 아까워서 주변 사람들에게도 방법을 공유했다. 실제로 이 과정에서 출석 체크를 깜빡하는 사람이 자주 있었기 때문에, 이런 식의 작은 자동화가 꽤 실용적이라고 느꼈다.

## 여기서 끝이 아니라, AutoHotkey가 재미있어졌다

원래 목적은 출석 체크 알림 하나였다. 그런데 한 번 작동하는 걸 보고 나니 생각이 바뀌었다.

**"이거, 다른 데도 쓸 수 있겠는데?"**

그 뒤로 AutoHotkey를 더 활용할 방법을 찾아보다가, **VS Code에서 커서 이동을 훨씬 편하게 만드는 핫키 설정** 관련 글을 보게 됐다. 내용을 받아 온 뒤, 내가 이미 써두고 있던 키 설정과 겹치는 부분은 정리해서 내 방식대로 조금 덜어냈다.

지금 사용 중인 핵심 설정은 아래와 같다.

```ahk
SetStoreCapslockMode false
SetCapsLockState "AlwaysOff"

CapsLock & f::
{
    if GetKeyState("CapsLock", "T")
        SetCapsLockState "AlwaysOff"
    else
        SetCapsLockState "AlwaysOn"
}

#HotIf GetKeyState("CapsLock", "P")

i::Send "{Up}"
j::Send "{Left}"
k::Send "{Down}"
l::Send "{Right}"

q & j::Send "^{Left}"
q & l::Send "^{Right}"

w::Send "{PgUp}"
a::Send "{Home}"
s::Send "{PgDn}"
d::Send "{End}"

Left::Send "{Home}"
Right::Send "{End}"
Up::Send "{PgUp}"
Down::Send "{PgDn}"

Enter::Send "^{Enter}"
o::Send "{BackSpace}"
u::Send "{Del}"

#HotIf
```

## VS Code에서 특히 좋았던 점

내가 가장 만족한 부분은 **오른손을 방향키 쪽으로 멀리 내리지 않아도 커서를 움직일 수 있다는 점**이었다.

보통 코드를 읽거나 수정할 때 커서를 맨 왼쪽, 맨 오른쪽, 위아래 줄로 자주 옮기게 되는데, 그때마다 손이 키보드 오른쪽 아래까지 내려가면 흐름이 끊긴다. 별것 아닌 것 같아도 이런 동작이 계속 반복되면 은근히 피로하다.

그런데 CapsLock을 조합키처럼 쓰고 나서는:

- `i j k l`로 방향키 이동
- `a d`로 Home / End
- `w s`로 Page Up / Page Down
- `q + j`, `q + l`로 단어 단위 이동
- `o`, `u`로 Backspace / Delete

이런 식으로 손을 거의 기본 위치에 둔 채 편하게 움직일 수 있게 됐다.

특히 코드 중간에서 문장을 다듬거나, 여러 줄을 오가며 커서를 조정할 때 만족도가 높았다.  
나는 원래 한번 집중이 끊기면 다시 몰입하는 데 시간이 조금 걸리는 편인데, 이런 키 설정은 생각보다 그 흐름을 잘 지켜준다.

## 이번에 느낀 점

이번 경험은 단순히 "팝업 하나 만들었다"에서 끝나지 않았다.

처음에는 출석 체크를 잊지 않기 위한 아주 작은 문제 해결이었는데, 그 과정에서:

- 예전 글을 그대로 따라 하면 왜 안 되는지 직접 겪었고
- 도구의 버전 차이를 확인하는 습관이 왜 중요한지 배웠고
- 자동화는 거창한 시스템이 아니라도 충분히 유용하다는 걸 느꼈고
- 한 번 익힌 도구를 다른 생산성 영역에도 확장할 수 있다는 걸 알게 됐다

무엇보다 좋았던 건, **실제로 내 생활이 조금 편해졌다는 점**이다.  
이런 도구는 배울 때는 사소해 보여도, 한번 손에 익으면 계속 쓰게 된다.

예전에는 AutoHotkey를 "개발자들이 뭔가 복잡하게 쓰는 도구" 정도로 생각했는데, 지금은 오히려 **생활 밀착형 자동화 도구**처럼 느껴진다. 나처럼 완전 처음 시작하는 사람도, 아주 작은 불편 하나만 잡아도 충분히 재미를 느낄 수 있었다.

앞으로도 반복해서 손이 가는 일, 자꾸 까먹는 일, 불필요하게 손목이 움직이는 일을 발견하면 이런 방식으로 하나씩 줄여보고 싶다.  
출석 체크 알림 하나로 시작했지만, 이런 작은 자동화가 쌓이면 결국 작업 방식 자체가 달라질 수도 있겠다는 생각이 든다.
