---
title: "[App Dev] Day 13: React Native 모델 서빙과 갤러리·카메라 연동"
slug: intel-app-day13-react-native-model-camera
date: 2026-03-19
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 13일차. Lottie 애니메이션, 업다운 게임 구현, 이미지 피커와 카메라 연동까지 React Native 심화 기능을 다뤘다."
thumbnail: ""
tags:
  - react-native
  - lottie
  - camera
  - image-picker
  - model-serving
  - bootcamp
readTime: 11
series: "Intel App Dev 2026"
seriesOrder: 13
featured: false
draft: false
toc: true
---

## 앱의 완성도를 높이는 것들

오늘은 세 가지를 다뤘다. 숫자 맞히기 게임을 만들면서 **Lottie 애니메이션**을 처음 써봤고, 갤러리에서 이미지를 선택하거나 카메라로 직접 촬영하는 **이미지 피커**를 연동했다. 마지막으로 FastAPI 서버에 이미지를 보내서 AI 모델의 결과를 받는 **모델 서빙 연동** 흐름을 실습했다.

기능들이 연결되면서 실제 앱의 데이터 흐름이 어떻게 구성되는지 파악할 수 있었다.

---

## UpDownGameScreen: 업다운 게임

1부터 100 사이 숫자를 맞히는 게임이다. 틀리면 "UP" 또는 "DOWN"으로 힌트를 주고, 맞히면 Lottie 축하 애니메이션이 재생된다.

```jsx
import { useState } from "react";
import { View, Text, TextInput, Pressable, StyleSheet } from "react-native";
import LottieView from "lottie-react-native";

export default function UpDownGameScreen() {
  const [input, setInput] = useState("");
  const [result, setResult] = useState("");
  const [attempts, setAttempts] = useState(0);
  const [isGameOver, setIsGameOver] = useState(false);
  const [targetNumber] = useState(() => Math.floor(Math.random() * 100) + 1);

  const handleGuess = () => {
    const guess = Number(input);

    if (!guess || guess < 1 || guess > 100) {
      setResult("1~100 사이 숫자를 입력하세요");
      return;
    }

    setAttempts((prev) => prev + 1);

    if (guess < targetNumber) {
      setResult("📈 UP! 더 큰 수를 입력하세요");
    } else if (guess > targetNumber) {
      setResult("📉 DOWN! 더 작은 수를 입력하세요");
    } else {
      setIsGameOver(true);
    }

    setInput("");
  };

  const handleReset = () => {
    setIsGameOver(false);
    setAttempts(0);
    setResult("");
    setInput("");
  };

  if (isGameOver) {
    return (
      <View style={styles.successContainer}>
        <LottieView
          source={require("../assets/confetti.json")}
          autoPlay
          loop={false}
          style={styles.lottie}
        />
        <Text style={styles.successText}>🎉 정답!</Text>
        <Text style={styles.attemptsText}>{attempts}번 만에 맞혔어요</Text>
        <Pressable onPress={handleReset} style={styles.resetBtn}>
          <Text style={styles.resetText}>다시 하기</Text>
        </Pressable>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>업다운 게임</Text>
      <Text style={styles.sub}>1~100 사이 숫자를 맞혀보세요</Text>

      <TextInput
        value={input}
        onChangeText={setInput}
        keyboardType="numeric"
        placeholder="숫자 입력"
        style={styles.input}
        onSubmitEditing={handleGuess}
        returnKeyType="go"
      />

      <Pressable onPress={handleGuess} style={styles.button}>
        <Text style={styles.buttonText}>확인</Text>
      </Pressable>

      {result !== "" && (
        <Text style={styles.result}>{result}</Text>
      )}

      <Text style={styles.attempts}>시도 횟수: {attempts}</Text>
    </View>
  );
}
```

`targetNumber`를 `useState`의 초기값 함수로 설정해서 최초 렌더링 때 한 번만 랜덤 값을 생성한다. 리렌더링마다 숫자가 바뀌면 안 되기 때문이다.

