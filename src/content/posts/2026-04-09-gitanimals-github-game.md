---
title: "깃허브에서 동물 키우기 — GitAnimals"
slug: gitanimals-github-game
date: 2026-04-09
author: Evan Yoon
category: explore
subcategory: new-tech
description: |
  지호님이 찾아온 귀여운 신문물, GitAnimals.
  깃허브 활동으로 동물을 키우고, 길드를 만들고,
  가챠까지 돌린다. 생각보다 훨씬 진지한 게임이다.
thumbnail: /images/posts/gitanimals/guild.png
tags:
  - gitanimals
  - github
  - side-project
  - fun
  - developer-culture
readTime: 6
featured: false
draft: false
toc: true
---

## 지호님이 찾아온 것

오늘 글은 "신문물 탐방"이라기보다는, 지호님이 귀엽고 재밌는 걸 찾았다고 같이 해보자고 해서 시작한 이야기다.

이름은 **[GitAnimals](https://github.com/git-goods/gitanimals)**.

처음 링크 받았을 때 반신반의했다. 깃허브에서 동물을 키운다고? 근데 직접 해보고 나서 생각이 바뀌었다. 생각보다 꽤 진지하게 만들어진 프로젝트다.

---

## GitAnimals가 뭔가

깃허브 활동 기록으로 동물을 키우고, 입양하고, 다른 유저와 거래까지 할 수 있는 게임이다.

[깃허브 레포](https://github.com/git-goods/gitanimals)를 보면 기여자도 꽤 많은 오픈소스 프로젝트다.

동물들이 정적인 이미지가 아니라 **실시간으로 움직인다**. README에 넣어두면 내 프로필에 방문한 사람들이 동물들이 뛰어다니는 걸 볼 수 있다.

![깃허브 프로필 화면](/images/posts/gitanimals/github.png)

---

## 시작 방법 — 진짜 10초면 된다

새 레포 하나 만들고 `README.MD`에 아래 코드 붙이면 끝이다.

```html
<a href="https://github.com/devxb/gitanimals">
  <img
    src="https://render.gitanimals.org/lines/{username}?pet-id=1"
    width="1000"
    height="120"
  />
</a>
```

`{username}` 자리에 내 깃허브 아이디 넣으면 바로 동물이 달리기 시작한다.

표시 방식은 세 가지가 있다.

**Lines 모드** — 동물이 일렬로 달리는 기본 모드

```html
<img
  src="https://render.gitanimals.org/lines/{username}?pet-id=1"
  width="1000"
  height="120"
/>
```

**Farm 모드** — 내가 가진 동물들이 자유롭게 뛰어노는 공간

```html
<img src="https://render.gitanimals.org/farms/{username}" />
```

**Guild 모드** — 길드원들 동물이 한 공간에 모이는 모드

```html
<img src="https://render.gitanimals.org/guilds/{guildId}/draw" />
```

셋 다 SVG 애니메이션이라 계속 움직인다. 프로필 방문하면 그냥 이미지만 있는 것보다 훨씬 눈에 띈다.

---

## 펫을 얻는 방법

꾸미기용처럼 보이지만 게임 구조가 제대로 잡혀 있다.

**1. 커밋 30번 — 새 펫 등장**
커밋이 30번 쌓일 때마다 펫이 하나 생긴다. 펫마다 나오는 확률이 다르고, 최대 30마리까지 보여줄 수 있다. 30마리가 넘으면 인벤토리로 들어가고 홈페이지에서 바꿔 끼울 수 있다.

**2. 펫 구매**
다른 유저가 파는 펫을 **커밋 포인트**로 살 수 있다. 커밋할 때마다 포인트가 쌓이고, 반대로 내 펫을 팔아서 포인트를 얻을 수도 있다.

활동 지표는 두 가지다.

- **Total Contributions** — 깃허브 가입 이후 누적된 전체 컨트리뷰션
- **Commits** — 펫 획득 기준이 되는 커밋 횟수

새 컨트리뷰션이 반영되는 데 최대 1시간 정도 걸릴 수 있다.

---

## 길드 기능

지호님이 길드장으로 우리를 초대해줬다. 길드 페이지에서 Join 버튼 누르면 바로 가입된다.

지금 우리 길드엔 Intel AI 과정 팀원들이 들어와 있다.

<a href="https://www.gitanimals.org/">
  <img
    src="https://render.gitanimals.org/guilds/829895026409453279/draw"
    width="600"
    height="300"
    alt="gitanimals guild"
  />
</a>

각자 설정한 동물들이 한 공간에서 같이 움직이는데, 처음 봤을 때 생각보다 훨씬 귀여워서 피식했다. 커밋을 열심히 할수록 내 동물이 레벨업해서 길드 화면에도 티가 난다.

![길드 페이지](/images/posts/gitanimals/guild.png)

---

## 웹사이트에서 할 수 있는 것들

[gitanimals.org](https://www.gitanimals.org/)에 들어가면 README 이미지 말고도 웹에서 직접 할 수 있는 게 꽤 있다.

**가챠**
커밋 포인트로 새 펫을 뽑는다. 확률이 공개돼 있고 희귀할수록 낮다. 당연히 희귀한 거 뽑고 싶어진다.

![가챠 화면](/images/posts/gitanimals/gotcha.png)

**마이페이지**
내 펫 목록, 레벨, 지금 보여주고 있는 펫을 관리하는 공간이다.

![마이페이지](/images/posts/gitanimals/mypage.png)

**거래소 (Auction)**
다른 유저와 펫을 사고파는 곳이다. 희귀 펫은 꽤 높은 포인트에 올라와 있기도 하다.

![경매/거래 화면](/images/posts/gitanimals/auction.png)

**배경 꾸미기**
농장 배경을 사서 내 동물들이 뛰어노는 공간을 꾸밀 수 있다. 기본 배경도 나쁘진 않은데 옵션이 다양하다.

![배경 구매 화면](/images/posts/gitanimals/background.png)

---

## 내 동물들

지금 내 메인 동물 유니콘.

<a href="https://github.com/devxb/gitanimals">
  <img src="https://render.gitanimals.org/lines/Evan-Yoon?pet-id=829896534857322024" width="1000" height="200"/>
</a>

농장 전체도 볼 수 있다.

<a href="https://github.com/devxb/gitanimals">
  <img src="https://render.gitanimals.org/farms/Evan-Yoon"/>
</a>

---

## 마무리

처음엔 그냥 프로필 꾸미기 도구인 줄 알았는데, 파고들수록 구조가 탄탄하다. 커밋이 게임 보상으로 연결되고, 길드에서 팀원들이랑 공간을 공유하고, 거래소에 가챠까지. 그냥 재미있는 사이드 프로젝트가 아니라 작은 생태계가 있다.

솔직히 이런 것들이 이 바닥에 계속 흥미를 갖게 하는 것 같다. AI나 클라우드나 알고리즘 같은 것들도 있지만, 개발자 문화 안에서 이렇게 재치 있는 것들이 계속 나온다는 게 — 이 분야를 파고들길 잘했다는 생각이 드는 순간 중 하나다.
