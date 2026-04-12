---
title: "[App Dev] Day 12: React Native 데이터 기반 렌더링, 리스트와 카드를 만들다"
slug: intel-app-day12-react-native-data-rendering
date: 2026-03-18
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 12일차. FlatList, Array.find, 조건부 렌더링으로 데이터 기반 UI를 구성하고 글래스모피즘 프로필 카드를 완성했다."
thumbnail: ""
tags:
  - react-native
  - flatlist
  - data-rendering
  - glassmorphism
  - mobile
  - bootcamp
readTime: 10
series: "Intel App Dev 2026"
seriesOrder: 12
featured: false
draft: false
toc: true
---

## 데이터가 UI를 결정하게 만든다

앱에서 보이는 대부분의 화면은 데이터에서 온다. 연락처 앱의 목록, SNS의 피드, 기분 선택 화면의 이모지들. 코드에서 이 데이터를 직접 하드코딩하지 않고, **데이터 배열을 정의하면 그 배열이 UI를 만들어내는 구조**를 갖춰야 실제 앱다워진다.

오늘은 두 가지를 만들었다. 기분을 선택하면 이모지와 메시지가 바뀌는 `MoodScreen`, 그리고 글래스모피즘 효과를 적용한 `ProfileCard`. 하나는 데이터 기반 렌더링의 핵심 패턴을, 하나는 React Native의 시각적 가능성을 보여줬다.

---

## FlatList: 리스트 렌더링의 표준

항목이 많은 목록은 `FlatList`를 써야 한다. 화면에 보이는 항목만 렌더링하는 가상화가 내장되어 있어서, 수천 개의 항목도 부드럽게 스크롤된다.

```jsx
import { FlatList, View, Text, StyleSheet } from "react-native";

const DATA = [
  { id: "1", name: "철수", age: 25 },
  { id: "2", name: "영희", age: 30 },
  { id: "3", name: "민수", age: 28 },
];

function UserList() {
  const renderItem = ({ item }) => (
    <View style={styles.item}>
      <Text style={styles.name}>{item.name}</Text>
      <Text style={styles.age}>{item.age}세</Text>
    </View>
  );

  return (
    <FlatList
      data={DATA}
      keyExtractor={(item) => item.id}
      renderItem={renderItem}
      ItemSeparatorComponent={() => <View style={styles.separator} />}
      ListEmptyComponent={<Text style={styles.empty}>항목이 없습니다</Text>}
    />
  );
}
```

| 속성 | 역할 |
|------|------|
| `data` | 렌더링할 배열 |
| `keyExtractor` | 각 항목의 고유 key |
| `renderItem` | 각 항목을 그리는 함수 |
| `ItemSeparatorComponent` | 항목 사이의 구분선 |
| `ListEmptyComponent` | data가 빈 배열일 때 표시 |
| `ListHeaderComponent` | 리스트 상단 고정 헤더 |

---

## MoodScreen: 데이터에서 UI로

`MoodScreen.js`는 기분 데이터 배열을 정의하고, 선택한 기분에 맞는 이모지와 메시지를 보여주는 화면이다.

