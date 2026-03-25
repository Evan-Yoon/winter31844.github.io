#!/usr/bin/env python3
"""
medium_to_blog.py
─────────────────────────────────────────────────────────────────
Medium HTML 내보내기 → devlog Markdown 자동 변환 스크립트

사용법:
  1. Medium 설정 → 내보내기(Export) → ZIP 다운로드
  2. ZIP 압축 해제 후 HTML 파일들을 ./medium_export/ 에 저장
  3. python medium_to_blog.py

출력: ./output/YYYY-MM-DD-slug.md (frontmatter 포함)
─────────────────────────────────────────────────────────────────
"""

import sys
import os
import re
import json
from pathlib import Path
from datetime import datetime

# ── 윈도우 터미널 한글 깨짐 완벽 방지 ─────────────────────────
if sys.stdout.encoding.lower() != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
        sys.stderr.reconfigure(encoding='utf-8')
    except AttributeError:
        pass

# ── 의존성 체크 후 import ──────────────────────────────────────
try:
    from bs4 import BeautifulSoup
    import markdownify
    from slugify import slugify
except ImportError:
    print("\n[알림] 필수 패키지가 설치되지 않았습니다.")
    print("터미널을 열고 다음 명령어를 먼저 실행해 주세요:")
    print("pip install beautifulsoup4 markdownify python-slugify\n")
    sys.exit(1)

# ── 카테고리 추측 규칙 ─────────────────────────────────────────
CATEGORY_RULES = {
    "tech": [
        "intel", "ai", "openvino", "gaudi", "nvidia", "visit",
        "방문", "인텔", "lab", "tech", "기술", "트렌드"
    ],
    "study": [
        "java", "python", "aws", "cloud", "자격증", "공부",
        "study", "학습", "강의", "알고리즘", "자료구조"
    ],
    "project": [
        "project", "프로젝트", "앱", "app", "flutter", "배포",
        "출시", "github", "개발기", "만들기"
    ],
    "trouble": [
        "error", "에러", "trouble", "bug", "해결", "fix",
        "오류", "exception", "fail", "실패", "디버깅"
    ],
}

def guess_category(title: str, content: str) -> str:
    """제목과 본문으로 카테고리 추측"""
    combined = (title + " " + content[:500]).lower()
    scores = {cat: 0 for cat in CATEGORY_RULES}
    for cat, keywords in CATEGORY_RULES.items():
        for kw in keywords:
            if kw.lower() in combined:
                scores[cat] += 1
    return max(scores, key=scores.get) if max(scores.values()) > 0 else "tech"

def estimate_read_time(text: str) -> int:
    """한국어/영어 혼합 기준 분당 250단어"""
    words = len(text.split())
    chars = len(re.sub(r'\s', '', text))
    kor_chars = len(re.findall(r'[가-힣]', text))
    minutes = max(1, round((words * 0.5 + kor_chars * 0.02) / 250 * 10))
    return minutes

def clean_medium_html(soup: BeautifulSoup) -> BeautifulSoup:
    """Medium 특유의 불필요한 태그 정리"""
    # 제거할 요소들
    for sel in ['script', 'style', '.metabar', 'nav',
                '.u-hide', '[data-action]', 'button']:
        for tag in soup.select(sel):
            tag.decompose()

    # Medium 하이라이트 → blockquote 변환
    for tag in soup.find_all(class_=re.compile(r'graf--pullquote|graf--blockquote')):
        tag.name = 'blockquote'

    # <pre><code> 코드블록 언어 힌트 보존
    for pre in soup.find_all('pre'):
        code = pre.find('code')
        if code:
            lang = code.get('class', [''])[0].replace('language-', '') if code.get('class') else ''
            if lang:
                pre['data-lang'] = lang

    return soup

def html_to_markdown(html_content: str) -> tuple[str, dict]:
    """HTML → Markdown 변환 + 메타데이터 추출"""
    soup = BeautifulSoup(html_content, 'html.parser')
    soup = clean_medium_html(soup)

    # 메타데이터 추출
    title = ""
    for sel in ['h1', '.graf--title', 'title']:
        el = soup.find(sel)
        if el:
            title = el.get_text().strip()
            break

    # 날짜 추출 (Medium 타임스탬프)
    date_str = datetime.now().strftime('%Y-%m-%d')
    time_el = soup.find('time')
    if time_el and time_el.get('datetime'):
        try:
            dt = datetime.fromisoformat(time_el['datetime'].replace('Z', '+00:00'))
            date_str = dt.strftime('%Y-%m-%d')
        except Exception:
            pass

    # 본문 추출 (Medium article body)
    body = soup.find('article') or soup.find(class_=re.compile(r'postArticle|story'))
    body_html = str(body) if body else str(soup)

    # Markdown 변환
    md_content = markdownify.markdownify(
        body_html,
        heading_style="ATX",       # ## 스타일
        bullets="-",               # - 리스트
        code_language="python",    # 기본 코드 언어
        strip=['figure'],          # figure 태그는 별도 처리
    )

    # 후처리: 중복 개행 정리
    md_content = re.sub(r'\n{3,}', '\n\n', md_content)
    md_content = re.sub(r'^\s+', '', md_content)

    # 이미지 alt 텍스트 정리
    md_content = re.sub(r'!\[\]\(', '![이미지](', md_content)

    # [수정된 부분] allow_unicode 옵션을 삭제하여 패키지 충돌 완벽 방지
    safe_slug = slugify(title) if title else "untitled"

    metadata = {
        "title": title,
        "date": date_str,
        "slug": safe_slug,
        "category": guess_category(title, md_content),
        "readTime": estimate_read_time(md_content),
        "tags": extract_tags(title, md_content),
    }

    return md_content, metadata

