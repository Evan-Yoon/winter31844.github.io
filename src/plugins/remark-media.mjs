// src/plugins/remark-media.mjs
//
// Behavior:
// 1. Convert standalone YouTube links into responsive embeds.
// 2. Read each post slug from frontmatter and scan public/images/posts/[slug]/.
// 3. Skip cover images and any images already referenced in the markdown body.
// 4. Auto-insert the remaining images before each next h2 section.

import fs from 'node:fs';
import path from 'node:path';

const IMAGE_EXTS = /\.(jpg|jpeg|png|webp|gif|avif)$/i;
const EXCLUDE = new Set(['cover.jpg', 'cover.png', 'cover.webp']);
const BASE = '';

function collectReferenced(nodes, result = new Set()) {
  for (const node of nodes) {
    if (node.type === 'image') result.add(path.basename(node.url));
    if (node.children) collectReferenced(node.children, result);
  }
  return result;
}

function getSingleYouTubeLink(node) {
  if (node?.type !== 'paragraph' || node.children?.length !== 1) return null;

  const [child] = node.children;
  if (child?.type !== 'link' || typeof child.url !== 'string') return null;

  try {
    const url = new URL(child.url);
    const host = url.hostname.replace(/^www\./, '');

    if (host === 'youtu.be') {
      return url.pathname.slice(1) || null;
    }

    if (host === 'youtube.com' || host === 'm.youtube.com') {
      if (url.pathname === '/watch') {
        return url.searchParams.get('v');
      }

      if (url.pathname.startsWith('/embed/')) {
        return url.pathname.split('/')[2] || null;
      }
    }
  } catch {
    return null;
  }

  return null;
}

export function remarkMedia() {
  return (tree, vfile) => {
    tree.children = tree.children.map((node) => {
      const videoId = getSingleYouTubeLink(node);
      if (!videoId) return node;

      return {
        type: 'html',
        value: [
          '<div class="video-embed" style="position:relative; width:100%; max-width:760px; margin:1rem auto; aspect-ratio:16 / 9;">',
          `<iframe src="https://www.youtube.com/embed/${videoId}" title="YouTube video player" loading="lazy" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen style="position:absolute; inset:0; width:100%; height:100%; border:0; border-radius:16px;"></iframe>`,
          '</div>',
        ].join(''),
      };
    });

    const slug = vfile.data?.astro?.frontmatter?.slug;
    if (!slug) return;

    const imagesDir = path.join(process.cwd(), 'public', 'images', 'posts', slug);
    if (!fs.existsSync(imagesDir)) return;

    const referenced = collectReferenced(tree.children);
    const candidates = fs
      .readdirSync(imagesDir)
      .filter((file) => IMAGE_EXTS.test(file) && !EXCLUDE.has(file) && !referenced.has(file))
      .sort();

    if (candidates.length === 0) return;

    const h2Indices = tree.children.reduce((acc, node, idx) => {
      if (node.type === 'heading' && node.depth === 2) acc.push(idx);
      return acc;
    }, []);

    if (h2Indices.length === 0) return;

    const insertions = h2Indices
      .map((h2Idx, index) => ({
        at: h2Indices[index + 1] ?? tree.children.length,
        file: candidates[index],
      }))
      .filter(({ file }) => file !== undefined);

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
