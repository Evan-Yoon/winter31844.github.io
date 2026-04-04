// src/plugins/remark-auto-images.mjs
//
// 동작 방식:
//  1. 포스트 frontmatter의 slug를 읽어 public/images/posts/[slug]/ 폴더를 탐색
//  2. cover.jpg 및 본문에 이미 수동으로 삽입된 이미지는 제외
//  3. 남은 이미지를 h2 섹션 끝(다음 h2 직전)마다 순서대로 자동 삽입
//
// 이미지 파일명 규칙: 01.jpg, 02.png, 03.webp ... (알파벳순 정렬)
// 제외 규칙:  cover.jpg (썸네일), 본문에 이미 쓰인 파일명

import fs from 'node:fs';
import path from 'node:path';

const IMAGE_EXTS = /\.(jpg|jpeg|png|webp|gif|avif)$/i;
const EXCLUDE = new Set(['cover.jpg', 'cover.png', 'cover.webp']);
const BASE = '';

/** 트리를 재귀적으로 순회해 이미 사용된 이미지 파일명을 수집 */
function collectReferenced(nodes, result = new Set()) {
  for (const node of nodes) {
    if (node.type === 'image') result.add(path.basename(node.url));
    if (node.children) collectReferenced(node.children, result);
  }
  return result;
}

export function remarkAutoImages() {
  return (tree, vfile) => {
    // frontmatter slug 가져오기 (Astro가 주입)
    const slug = vfile.data?.astro?.frontmatter?.slug;
    if (!slug) return;

    const imagesDir = path.join(process.cwd(), 'public', 'images', 'posts', slug);
    if (!fs.existsSync(imagesDir)) return;

    // 삽입 후보 이미지: cover 제외, 이미 본문에 쓰인 것 제외, 알파벳순 정렬
    const referenced = collectReferenced(tree.children);
    const candidates = fs
      .readdirSync(imagesDir)
      .filter(f => IMAGE_EXTS.test(f) && !EXCLUDE.has(f) && !referenced.has(f))
      .sort();

    if (candidates.length === 0) return;

    // h2 헤딩 위치 수집
    const h2Indices = tree.children.reduce((acc, node, idx) => {
      if (node.type === 'heading' && node.depth === 2) acc.push(idx);
      return acc;
    }, []);

    // h2가 없으면 자동 삽입 건너뜀 (글 구조 파악 불가)
    if (h2Indices.length === 0) return;

    // 각 h2 섹션 끝(다음 h2 직전 또는 문서 끝)에 이미지 하나씩 삽입 계획
    const insertions = h2Indices
      .map((h2Idx, i) => ({
        at: h2Indices[i + 1] ?? tree.children.length,
        file: candidates[i],
      }))
      .filter(({ file }) => file !== undefined); // 이미지가 섹션 수보다 적으면 남는 섹션은 건너뜀

    // 인덱스가 뒤에서 앞으로 처리돼야 splice 위치가 밀리지 않음
    insertions.reverse().forEach(({ at, file }) => {
      const src = `${BASE}/images/posts/${slug}/${file}`;
      const alt = file.replace(IMAGE_EXTS, '').replace(/[-_]/g, ' ');

      tree.children.splice(at, 0, {
        type: 'paragraph',
        data: { hProperties: { className: 'auto-image-wrap' } },
        children: [{ type: 'image', url: src, alt, title: null }],
      });
    });
  };
}