---

## Lottie 애니메이션

Lottie는 Adobe After Effects 애니메이션을 JSON 파일로 내보내서 앱에서 재생하는 라이브러리다. GIF보다 가볍고 해상도에 무관하게 선명하다.

```bash
npx expo install lottie-react-native
```

`lottiefiles.com`에서 무료 JSON 파일을 내려받아 `assets/` 폴더에 넣고 쓴다.

```jsx
import LottieView from "lottie-react-native";

<LottieView
  source={require("../assets/confetti.json")}
  autoPlay           // 자동 재생
  loop={false}       // 한 번만 재생
  style={{ width: 300, height: 300 }}
/>
```

업다운 게임에서 정답을 맞혔을 때 폭죽 Lottie가 재생된다. 단순한 흰 화면에 텍스트만 있던 게임이 갑자기 훨씬 완성도 있어 보이게 됐다. 애니메이션 하나가 UX를 얼마나 바꾸는지 직접 느꼈다.

---

## 이미지 피커: 갤러리에서 선택

`expo-image-picker`로 갤러리에서 이미지를 선택하거나 카메라로 촬영할 수 있다.

```bash
npx expo install expo-image-picker
```

```jsx
import * as ImagePicker from "expo-image-picker";
import { useState } from "react";
import { View, Image, Pressable, Text, Alert } from "react-native";

export default function ImagePickerExample() {
  const [image, setImage] = useState(null);

  const pickFromGallery = async () => {
    // 권한 요청
    const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
    if (status !== "granted") {
      Alert.alert("권한 필요", "갤러리 접근 권한이 필요합니다.");
      return;
    }

    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,   // 크롭 편집 허용
      aspect: [1, 1],        // 정사각형 크롭
      quality: 0.8,          // 이미지 품질 (0~1)
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  const takePhoto = async () => {
    const { status } = await ImagePicker.requestCameraPermissionsAsync();
    if (status !== "granted") {
      Alert.alert("권한 필요", "카메라 접근 권한이 필요합니다.");
      return;
    }

    const result = await ImagePicker.launchCameraAsync({
      allowsEditing: true,
      aspect: [4, 3],
      quality: 0.8,
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  return (
    <View style={{ flex: 1, alignItems: "center", padding: 20 }}>
      {image ? (
        <Image
          source={{ uri: image }}
          style={{ width: 280, height: 280, borderRadius: 12 }}
        />
      ) : (
        <View style={{ width: 280, height: 280, backgroundColor: "#f3f4f6", borderRadius: 12, justifyContent: "center", alignItems: "center" }}>
          <Text style={{ color: "#9ca3af" }}>이미지를 선택하세요</Text>
        </View>
      )}

      <Pressable onPress={pickFromGallery} style={styles.btn}>
        <Text style={styles.btnText}>갤러리에서 선택</Text>
      </Pressable>

      <Pressable onPress={takePhoto} style={[styles.btn, { backgroundColor: "#059669" }]}>
        <Text style={styles.btnText}>카메라로 촬영</Text>
      </Pressable>
    </View>
  );
}
```

`requestMediaLibraryPermissionsAsync()`와 `requestCameraPermissionsAsync()`로 사용자 권한을 먼저 요청한다. 권한 없이는 갤러리나 카메라에 접근할 수 없다. 이 과정이 웹과 가장 다른 부분이다.

---

## AI 모델 서빙: 이미지를 서버로 보내기

선택한 이미지를 FastAPI 서버로 보내서 AI 모델의 분류 결과를 받는 흐름이다.

```jsx
const analyzeImage = async (imageUri) => {
  setLoading(true);
  try {
    // 이미지를 FormData로 묶어서 전송
    const formData = new FormData();
    formData.append("file", {
      uri: imageUri,
      type: "image/jpeg",
      name: "photo.jpg",
    });

    const response = await fetch("http://localhost:8000/predict", {
      method: "POST",
      body: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    const result = await response.json();
    setPrediction(result);
  } catch (err) {
    Alert.alert("오류", "서버 연결에 실패했습니다.");
  } finally {
    setLoading(false);
  }
};
```