def extract_tags(title: str, content: str) -> list[str]:
    """본문에서 태그 자동 추출"""
    tag_map = {
        "intel": "intel-ai", "openvino": "openvino", "ai": "ai",
        "java": "java", "python": "python", "flutter": "flutter",
        "aws": "aws", "cloud": "cloud", "방문": "field-visit",
        "프로젝트": "project", "에러": "trouble-shooting",
        "공부": "study", "학습": "learning",
    }
    combined = (title + " " + content[:1000]).lower()
    found = []
    for kw, tag in tag_map.items():
        if kw in combined and tag not in found:
            found.append(tag)
    return found[:6]  # 최대 6개

def build_frontmatter(meta: dict, extra: dict = None) -> str:
    """Frontmatter YAML 생성"""
    tags_yaml = "\n".join(f"  - {t}" for t in meta.get("tags", []))
    
    # 따옴표 이스케이프 처리: 제목에 큰따옴표가 있으면 Astro 빌드 시 에러 발생 방지
    safe_title = meta['title'].replace('"', '\\"')

    fm = f"""---
title: "{safe_title}"
slug: "{meta['slug']}"
date: {meta['date']}
author: "Evan Yoon"
category: "{meta['category']}"
description: ""
thumbnail: "/images/posts/{meta['slug']}/cover.jpg"
tags:
{tags_yaml or '  - uncategorized'}
readTime: {meta['readTime']}
series: ""
seriesOrder: 1
featured: false
draft: false
toc: true
---

"""
    return fm

def convert_file(html_path: Path, output_dir: Path) -> Path:
    """단일 HTML 파일 변환"""
    print(f"  변환 중: {html_path.name}")

    with open(html_path, 'r', encoding='utf-8') as f:
        html_content = f.read()

    md_content, meta = html_to_markdown(html_content)
    frontmatter = build_frontmatter(meta)

    output_filename = f"{meta['date']}-{meta['slug']}.md"
    output_path = output_dir / output_filename

    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(frontmatter + md_content)

    print(f"  ✓ 저장 완료: {output_filename} (카테고리: {meta['category']}, {meta['readTime']}분 읽기)")
    return output_path

def main():
    input_dir = Path("./medium_export")
    output_dir = Path("./output")
    output_dir.mkdir(exist_ok=True)

    # HTML 파일 찾기
    html_files = list(input_dir.glob("*.html")) if input_dir.exists() else []

    if not html_files:
        print("""
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  사용 방법:
  1. Medium.com → Settings → Security and apps → Export your data
  2. 받은 ZIP 파일 압축 해제
  3. 내부 HTML 파일들을 ./medium_export/ 폴더에 복사
  4. 다시 실행: python medium_to_blog.py
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  ※ 테스트용 샘플 파일을 생성합니다...
""")
        # 샘플 생성
        input_dir.mkdir(exist_ok=True)
        sample = input_dir / "sample-intel-visit.html"
        sample.write_text("""
<html><body>
<article>
<h1>A Visit to Intel Korea: Meeting the Experts</h1>
<time datetime="2025-06-15T09:00:00Z">June 15, 2025</time>
<p>This is a sample article about visiting Intel Korea as part of the Intel AI for Future Workforce program.</p>
<blockquote>Real growth starts when you admit what you don't know.</blockquote>
<h2>What I Learned</h2>
<p>The visit was incredibly insightful...</p>
<pre><code class="language-python">from openvino.runtime import Core
ie = Core()
print("OpenVINO loaded!")</code></pre>
</article>
</body></html>
""", encoding='utf-8')
        html_files = [sample]

    print(f"\n📂 {len(html_files)}개 파일 발견\n")
    converted = []
    for f in sorted(html_files):
        try:
            out = convert_file(f, output_dir)
            converted.append(out)
        except Exception as e:
            print(f"  ✗ 오류 발생 ({f.name}): {e}")

    print(f"\n✅ 완료! {len(converted)}개 파일이 ./output/ 폴더에 저장되었습니다.")
    print(f"   → 이제 output 폴더 안의 파일들을 src/content/posts/ 로 복사하세요.\n")

if __name__ == "__main__":
    main()