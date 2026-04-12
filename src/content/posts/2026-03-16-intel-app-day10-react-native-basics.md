---
title: "[App Dev] Day 10: React Native 시작, 웹과 모바일의 차이"
slug: intel-app-day10-react-native-basics
date: 2026-03-16
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 10일차. Expo로 React Native 개발 환경을 구성하고, 웹과 모바일의 핵심 차이를 기본 컴포넌트를 통해 파악했다."
thumbnail: ""
tags:
  - react-native
  - expo
  - mobile
  - frontend
  - bootcamp
readTime: 9
series: "Intel App Dev 2026"
seriesOrder: 10
featured: false
draft: false
toc: true
---

## 웹과 모바일 환경의 차이

지난주까지 React로 웹을 만들었다. 오늘부터는 React Native로 **모바일 앱**을 만든다. JavaScript와 JSX를 쓴다는 점은 같다. 하지만 실행 환경이 다르다.

웹 브라우저에서는 `<div>`, `<p>`, `<button>` 같은 HTML 태그를 쓴다. 모바일에서는 `<View>`, `<Text>`, `<Pressable>` 같은 React Native 전용 컴포넌트를 쓴다. CSS 대신 JavaScript 객체로 스타일을 정의하고, 플렉스박스 방향 기본값도 다르다.

---

## Expo로 개발 환경 구성

React Native는 Android Studio나 Xcode 없이도 **Expo**를 쓰면 훨씬 빠르게 시작할 수 있다.

```bash
npx create-expo-app mobile-app
cd mobile-app
npx expo start
```

`npx expo start`를 실행하면 QR 코드가 터미널에 뜬다. 폰에 **Expo Go** 앱을 설치하고 QR 코드를 찍으면 실제 기기에서 바로 앱이 실행된다. 코드를 저장하면 폰 화면이 자동으로 갱신된다. 시뮬레이터 없이 실기기에서 바로 테스트할 수 있다는 게 처음에 꽤 편리하게 느껴졌다.

프로젝트 구조:

```
mobile-app/
├── App.js            ← 진입점
├── screens/          ← 화면 컴포넌트
│   ├── UpDownGameScreen.js
│   ├── InputExampleScreen.js
│   ├── ProfileCard.js
│   └── MoodScreen.js
├── components/
│   └── MoodButton.js
├── app.json          ← Expo 설정
└── package.json
```

---

## 웹 vs 모바일: 핵심 차이

| 항목 | React (웹) | React Native (모바일) |
|------|-----------|----------------------|
| 기본 컨테이너 | `<div>` | `<View>` |
| 텍스트 | `<p>`, `<span>` | `<Text>` |
| 이미지 | `<img>` | `<Image>` |
| 버튼 | `<button>` | `<Pressable>`, `<TouchableOpacity>` |
| 스크롤 | CSS `overflow` | `<ScrollView>`, `<FlatList>` |
| 스타일 | CSS 파일 또는 Tailwind | `StyleSheet.create()` |
| flex 기본 방향 | row (가로) | column (세로) |

---

## 기본 컴포넌트: View와 Text

`<View>`는 React Native의 `<div>`다. 레이아웃을 담는 컨테이너 역할을 한다.

```jsx
import { View, Text, StyleSheet } from "react-native";

function HelloScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>안녕하세요!</Text>
      <Text style={styles.subtitle}>React Native 첫 화면</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fff",
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#1f2937",
  },
  subtitle: {
    fontSize: 14,
    color: "#6b7280",
    marginTop: 8,
  },
});
```

`StyleSheet.create()`는 웹의 CSS와 다르게 JavaScript 객체를 넘긴다. 속성명은 camelCase(`backgroundColor`, `fontSize`). 단위는 픽셀 수치를 그냥 숫자로 쓰고 `px` 같은 단위를 붙이지 않는다.

---

## Image와 Pressable

```jsx
import { Image, Pressable, Text, View } from "react-native";

function ProfileButton({ onPress }) {
  return (
    <Pressable
      onPress={onPress}
      style={({ pressed }) => ({
        opacity: pressed ? 0.7 : 1,
        backgroundColor: pressed ? "#e5e7eb" : "#3b82f6",
        padding: 12,
        borderRadius: 8,
      })}
    >
      <Text style={{ color: "white", fontWeight: "bold" }}>프로필 보기</Text>
    </Pressable>
  );
}
```

`Pressable`은 누르는 상태(`pressed`)에 반응하는 스타일을 쉽게 줄 수 있다. `style`에 함수를 넘기면 `{ pressed }` 상태를 받아서 조건부 스타일을 적용할 수 있다.

---

## ScrollView

화면보다 내용이 길면 `ScrollView`로 감싸야 한다. 웹에서는 자동으로 스크롤이 되지만, 모바일에서는 명시적으로 `ScrollView`를 써야 한다.