```jsx
import { useState } from "react";
import { View, Text, FlatList, StyleSheet } from "react-native";
import MoodButton from "../components/MoodButton";

const MOOD_DATA = [
  { id: "happy",   emoji: "😊", label: "행복해요", message: "오늘 정말 좋은 날이네요!", color: "#fbbf24" },
  { id: "neutral", emoji: "😐", label: "그냥 그래요", message: "평범한 하루도 소중해요.",  color: "#6b7280" },
  { id: "sad",     emoji: "😢", label: "슬퍼요",   message: "힘내세요. 다 잘 될 거예요.", color: "#60a5fa" },
  { id: "angry",   emoji: "😠", label: "화났어요",  message: "깊게 숨을 쉬어봐요.",       color: "#f87171" },
  { id: "tired",   emoji: "😴", label: "피곤해요",  message: "오늘 푹 쉬어요.",           color: "#a78bfa" },
  { id: "excited", emoji: "🤩", label: "신나요",    message: "그 에너지 최고예요!",        color: "#34d399" },
];

export default function MoodScreen() {
  const [selectedMood, setSelectedMood] = useState(null);

  const handleSelect = (moodId) => {
    const found = MOOD_DATA.find((m) => m.id === moodId);
    setSelectedMood(found);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>지금 기분이 어떠세요?</Text>

      {/* 선택된 기분 표시 */}
      {selectedMood ? (
        <View style={[styles.result, { borderColor: selectedMood.color }]}>
          <Text style={styles.emoji}>{selectedMood.emoji}</Text>
          <Text style={styles.message}>{selectedMood.message}</Text>
        </View>
      ) : (
        <View style={styles.placeholder}>
          <Text style={styles.placeholderText}>기분을 선택해보세요</Text>
        </View>
      )}

      {/* 기분 버튼 그리드 */}
      <FlatList
        data={MOOD_DATA}
        keyExtractor={(item) => item.id}
        numColumns={3}
        renderItem={({ item }) => (
          <MoodButton
            mood={item}
            isSelected={selectedMood?.id === item.id}
            onPress={() => handleSelect(item.id)}
          />
        )}
        contentContainerStyle={styles.grid}
      />
    </View>
  );
}
```

`Array.find()`로 선택된 기분 데이터를 찾고, 그 데이터가 UI를 결정한다. `numColumns={3}`으로 FlatList를 3열 그리드로 배치했다.

---

## MoodButton 컴포넌트

```jsx
// components/MoodButton.js
import { Pressable, Text, StyleSheet } from "react-native";

export default function MoodButton({ mood, isSelected, onPress }) {
  return (
    <Pressable
      onPress={onPress}
      style={[
        styles.button,
        isSelected && { borderColor: mood.color, borderWidth: 2, backgroundColor: mood.color + "22" },
      ]}
    >
      <Text style={styles.emoji}>{mood.emoji}</Text>
      <Text style={styles.label}>{mood.label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  button: {
    flex: 1,
    margin: 6,
    padding: 12,
    borderRadius: 12,
    alignItems: "center",
    backgroundColor: "#f3f4f6",
    borderWidth: 2,
    borderColor: "transparent",
  },
  emoji: { fontSize: 28, marginBottom: 4 },
  label: { fontSize: 11, color: "#374151" },
});
```

선택된 버튼은 해당 색상으로 테두리가 생기고 배경에 반투명 색상이 들어간다. `mood.color + "22"`는 hex 색상에 투명도를 추가하는 방법이다.

---

## ProfileCard: 글래스모피즘 효과

`ProfileCard.js`는 배경 이미지 위에 블러 처리된 반투명 카드를 올리는 글래스모피즘 효과를 구현한다.

```jsx
import {
  View, Text, ImageBackground, StyleSheet, TouchableOpacity,
} from "react-native";
import { LinearGradient } from "expo-linear-gradient";
import { BlurView } from "expo-blur";

const PROFILE = {
  name: "Evan Yoon",
  bio: "AI Developer | HR → Dev",
  followers: 1240,
  following: 380,
  posts: 47,
  backgroundImage: "https://images.unsplash.com/photo-1519681393784-d120267933ba",
};

export default function ProfileCard() {
  return (
    <ImageBackground
      source={{ uri: PROFILE.backgroundImage }}
      style={styles.background}
      resizeMode="cover"
    >
      {/* 그라데이션 오버레이 */}
      <LinearGradient
        colors={["transparent", "rgba(0,0,0,0.6)"]}
        style={StyleSheet.absoluteFillObject}
      />

      {/* 글래스 카드 */}
      <BlurView intensity={30} tint="dark" style={styles.card}>
        {/* 아바타 */}
        <View style={styles.avatar}>
          <Text style={styles.avatarText}>EY</Text>
        </View>

        <Text style={styles.name}>{PROFILE.name}</Text>
        <Text style={styles.bio}>{PROFILE.bio}</Text>

        {/* 통계 */}
        <View style={styles.stats}>
          {[
            { label: "게시물", value: PROFILE.posts },
            { label: "팔로워", value: PROFILE.followers.toLocaleString() },
            { label: "팔로잉", value: PROFILE.following },
          ].map(({ label, value }) => (
            <View key={label} style={styles.stat}>
              <Text style={styles.statValue}>{value}</Text>
              <Text style={styles.statLabel}>{label}</Text>
            </View>
          ))}
        </View>

        {/* 버튼 */}
        <TouchableOpacity style={styles.followBtn}>
          <Text style={styles.followText}>팔로우</Text>
        </TouchableOpacity>
      </BlurView>
    </ImageBackground>
  );
}
```

