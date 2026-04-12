import "dotenv/config";
import fs from "fs";
import path from "path";
import { XMLParser } from "fast-xml-parser";
import { callClaude } from "./lib/claude.js";

const OUTPUT_DIR = path.join(process.cwd(), "src", "content", "posts", "ainews");

const parser = new XMLParser({
  ignoreAttributes: false,
  attributeNamePrefix: "",
  trimValues: true,
});

const SOURCE_CONFIGS = [
  {
    id: "en",
    sourceName: "AI News",
    siteUrl: "https://www.artificialintelligence-news.com",
    feedUrls: [
      "https://www.artificialintelligence-news.com/feed/",
      "https://www.artificialintelligence-news.com/news/feed/",
    ],
    language: "English",
    locale: "en-US",
    promptLanguage:
      "Write the article in natural English. Keep it compact, readable, and editorial rather than promotional.",
  },
  {
    id: "ko",
    sourceName: "AI타임스",
    siteUrl: "https://www.aitimes.com",
    feedUrls: ["https://cdn.aitimes.com/rss/gn_rss_allArticle.xml"],
    language: "Korean",
    locale: "ko-KR",
    promptLanguage:
      "본문은 자연스러운 한국어로 작성한다. 문장은 짧게 유지하고, 한 주 동안의 흐름을 빠르게 훑는 요약문처럼 쓴다.",
  },
];

const POSITIVE_KEYWORDS = [
  "agent",
  "agentic",
  "ai",
  "benchmark",
  "chip",
  "cloud",
  "coding",
  "computer vision",
  "data center",
  "developer",
  "foundation model",
  "gpu",
  "hardware",
  "inference",
  "infrastructure",
  "llm",
  "machine learning",
  "model",
  "multimodal",
  "open source",
  "reasoning",
  "research",
  "robot",
  "semiconductor",
  "training",
  "vision",
  "데이터센터",
  "모델",
  "반도체",
  "에이전트",
  "오픈소스",
  "인프라",
  "칩",
  "추론",
  "학습",
];

const NEGATIVE_KEYWORDS = [
  "conference",
  "earnings",
  "event",
  "funding",
  "hiring",
  "interview",
  "lawsuit",
  "market",
  "newsletter",
  "opinion",
  "politics",
  "stock",
  "subscribe",
  "이벤트",
  "인터뷰",
  "정치",
  "주가",
  "투자 유치",
];

function getArgValue(flagName) {
  const args = process.argv.slice(2);
  const index = args.indexOf(flagName);
  if (index === -1) return null;
  return args[index + 1] ?? null;
}

function parseAnchorDate() {
  const raw = getArgValue("--date") || process.env.AI_NEWS_NOW || null;
  if (!raw) return new Date();

  const parsed = new Date(raw);
  if (Number.isNaN(parsed.valueOf())) {
    throw new Error(`Invalid date value: ${raw}`);
  }
  return parsed;
}

function getWeeklyWindow(now = new Date()) {
  const current = new Date(now);
  const day = current.getDay();
  const daysSinceMonday = (day + 6) % 7;

  const thisMonday = new Date(current);
  thisMonday.setHours(0, 0, 0, 0);
  thisMonday.setDate(thisMonday.getDate() - daysSinceMonday);

  const start = new Date(thisMonday);
  start.setDate(start.getDate() - 7);
  start.setHours(0, 0, 0, 0);

  const end = new Date(thisMonday);
  end.setMilliseconds(-1);

  return { start, end, digestDate: toIsoDate(thisMonday) };
}

function toIsoDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function formatDisplayDate(date, locale) {
  return new Intl.DateTimeFormat(locale, {
    year: "numeric",
    month: "long",
    day: "numeric",
  }).format(date);
}

