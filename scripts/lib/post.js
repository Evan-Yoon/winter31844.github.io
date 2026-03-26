import fs from "fs";
import path from "path";
import { callClaude } from "./claude.js";
import {
  POSTS_DIR,
  STYLE_PATH,
  ensureExists,
  readTextFile,
  getTodayDateString,
  extractFrontmatterTitle,
  sanitizeFileName,
  ensureFrontmatter,
} from "./utils.js";

ensureExists(STYLE_PATH, "style.md file not found in project root.");
ensureExists(POSTS_DIR, "src/content/posts directory not found.");

export async function generateAndSavePost({
  sourceType,
  sourceTitle,
  sourcePayload,
  extraInstructions = "",
}) {
  const styleGuide = readTextFile(STYLE_PATH).trim();
  const today = getTodayDateString();

  const prompt = `
You are a blog writing assistant for Evan Yoon.

Follow the style guide exactly.

<STYLE_GUIDE>
${styleGuide}
</STYLE_GUIDE>

You are generating a blog post from the following source type:
${sourceType}

Source title:
${sourceTitle}

Source material:
<SOURCE_MATERIAL>
${sourcePayload}
</SOURCE_MATERIAL>

Additional instructions:
${extraInstructions || "None"}

Strict requirements:
1. Output ONLY valid markdown.
2. The markdown MUST begin with a valid frontmatter block using this exact field order:
---
title: "..."
slug: "..."
date: ${today}
author: "Evan Yoon"
category: "..."
description: "..."
thumbnail: ""
tags:
  - ...
readTime: ...
series: ""
seriesOrder: 1
featured: false
draft: false
toc: true
---
3. After the frontmatter, write the full blog post body.
4. Choose Korean or English naturally based on the source material.
5. The final article must feel human and aligned with Evan Yoon's tone.
6. Do not include explanations outside the markdown.
7. Do not wrap the response in code fences.
`;

  const markdown = await callClaude({
    prompt,
    maxTokens: 4000,
    temperature: 0.8,
  });

  ensureFrontmatter(markdown);

  const title = extractFrontmatterTitle(markdown) || "auto-generated-post";
  const safeTitle = sanitizeFileName(title);
  const fileName = `${today}-${safeTitle}.md`;
  const filePath = path.join(POSTS_DIR, fileName);

  fs.writeFileSync(filePath, markdown, "utf-8");

  console.log(`Post generated successfully: ${filePath}`);
  return filePath;
}