FastAPI 서버에서는 이미지를 받아 모델로 추론한다.

```python
# server/main.py
from fastapi import FastAPI, File, UploadFile
from PIL import Image
import io

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    contents = await file.read()
    image = Image.open(io.BytesIO(contents))

    # 실제 모델 추론 (예시)
    # result = model.predict(preprocess(image))

    return {
        "label": "고양이",
        "confidence": 0.94,
    }
```

이미지 데이터를 `FormData`에 넣어서 POST로 전송하는 방식이다. JSON이 아닌 파일 데이터를 보낼 때는 `multipart/form-data` 방식을 쓴다.

---

## 전체 흐름 정리

오늘 다룬 기능들의 흐름을 합치면 이렇게 된다.

```
갤러리/카메라
    ↓ 이미지 선택 (expo-image-picker)
React Native 앱
    ↓ FormData로 이미지 전송 (fetch POST)
FastAPI 서버 (/predict)
    ↓ 이미지 전처리 + 모델 추론
JSON 응답 (label, confidence)
    ↓ 결과 수신
React Native 앱 화면에 표시
```

이 흐름이 Walkmate 같은 실제 AI 앱의 기본 아키텍처다. 모델이 온디바이스면 FastAPI 없이 바로 추론할 수도 있고, 모델이 서버에 있으면 이 흐름으로 연결한다.

---

## 헷갈렸던 점

**권한 요청 타이밍**: 앱 실행 직후가 아니라 실제로 기능을 사용하려고 할 때 권한을 요청하는 게 UX상 더 자연스럽다. 처음부터 모든 권한을 한꺼번에 요청하면 사용자가 거부할 가능성이 높다.

**Expo Go와 실제 빌드의 차이**: Expo Go로 개발할 때는 일부 네이티브 기능이 제한될 수 있다. 카메라, 갤러리 같은 기능은 Expo Go에서도 잘 됐지만, Lottie 같은 경우 빌드 방식에 따라 설정이 달라지는 경우가 있다.

**FormData의 Content-Type 헤더**: `fetch`에 `Content-Type: multipart/form-data`를 명시하면 오히려 boundary 값이 빠져서 서버에서 파싱을 못 하는 경우가 있다. `Content-Type` 헤더를 생략하면 `fetch`가 자동으로 올바른 boundary를 포함한 헤더를 설정해준다.

---

## 복습용 질문

<details>
<summary>1. Lottie 애니메이션이 GIF보다 앱에서 유리한 이유는?</summary>

JSON 기반이라 파일 크기가 작고, 벡터 방식이라 어떤 해상도에서도 선명하다. 재생 속도, 루프, 특정 프레임 제어가 가능하고 코드에서 동적으로 색상이나 레이어를 변경할 수도 있다.

</details>

<details>
<summary>2. 이미지를 서버로 전송할 때 JSON 대신 FormData를 쓰는 이유는?</summary>

JSON은 텍스트 기반이라 바이너리 데이터(이미지, 파일)를 직접 담을 수 없다. Base64로 인코딩하면 가능하지만 용량이 약 1.3배 늘어난다. multipart/form-data는 바이너리 데이터를 효율적으로 전송하는 방식이다.

</details>

<details>
<summary>3. expo-image-picker에서 result.canceled를 확인하는 이유는?</summary>

사용자가 갤러리나 카메라 화면에서 취소 버튼을 누를 수 있다. 이때 result.canceled가 true로 반환된다. 확인하지 않으면 undefined인 result.assets를 접근해서 오류가 난다.

</details>

---

## 한 줄 정리

갤러리에서 이미지를 골라 서버로 보내고 AI 결과를 다시 화면에 보여주는 것까지 직접 연결해봤다. 내일은 3주 과정 전체 총정리다.