function normalizeWhitespace(value) {
  return String(value || "")
    .replace(/<script[\s\S]*?<\/script>/gi, " ")
    .replace(/<style[\s\S]*?<\/style>/gi, " ")
    .replace(/<[^>]+>/g, " ")
    .replace(/&nbsp;/gi, " ")
    .replace(/&amp;/gi, "&")
    .replace(/&#39;/g, "'")
    .replace(/&quot;/g, '"')
    .replace(/\s+/g, " ")
    .trim();
}

function toArray(value) {
  if (!value) return [];
  return Array.isArray(value) ? value : [value];
}

function absolutizeUrl(value, siteUrl) {
  if (!value) return "";
  try {
    return new URL(value, siteUrl).toString();
  } catch {
    return "";
  }
}

function getFirstImageUrl(html, siteUrl) {
  const match = String(html || "").match(/<img[^>]+src=["']([^"']+)["']/i);
  return match ? absolutizeUrl(match[1], siteUrl) : "";
}

function getMediaImageUrl(media, siteUrl) {
  if (!media) return "";
  if (Array.isArray(media)) {
    return media.map((item) => getMediaImageUrl(item, siteUrl)).find(Boolean) || "";
  }
  if (typeof media === "object") {
    return absolutizeUrl(media.url || media.href || "", siteUrl);
  }
  return "";
}

function scoreArticle(article) {
  const haystack = [
    article.title,
    article.description,
    article.category?.join(" "),
  ]
    .filter(Boolean)
    .join(" ")
    .toLowerCase();

  let score = 0;
  for (const keyword of POSITIVE_KEYWORDS) {
    if (haystack.includes(keyword)) score += 3;
  }
  for (const keyword of NEGATIVE_KEYWORDS) {
    if (haystack.includes(keyword)) score -= 2;
  }
  return score;
}

function isInsideWindow(date, window) {
  return date >= window.start && date <= window.end;
}

function uniqueByLink(items) {
  const seen = new Set();
  return items.filter((item) => {
    if (!item.link || seen.has(item.link)) return false;
    seen.add(item.link);
    return true;
  });
}

function collectFeedItems(xmlText, source) {
  const parsed = parser.parse(xmlText);
  if (!parsed?.rss?.channel?.item) return [];

  return toArray(parsed.rss.channel.item).map((item) => {
    const encoded = item["content:encoded"] || "";
    const imageUrl =
      getMediaImageUrl(item["media:content"], source.siteUrl) ||
      getFirstImageUrl(encoded || item.description, source.siteUrl);

    return {
      title: normalizeWhitespace(item.title),
      link: absolutizeUrl(item.link, source.siteUrl),
      description: normalizeWhitespace(item.description || encoded),
      publishedAt:
        item.pubDate ||
        item.published ||
        item.updated ||
        item["dc:date"] ||
        "",
      category: toArray(item.category).map((entry) =>
        normalizeWhitespace(typeof entry === "string" ? entry : entry["#text"]),
      ),
      imageUrl,
    };
  });
}

async function fetchText(url) {
  const response = await fetch(url, {
    headers: {
      "user-agent": "Mozilla/5.0 (compatible; EvanYoonWeeklyAINews/1.0)",
      accept: "text/html,application/xml,text/xml;q=0.9,*/*;q=0.8",
    },
  });

  if (!response.ok) {
    throw new Error(`Fetch failed for ${url}: ${response.status}`);
  }

  return response.text();
}

async function loadArticlesFromSource(source) {
  for (const feedUrl of source.feedUrls) {
    try {
      const xmlText = await fetchText(feedUrl);
      const items = collectFeedItems(xmlText, source).filter(
        (item) => item.title && item.link && item.publishedAt,
      );

      if (items.length > 0) {
        return { articles: items, mode: `feed:${feedUrl}` };
      }
    } catch (error) {
      console.warn(`[${source.id}] feed fetch failed: ${feedUrl}`, error.message);
    }
  }

  throw new Error(`[${source.id}] no articles found from configured feeds`);
}

function pickWeeklyArticles(rawArticles, window) {
  return uniqueByLink(
    rawArticles
      .map((article) => ({
        ...article,
        publishedDate: new Date(article.publishedAt),
      }))
      .filter((article) => !Number.isNaN(article.publishedDate.valueOf()))
      .filter((article) => isInsideWindow(article.publishedDate, window))
      .map((article) => ({
        ...article,
        score: scoreArticle(article),
      }))
      .filter((article) => article.score > 0)
      .sort((left, right) => {
        if (right.score !== left.score) return right.score - left.score;
        return right.publishedDate.valueOf() - left.publishedDate.valueOf();
      }),
  ).slice(0, 5);
}

function buildPrompt({ source, digestDate, window, articles }) {
  const slug = `${digestDate}-weekly-ai-news-${source.id}`;
  const thumbnail = articles.find((article) => article.imageUrl)?.imageUrl || "";
  const weeklyLabel = `${formatDisplayDate(window.start, source.locale)} - ${formatDisplayDate(window.end, source.locale)}`;

  const articlePayload = articles
    .map((article, index) =>
      [
        `Article ${index + 1}`,
        `Title: ${article.title}`,
        `Published: ${formatDisplayDate(article.publishedDate, source.locale)}`,
        `URL: ${article.link}`,
        `Category: ${article.category.join(", ") || "N/A"}`,
        `Image: ${article.imageUrl || "N/A"}`,
        `Summary: ${article.description || "N/A"}`,
      ].join("\n"),
    )
    .join("\n\n");

  return `
You are creating one weekly AI news summary for Evan Yoon's blog.

Source site: ${source.sourceName}
Language: ${source.language}
Publication date: ${digestDate}
Weekly window: ${weeklyLabel}
Target slug: ${slug}

${source.promptLanguage}

Use only the supplied article metadata. Do not invent facts. Do not copy source wording closely. Rewrite in Evan Yoon's own short summary style.

Output requirements:
1. Output only valid markdown.
2. Start with frontmatter in this exact order:
---
title: "..."
slug: "${slug}"
date: "${digestDate}"
author: "Evan Yoon"
category: "ai-news"
description: "..."
thumbnail: "${thumbnail}"
tags: ["...", "...", "..."]
draft: false
toc: false
---
3. After the frontmatter, write:
   - one short opening paragraph
   - ## This Week
   - 3 to 5 issue blocks
4. Each issue block must use this exact mini-structure:
   - ### short issue title
   - a markdown image line using the supplied article image URL if available
   - 1 or 2 short sentences of summary
   - one source line in markdown: Source: [title](url)
5. If an article has no image URL, skip the image line for that issue only.
6. Keep the whole body around 220 to 360 words in English, or 260 to 420 words in Korean.
7. Make it clear that this is a weekly summary for the supplied Monday-to-Sunday range.
8. Focus on AI technology, models, chips, infrastructure, tooling, and deployment.
9. Avoid filler, hype, and press-release tone.

Articles:
${articlePayload}
`.trim();
}

function getOutputPath(digestDate, source) {
  return path.join(OUTPUT_DIR, `${digestDate}-weekly-ai-news-${source.id}.md`);
}

async function createWeeklyPost({ source, digestDate, window }) {
  const outputPath = getOutputPath(digestDate, source);

  if (fs.existsSync(outputPath)) {
    console.log(`[${source.id}] skipped: file already exists`);
    return null;
  }

  const { articles, mode } = await loadArticlesFromSource(source);
  const selected = pickWeeklyArticles(articles, window);

  if (selected.length === 0) {
    console.log(`[${source.id}] skipped: no qualifying weekly articles inside ${mode}`);
    return null;
  }

  const markdown = await callClaude({
    prompt: buildPrompt({ source, digestDate, window, articles: selected }),
    maxTokens: 2200,
    temperature: 0.45,
  });

  fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  fs.writeFileSync(outputPath, `${markdown.trim()}\n`, "utf-8");
  console.log(`[${source.id}] created ${outputPath}`);
  return outputPath;
}

async function main() {
  if (!process.env.ANTHROPIC_API_KEY) {
    throw new Error("ANTHROPIC_API_KEY is required.");
  }

  const anchorDate = parseAnchorDate();
  const { start, end, digestDate } = getWeeklyWindow(anchorDate);

  console.log(`Digest date: ${digestDate}`);
  console.log(`Weekly window: ${start.toISOString()} -> ${end.toISOString()}`);

  const createdFiles = [];
  for (const source of SOURCE_CONFIGS) {
    const filePath = await createWeeklyPost({
      source,
      digestDate,
      window: { start, end },
    });
    if (filePath) createdFiles.push(filePath);
  }

  if (createdFiles.length === 0) {
    console.log("No weekly AI news posts were created.");
  }
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
