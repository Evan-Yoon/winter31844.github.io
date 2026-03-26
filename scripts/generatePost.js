import fs from "fs";
import path from "path";

const API_KEY = process.env.ANTHROPIC_API_KEY;

if (!API_KEY) {
  throw new Error("ANTHROPIC_API_KEY is not set.");
}

const ROOT = process.cwd();
const STYLE_PATH = path.join(ROOT, "style.md");
const INPUT_PATH = path.join(ROOT, "input.txt");
const POSTS_DIR = path.join(ROOT, "src", "content", "posts");

if (!fs.existsSync(STYLE_PATH)) {
  throw new Error("style.md file not found in project root.");
}

if (!fs.existsSync(INPUT_PATH)) {
  throw new Error("input.txt file not found in project root.");
}

if (!fs.existsSync(POSTS_DIR)) {
  throw new Error("src/content/posts directory not found.");
}

const styleGuide = fs.readFileSync(STYLE_PATH, "utf-8").trim();
const inputContent = fs.readFileSync(INPUT_PATH, "utf-8").trim();

if (!inputContent) {
  throw new Error("input.txt is empty.");
}

function getTodayDateString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function sanitizeFileName(name) {
  return name
    .toLowerCase()
    .replace(/['"]/g, "")
    .replace(/[^a-z0-9가-힣\s-]/g, "")
    .trim()
    .replace(/\s+/g, "-")
    .replace(/-+/g, "-");
}

function extractFrontmatterTitle(markdown) {
  const match = markdown.match(/^---[\s\S]*?title:\s*"(.+?)"[\s\S]*?---/m);
  return match ? match[1] : null;
}

function ensureFrontmatter(markdown) {
  if (!markdown.trim().startsWith("---")) {
    throw new Error(
      "Generated content does not contain valid frontmatter starting with ---",
    );
  }
  if (!/^---[\s\S]*?---/.test(markdown)) {
    throw new Error(
      "Generated content does not contain a valid closing frontmatter block.",
    );
  }
}

async function generatePostWithClaude() {
  const today = getTodayDateString();

  const prompt = `
You are a blog writing assistant for Evan Yoon.

Follow the style guide below exactly.

<STYLE_GUIDE>
${styleGuide}
</STYLE_GUIDE>

Now write ONE complete blog post based on the source material below.

<SOURCE_MATERIAL>
${inputContent}
</SOURCE_MATERIAL>

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
4. Choose the language naturally based on the source material.
5. Make the writing feel human and consistent with the style guide.
6. Do not include explanations outside the markdown.
7. Do not wrap the answer in code fences.
`;

  const response = await fetch("https://api.anthropic.com/v1/messages", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "x-api-key": API_KEY,
      "anthropic-version": "2023-06-01",
    },
    body: JSON.stringify({
      model: "claude-3-5-sonnet-20241022",
      max_tokens: 4000,
      temperature: 0.8,
      messages: [
        {
          role: "user",
          content: prompt,
        },
      ],
    }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `Claude API request failed: ${response.status} ${errorText}`,
    );
  }

  const data = await response.json();

  if (!data.content || !Array.isArray(data.content) || !data.content[0]?.text) {
    throw new Error("Claude response format is invalid.");
  }

  let markdown = data.content[0].text.trim();

  markdown = markdown
    .replace(/^```markdown\s*/i, "")
    .replace(/^```\s*/i, "")
    .replace(/\s*```$/, "");

  ensureFrontmatter(markdown);

  const title = extractFrontmatterTitle(markdown) || "auto-generated-post";
  const fileSafeTitle = sanitizeFileName(title);
  const fileName = `${today}-${fileSafeTitle}.md`;
  const filePath = path.join(POSTS_DIR, fileName);

  fs.writeFileSync(filePath, markdown, "utf-8");

  console.log(`Post generated successfully: ${filePath}`);
}

generatePostWithClaude().catch((error) => {
  console.error(error);
  process.exit(1);
});