```jsx
import { ScrollView, View, Text } from "react-native";

function LongList() {
  return (
    <ScrollView contentContainerStyle={{ padding: 16 }}>
      {Array.from({ length: 30 }, (_, i) => (
        <View key={i} style={{ padding: 12, borderBottomWidth: 1, borderColor: "#e5e7eb" }}>
          <Text>항목 {i + 1}</Text>
        </View>
      ))}
    </ScrollView>
  );
}
```

리스트가 많으면 `ScrollView` 대신 `FlatList`를 써야 한다. `ScrollView`는 모든 항목을 한 번에 렌더링해서 항목이 많으면 성능이 저하된다.

---

## App.js: 화면 전환

오늘 프로젝트는 탭으로 여러 화면을 전환하는 구조다.

```jsx
// App.js
import { useState } from "react";
import { View, TouchableOpacity, Text, StyleSheet } from "react-native";
import UpDownGameScreen from "./screens/UpDownGameScreen";
import InputExampleScreen from "./screens/InputExampleScreen";
import ProfileCard from "./screens/ProfileCard";
import MoodScreen from "./screens/MoodScreen";

const TABS = [
  { label: "업다운", screen: UpDownGameScreen },
  { label: "입력폼", screen: InputExampleScreen },
  { label: "프로필", screen: ProfileCard },
  { label: "기분", screen: MoodScreen },
];

export default function App() {
  const [activeTab, setActiveTab] = useState(0);
  const ActiveScreen = TABS[activeTab].screen;

  return (
    <View style={styles.container}>
      <View style={styles.content}>
        <ActiveScreen />
      </View>
      <View style={styles.tabBar}>
        {TABS.map((tab, i) => (
          <TouchableOpacity
            key={i}
            onPress={() => setActiveTab(i)}
            style={[styles.tab, activeTab === i && styles.activeTab]}
          >
            <Text style={activeTab === i ? styles.activeLabel : styles.label}>
              {tab.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );
}
```

React Router 없이 상태만으로 화면 전환을 구현했다. 실제 앱에서는 `React Navigation` 라이브러리를 쓰지만, 오늘은 기본 상태 관리로 탭 전환 로직을 직접 만들어봤다.

---

## 플렉스박스 방향 차이

웹에서는 `flex-direction`의 기본값이 `row`(가로 정렬)다. React Native에서는 `column`(세로 정렬)이다. 처음에 이것 때문에 레이아웃이 예상과 다르게 나왔다.

```jsx
// 웹 CSS: flex-direction 기본값 = row
// React Native: flex-direction 기본값 = column

// 모바일에서 가로로 나열하려면 명시해야 함
<View style={{ flexDirection: "row" }}>
  <Text>왼쪽</Text>
  <Text>오른쪽</Text>
</View>
```

`flex: 1`은 웹과 동일하게 부모 공간을 채운다. `justifyContent`, `alignItems`도 동일하게 작동한다.

---

## 헷갈렸던 점

**Text 안에만 텍스트를 쓸 수 있다**: React Native에서 `<View>` 안에 문자열을 직접 쓰면 에러가 난다. 반드시 `<Text>`로 감싸야 한다. 웹에서는 `<div>텍스트</div>`가 되지만 모바일에서는 안 된다.

**스타일 단위**: React Native의 숫자 단위는 dp(density-independent pixel)다. 기기 화면 밀도에 따라 실제 픽셀로 자동 변환된다.

**`SafeAreaView`**: 아이폰의 노치나 하단 바 영역과 겹치지 않으려면 `SafeAreaView`로 감싸야 한다. 처음에 이걸 안 써서 내용이 상태바와 겹치는 걸 경험했다.

---

## 복습용 질문

<details>
<summary>1. React Native에서 Text 컴포넌트 없이 문자열을 출력할 수 없는 이유는?</summary>

React Native는 브라우저가 아니라 네이티브 모바일 환경에서 실행된다. 모바일 네이티브에서는 텍스트를 특정 컨테이너(Text 컴포넌트)에 담아서 렌더링해야 한다. HTML처럼 자동으로 텍스트 노드를 처리하는 환경이 아니다.

</details>

<details>
<summary>2. FlatList가 ScrollView보다 많은 항목에 적합한 이유는?</summary>

FlatList는 화면에 보이는 항목만 렌더링하는 가상화(virtualization) 방식이다. 1000개 항목이 있어도 화면에 보이는 20개 정도만 렌더링한다. ScrollView는 모든 항목을 한 번에 렌더링해서 항목이 많으면 초기 로딩이 느리고 메모리를 많이 쓴다.

</details>

<details>
<summary>3. React Native에서 flex-direction 기본값이 column인 이유는?</summary>

모바일 앱은 세로 방향으로 스크롤하는 구조가 기본이기 때문이다. 콘텐츠가 위에서 아래로 쌓이는 흐름이 자연스러워서 column이 기본값으로 설정되어 있다.

</details>

---

## 한 줄 정리

같은 React인데 완전히 새로운 환경에 들어온 느낌이다. 문법은 익숙하지만 컴포넌트 이름, 스타일 방식, 레이아웃 기본값이 다 다르다. 내일은 모바일에서 사용자 입력을 처리하는 방법을 배운다.
