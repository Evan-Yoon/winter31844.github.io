# Evan Yoon Blog — Agent Guide Index

이 폴더는 블로그 글 작성을 담당하는 에이전트를 위한 가이드 모음이다.
글을 작성하기 전에 반드시 아래 순서대로 읽는다.

---

## 읽는 순서

1. **[style.md](../style.md)** — 전역 글쓰기 규칙. 모든 카테고리에 공통 적용.
2. **이 폴더의 카테고리 가이드** — 해당 카테고리 글에만 적용되는 규칙.

카테고리 가이드는 style.md를 대체하지 않는다. 둘 다 따른다.

---

## 카테고리 가이드 목록

| 파일 | 카테고리 | 설명 |
|------|----------|------|
| [guide-study.md](guide-study.md) | `study` | 수업·자격증·자기주도 학습 기록 |
| [guide-project.md](guide-project.md) | `project` | 팀 프로젝트 일지·개인 앱 개발 회고 |
| [guide-explore.md](guide-explore.md) | `explore` | 에세이·현장 방문·신기술 탐방 |
| [guide-ainews.md](guide-ainews.md) | `ai-news` | 주간 AI 카드뉴스 (HTML 포맷 고정) |

---

## 카테고리 선택 기준

```
배운 것을 정리했다          → study
만든 것을 기록했다          → project
경험하고 탐구한 것을 담았다 → explore
이번 주 AI 뉴스를 정리했다  → ai-news
```

---

## 서브카테고리 목록

| 카테고리 | 서브카테고리 | 사용 시점 |
|----------|-------------|-----------|
| study | bootcamp | 부트캠프 수업 일별 기록 |
| study | certification | 자격증 스터디 기록 |
| study | self-study | 자기 주도 학습 / 특정 주제 심화 |
| project | team-project | 다인 팀 프로젝트 개발 일지 |
| project | personal-project | 개인 앱·서비스 개발 기록 또는 회고 |
| explore | essay | 진로·커리어 관련 개인 에세이 |
| explore | field-visit | 기업 방문·현장 견학 후기 |
| explore | new-tech | 개발 도구·기술 탐방 |
| explore | lecture | 외부 특강·세미나 후기 |

---

## 파일 저장 경로

```
# 일반 포스트
src/content/posts/YYYY-MM-DD-slug.md

# AI 뉴스 (ainews 서브폴더 필수)
src/content/posts/ainews/YYYY-MM-DD-weekly-ai-news-ko.md
```

---

## 이미지 경로 패턴

```
# 포스트 이미지
/images/posts/[프로젝트명-소문자]/cover.png
/images/posts/[프로젝트명-소문자]/파일명.png

# AI 뉴스 썸네일
/images/ai-news/YYYY-MM-DD-weekly-overview.svg
```
