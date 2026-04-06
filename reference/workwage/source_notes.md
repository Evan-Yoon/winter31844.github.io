# Source notes

이 패키지는 아래 자료를 바탕으로 정리되었습니다.

1. 업로드된 Word 문서
- 파일명: 플러터 앱 아이디어 제안.docx
- 용도: 아이디어 발상, MVP 축소 과정, Stitch/Jules 프롬프트, 노동법 계산 예시, 초기 시행착오 정리

2. 공개 GitHub 저장소
- 저장소: Evan-Yoon/salery_calculator
- 활용 범위:
  - README 공개 설명
  - 공개 폴더 구조(lib/models, providers, screens, utils, widgets)
  - 공개 commit history 제목과 날짜

주의:
- commit_map.csv는 “공개 커밋 제목” 기준 요약입니다.
- 각 커밋의 실제 diff 내용까지 정밀하게 읽은 것은 아닙니다.
- 따라서 블로그 초안 작성 시에는 commit 제목을 “개발 흐름의 단서”로 사용하고,
  실제 파일 내용을 직접 확인 가능한 경우에는 Antigravity/Claude가 저장소 내용을 추가로 읽어 세밀하게 보완하는 것이 가장 좋습니다.

추천 사용 순서:
1. manifest.csv 확인
2. chunk 01~09 순서대로 Claude에 투입
3. commit_map.csv는 phase 설명 보강용으로 함께 투입
4. prompts 폴더의 1차~4차 프롬프트를 순서대로 사용
