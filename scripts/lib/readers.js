import fs from "fs";
import path from "path";
import mammoth from "mammoth";
import JSZip from "jszip";
import { XMLParser } from "fast-xml-parser";
import pdfParse from "pdf-parse";
import { normalizeWhitespace, truncateText } from "./utils.js";

export async function readDocumentFile(filePath) {
  const ext = path.extname(filePath).toLowerCase();

  if (!fs.existsSync(filePath)) {
    throw new Error(`File not found: ${filePath}`);
  }

  switch (ext) {
    case ".txt":
    case ".md":
      return normalizeWhitespace(fs.readFileSync(filePath, "utf-8"));

    case ".docx":
      return await readDocx(filePath);

    case ".pptx":
      return await readPptx(filePath);

    case ".pdf":
      return await readPdf(filePath);

    default:
      throw new Error(`Unsupported file type: ${ext}`);
  }
}

async function readDocx(filePath) {
  const result = await mammoth.extractRawText({ path: filePath });
  return normalizeWhitespace(result.value);
}

async function readPdf(filePath) {
  const buffer = fs.readFileSync(filePath);
  const result = await pdfParse(buffer);
  return normalizeWhitespace(result.text);
}

async function readPptx(filePath) {
  const buffer = fs.readFileSync(filePath);
  const zip = await JSZip.loadAsync(buffer);
  const parser = new XMLParser({
    ignoreAttributes: false,
    attributeNamePrefix: "@_",
  });

  const slidePaths = Object.keys(zip.files)
    .filter((name) => /^ppt\/slides\/slide\d+\.xml$/.test(name))
    .sort((a, b) => {
      const aNum = Number(a.match(/slide(\d+)\.xml$/)?.[1] || 0);
      const bNum = Number(b.match(/slide(\d+)\.xml$/)?.[1] || 0);
      return aNum - bNum;
    });

  const slides = [];

  for (const slidePath of slidePaths) {
    const xmlText = await zip.files[slidePath].async("text");
    const parsed = parser.parse(xmlText);
    const texts = [];

    collectPptText(parsed, texts);

    const slideNumber = slidePath.match(/slide(\d+)\.xml$/)?.[1] || "?";
    const cleaned = normalizeWhitespace(texts.join(" "));

    if (cleaned) {
      slides.push(`## Slide ${slideNumber}\n${cleaned}`);
    }
  }

  return truncateText(slides.join("\n\n"), 15000);
}

function collectPptText(node, output) {
  if (!node) return;

  if (typeof node === "string") return;

  if (Array.isArray(node)) {
    for (const item of node) {
      collectPptText(item, output);
    }
    return;
  }

  for (const [key, value] of Object.entries(node)) {
    if (key === "a:t") {
      if (typeof value === "string") {
        output.push(value);
      } else if (Array.isArray(value)) {
        for (const v of value) {
          if (typeof v === "string") output.push(v);
        }
      }
      continue;
    }

    collectPptText(value, output);
  }
}

export async function readNotionPage({ token, pageId }) {
  const page = await fetchJson(
    `https://api.notion.com/v1/pages/${pageId}`,
    token,
  );
  const title = extractNotionTitle(page);

  const blocks = await fetchAllBlocks(pageId, token);
  const lines = [];

  flattenNotionBlocks(blocks, lines, 0);

  return {
    title: title || "Untitled Notion Page",
    content: truncateText(normalizeWhitespace(lines.join("\n")), 16000),
  };
}

async function fetchAllBlocks(blockId, token) {
  let url = `https://api.notion.com/v1/blocks/${blockId}/children?page_size=100`;
  const allResults = [];

  while (url) {
    const data = await fetchJson(url, token);
    allResults.push(...(data.results || []));

    if (data.has_more && data.next_cursor) {
      url = `https://api.notion.com/v1/blocks/${blockId}/children?page_size=100&start_cursor=${data.next_cursor}`;
    } else {
      url = null;
    }
  }

  const expanded = [];

  for (const block of allResults) {
    let children = [];
    if (block.has_children) {
      children = await fetchAllBlocks(block.id, token);
    }
    expanded.push({ ...block, children });
  }

  return expanded;
}

function extractNotionTitle(page) {
  const prop = page?.properties;
  if (!prop) return "";

  for (const value of Object.values(prop)) {
    if (value?.type === "title" && Array.isArray(value.title)) {
      return richTextToPlain(value.title);
    }
  }

  return "";
}

function flattenNotionBlocks(blocks, lines, depth) {
  for (const block of blocks) {
    const text = extractBlockText(block);

    switch (block.type) {
      case "heading_1":
        if (text) lines.push(`# ${text}`);
        break;
      case "heading_2":
        if (text) lines.push(`## ${text}`);
        break;
      case "heading_3":
        if (text) lines.push(`### ${text}`);
        break;
      case "bulleted_list_item":
        if (text) lines.push(`${"  ".repeat(depth)}- ${text}`);
        break;
      case "numbered_list_item":
        if (text) lines.push(`${"  ".repeat(depth)}1. ${text}`);
        break;
      case "to_do":
        if (text) lines.push(`${"  ".repeat(depth)}- [ ] ${text}`);
        break;
      case "quote":
        if (text) lines.push(`> ${text}`);
        break;
      case "code":
        if (text) lines.push(`\`\`\`\n${text}\n\`\`\``);
        break;
      case "paragraph":
      default:
        if (text) lines.push(text);
        break;
    }

    if (block.children?.length) {
      flattenNotionBlocks(block.children, lines, depth + 1);
    }
  }
}

function extractBlockText(block) {
  const value = block?.[block.type];
  if (!value) return "";

  if (Array.isArray(value.rich_text)) {
    return richTextToPlain(value.rich_text);
  }

  if (Array.isArray(value.caption)) {
    return richTextToPlain(value.caption);
  }

  return "";
}

function richTextToPlain(items) {
  return (items || [])
    .map((item) => item?.plain_text || "")
    .join("")
    .trim();
}

async function fetchJson(url, token) {
  const response = await fetch(url, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Notion-Version": "2022-06-28",
    },
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Notion API request failed: ${response.status} ${text}`);
  }

  return await response.json();
}
