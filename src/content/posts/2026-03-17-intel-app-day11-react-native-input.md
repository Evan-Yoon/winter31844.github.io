---
title: "[App Dev] Day 11: React Native 사용자 입력, 모바일 UX를 손으로 익히다"
slug: intel-app-day11-react-native-input
date: 2026-03-17
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 11일차. TextInput, 키보드 처리, 다양한 입력 유형을 모바일 폼으로 직접 구현하며 모바일 UX 감각을 익혔다."
thumbnail: ""
tags:
  - react-native
  - textinput
  - keyboard
  - ux
  - mobile
  - bootcamp
readTime: 10
series: "Intel App Dev 2026"
seriesOrder: 11
featured: false
draft: false
toc: true
---

## 모바일 입력은 웹과 다르다

웹에서 폼은 마우스 클릭과 키보드로 조작한다. 모바일에서는 터치와 소프트 키보드를 쓴다. 소프트 키보드는 화면 절반을 차지하고, 숫자 입력인지 이메일 입력인지에 따라 다른 키보드가 뜬다. 비밀번호는 입력 중에만 보이고 바로 가려진다.

이런 것들이 웹에서는 HTML `input type`만 바꾸면 됐지만, 모바일에서는 하나하나 설정해야 한다. 오늘은 `InputExampleScreen.js`를 만들면서 모바일 입력 UX를 직접 다뤘다.

---

## TextInput 기본

React Native에서 텍스트 입력은 `TextInput` 컴포넌트를 쓴다.

```jsx
import { TextInput, View, Text, StyleSheet } from "react-native";
import { useState } from "react";

function BasicInput() {
  const [value, setValue] = useState("");

  return (
    <View>
      <TextInput
        value={value}
        onChangeText={setValue}     // e.target.value 없이 바로 value 전달
        placeholder="입력하세요"
        style={styles.input}
      />
      <Text>입력값: {value}</Text>
    </View>
  );
}
```

웹의 `onChange={(e) => setValue(e.target.value)}`와 달리, `onChangeText`는 텍스트 값을 바로 넘겨준다. `e.target.value`를 꺼낼 필요가 없다.

---

## 다양한 키보드 타입

`keyboardType` 속성으로 어떤 키보드가 올라올지 설정한다.

```jsx
// 기본 텍스트 키보드
<TextInput keyboardType="default" />

// 숫자 전용 키보드
<TextInput keyboardType="numeric" />

// 이메일 입력 (@ 키가 강조됨)
<TextInput keyboardType="email-address" />

// 전화번호
<TextInput keyboardType="phone-pad" />

// URL 입력
<TextInput keyboardType="url" />
```

---

## InputExampleScreen: 다중 입력 폼

오늘 만든 `InputExampleScreen.js`는 여러 입력 필드를 포함한 실제 폼이다.

```jsx
import { useState } from "react";
import {
  View, Text, TextInput, StyleSheet, ScrollView,
} from "react-native";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";

function InputExampleScreen() {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [search, setSearch] = useState("");
  const [bio, setBio] = useState("");

  return (
    <KeyboardAwareScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
    >
      <Text style={styles.header}>사용자 정보 입력</Text>

      {/* 아이디 */}
      <Text style={styles.label}>아이디 (최대 10자)</Text>
      <TextInput
        value={userId}
        onChangeText={setUserId}
        maxLength={10}
        placeholder="아이디를 입력하세요"
        style={styles.input}
        autoCapitalize="none"
      />

      {/* 비밀번호 */}
      <Text style={styles.label}>비밀번호</Text>
      <TextInput
        value={password}
        onChangeText={setPassword}
        secureTextEntry={true}
        placeholder="비밀번호를 입력하세요"
        style={styles.input}
      />

      {/* 이메일 */}
      <Text style={styles.label}>이메일</Text>
      <TextInput
        value={email}
        onChangeText={setEmail}
        keyboardType="email-address"
        autoCapitalize="none"
        placeholder="example@email.com"
        style={styles.input}
      />

      {/* 전화번호 */}
      <Text style={styles.label}>전화번호</Text>
      <TextInput
        value={phone}
        onChangeText={setPhone}
        keyboardType="phone-pad"
        placeholder="010-0000-0000"
        style={styles.input}
      />

      {/* 우편번호 */}
      <Text style={styles.label}>우편번호</Text>
      <TextInput
        value={postalCode}
        onChangeText={setPostalCode}
        keyboardType="numeric"
        maxLength={5}
        placeholder="12345"
        style={styles.input}
      />

      {/* 검색 */}
      <Text style={styles.label}>검색</Text>
      <TextInput
        value={search}
        onChangeText={setSearch}
        returnKeyType="search"
        placeholder="검색어를 입력하세요"
        style={styles.input}
        clearButtonMode="while-editing"
      />

      {/* 자기소개 (여러 줄) */}
      <Text style={styles.label}>자기소개</Text>
      <TextInput
        value={bio}
        onChangeText={setBio}
        multiline={true}
        numberOfLines={4}
        placeholder="자기소개를 입력하세요..."
        style={[styles.input, styles.textArea]}
        textAlignVertical="top"
      />
    </KeyboardAwareScrollView>
  );
}
```

---

## KeyboardAwareScrollView가 필요한 이유

소프트 키보드가 올라오면 화면 아래 절반을 가린다. 아래쪽 입력 필드를 누르면 키보드에 가려져서 내가 뭘 입력하는지 보이지 않는다.

`react-native-keyboard-aware-scroll-view`를 쓰면 키보드가 올라올 때 현재 포커스된 input이 보이도록 자동으로 화면을 위로 스크롤해준다.