`LinearGradient`와 `BlurView`는 Expo에서 제공하는 고급 컴포넌트다.

```bash
npx expo install expo-linear-gradient expo-blur
```

`expo install`을 쓰면 현재 Expo 버전에 호환되는 패키지를 자동으로 설치해준다.

---

## 조건부 스타일링 패턴

React Native에서 스타일 배열에 조건부로 객체를 넣는 패턴을 자주 쓴다.

```jsx
<View
  style={[
    styles.base,           // 항상 적용
    isActive && styles.active,  // isActive가 true일 때만
    { borderColor: color },     // 동적 값
  ]}
/>
```

`false`, `null`, `undefined`는 스타일 배열에서 무시된다. 조건이 false면 해당 스타일이 적용되지 않는다.

---

## 헷갈렸던 점

**FlatList의 numColumns**: `numColumns`를 바꾸면 에러가 날 수 있다. 컬럼 수를 변경하면 `key`가 달라지기 때문이다. 개발 중에 numColumns를 바꿀 때는 앱을 재시작해야 하는 경우가 있었다.

**BlurView의 iOS/Android 차이**: `BlurView`의 블러 품질은 iOS에서 더 자연스럽다. Android에서는 블러 효과가 iOS보다 덜 부드럽게 보일 수 있다. 실기기로 테스트했을 때 차이를 직접 확인했다.

**ImageBackground의 자식 요소**: `ImageBackground`는 배경에 이미지를 깔고 그 위에 자식 컴포넌트를 올린다. 자식이 이미지를 덮어씌우는 방식이라서, `StyleSheet.absoluteFillObject`로 그라데이션이 이미지 전체를 채우게 해야 했다.

---

## 복습용 질문

<details>
<summary>1. FlatList와 ScrollView의 주요 차이는?</summary>

FlatList는 화면에 보이는 항목만 렌더링하는 가상화를 사용한다. 항목이 많아도 성능이 유지된다. ScrollView는 모든 항목을 한 번에 렌더링한다. 항목 수가 고정이고 적을 때는 ScrollView, 동적으로 많은 항목을 표시할 때는 FlatList를 쓴다.

</details>

<details>
<summary>2. FlatList에서 numColumns를 쓰면 renderItem 스타일에 무엇을 추가해야 하는가?</summary>

각 항목에 `flex: 1`을 줘야 열 너비를 균등하게 나눈다. 추가로 margin이나 padding을 줄 때 `numColumns`와 맞춰 계산해야 좌우 여백이 자연스럽게 맞아떨어진다.

</details>

<details>
<summary>3. Array.find와 Array.filter의 차이는?</summary>

`find`는 조건에 맞는 첫 번째 요소 하나를 반환한다. 없으면 `undefined`를 반환한다. `filter`는 조건에 맞는 모든 요소를 배열로 반환한다. 없으면 빈 배열을 반환한다.

</details>

---

## 한 줄 정리

데이터 배열을 정의하면 UI가 자동으로 따라오는 구조가 앱 개발의 기본이다. 내일은 AI 모델을 앱과 연결하고 카메라·갤러리 기능까지 다룬다.