```bash
npm install react-native-keyboard-aware-scroll-view
```

처음에 이 라이브러리 없이 폼을 만들었다가, 아래쪽 입력 필드가 키보드에 가려지는 걸 직접 경험했다. 특히 자기소개 같은 마지막 필드는 키보드가 올라와도 전혀 보이지 않았다. 설치 후 바로 해결됐다.

---

## 주요 TextInput 속성들

```jsx
<TextInput
  // 입력 제한
  maxLength={10}               // 최대 글자 수
  multiline={true}             // 여러 줄 입력 가능
  numberOfLines={4}            // 기본 표시 줄 수

  // 보안
  secureTextEntry={true}       // 비밀번호 마스킹

  // 키보드
  keyboardType="email-address" // 키보드 종류
  returnKeyType="done"         // 완료 버튼 텍스트 (done/search/next/go)
  autoCapitalize="none"        // 자동 대문자 방지

  // 이벤트
  onSubmitEditing={() => {}}   // 완료 버튼 눌렀을 때
  onFocus={() => {}}           // 포커스될 때
  onBlur={() => {}}            // 포커스 잃을 때

  // iOS 전용
  clearButtonMode="while-editing" // 입력 중 ✕ 버튼 표시

  // Android 전용
  textAlignVertical="top"      // multiline에서 텍스트 상단 정렬
/>
```

---

## returnKeyType으로 다음 필드 이동

모바일 폼에서 키보드의 완료 버튼을 누르면 다음 필드로 넘어가는 UX가 자연스럽다.

```jsx
import { useRef } from "react";

function FormWithFocus() {
  const emailRef = useRef(null);
  const passwordRef = useRef(null);

  return (
    <>
      <TextInput
        placeholder="이메일"
        returnKeyType="next"
        onSubmitEditing={() => passwordRef.current?.focus()}
        blurOnSubmit={false}
      />
      <TextInput
        ref={passwordRef}
        placeholder="비밀번호"
        secureTextEntry
        returnKeyType="done"
      />
    </>
  );
}
```

`ref`를 사용해서 완료 버튼을 누르면 다음 입력 필드로 포커스를 이동한다. `blurOnSubmit={false}`는 엔터를 눌러도 키보드가 닫히지 않게 해서 다음 필드로 이동하는 흐름을 유지한다.

---

## 스타일시트

```jsx
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f9fafb",
  },
  content: {
    padding: 20,
    paddingBottom: 40,
  },
  header: {
    fontSize: 22,
    fontWeight: "bold",
    marginBottom: 24,
    color: "#111827",
  },
  label: {
    fontSize: 14,
    fontWeight: "600",
    color: "#374151",
    marginBottom: 6,
  },
  input: {
    borderWidth: 1,
    borderColor: "#d1d5db",
    borderRadius: 8,
    padding: 12,
    fontSize: 15,
    backgroundColor: "#fff",
    marginBottom: 16,
    color: "#111827",
  },
  textArea: {
    height: 100,
    textAlignVertical: "top",
  },
});
```

React Native 스타일은 JavaScript 객체다. 속성명은 모두 camelCase고, 숫자 값에는 단위를 붙이지 않는다. 스타일을 컴포넌트 바깥의 `StyleSheet.create()`로 분리하면 성능상 이점이 있고 코드도 깔끔해진다.

---

## 헷갈렸던 점

**`onChangeText` vs `onChange`**: React Native의 `onChangeText`는 텍스트 값을 바로 전달한다. `onChange`는 이벤트 객체를 전달한다. 일반적으로 `onChangeText`가 더 편리하다.

**multiline의 높이**: `multiline={true}`만 쓰면 입력할수록 input이 자동으로 커진다. 고정 높이를 원하면 `style={{ height: 100 }}`처럼 직접 지정해야 한다.

**Android와 iOS 차이**: `clearButtonMode`는 iOS에서만 작동한다. `textAlignVertical`은 Android에서만 효과가 있다. 크로스 플랫폼이라고는 하지만 이런 차이가 존재한다.

---

## 복습용 질문

<details>
<summary>1. KeyboardAwareScrollView를 써야 하는 상황은?</summary>

화면 아래쪽에 입력 필드가 있을 때, 소프트 키보드가 올라오면 해당 필드가 가려질 수 있다. KeyboardAwareScrollView는 포커스된 입력 필드가 키보드 위에 보이도록 자동으로 스크롤을 조정한다.

</details>

<details>
<summary>2. secureTextEntry를 true로 설정하면 어떻게 되는가?</summary>

입력한 문자가 즉시 점(•)으로 대체된다. 비밀번호처럼 화면에 노출되면 안 되는 입력에 쓴다. iOS에서는 입력 직후 잠깐 문자가 보였다가 가려지는 애니메이션이 기본 적용된다.

</details>

<details>
<summary>3. returnKeyType="next"로만 하면 다음 필드로 이동이 되는가?</summary>

아니다. `returnKeyType="next"`는 키보드 버튼 레이블만 바꾼다. 실제 포커스 이동은 `ref`와 `onSubmitEditing`을 조합해서 직접 구현해야 한다.

</details>

---

## 한 줄 정리

모바일 입력은 생각보다 세부적인 처리가 많다. 키보드 타입, 포커스 이동, 키보드에 가려지는 문제까지. 웹에서 `<input type="email">`로 끝나던 것이 모바일에서는 여러 속성 조합이 필요하다. 내일은 데이터를 리스트로 렌더링하는 방법을 배운다.
