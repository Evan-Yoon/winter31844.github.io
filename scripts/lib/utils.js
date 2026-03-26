import fs from "fs";
import path from "path";

export const ROOT = process.cwd();
export const POSTS_DIR = path.join(ROOT, "src", "content", "posts");
export const STYLE_PATH = path.join(ROOT, "style.md");

export function ensureExists(targetPath, message) {
  if (!fs.existsSync(targetPath)) {
    throw new Error(message);
  }
}

export function readTextFile(filePath) {
  return fs.readFileSync(filePath, "utf-8");
}

export function writeTextFile(filePath, content) {
  fs.writeFileSync(filePath, content, "utf-8");
}

export function getTodayDateString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

export function sanitizeFileName(name) {
  return name
    .toLowerCase()
    .replace(/['"]/g, "")
    .replace(/[^a-z0-9가-힣\s-]/g, "")
    .trim()
    .replace(/\s+/g, "-")
    .replace(/-+/g, "-");
}

export function extractFrontmatterTitle(markdown) {
  const match = markdown.match(/^---[\s\S]*?title:\s*"(.+?)"[\s\S]*?---/m);
  return match ? match[1] : null;
}

export function ensureFrontmatter(markdown) {
  if (!markdown.trim().startsWith("---")) {
    throw new Error("Generated content does not start with valid frontmatter.");
  }

  if (!/^---[\s\S]*?---/.test(markdown)) {
    throw new Error(
      "Generated content does not contain a valid closing frontmatter block.",
    );
  }
}

export function getArgValue(flagName) {
  const args = process.argv.slice(2);
  const index = args.indexOf(flagName);

  if (index === -1) return null;
  return args[index + 1] ?? null;
}

export function getRequiredArg(flagName, errorMessage) {
  const value = getArgValue(flagName);
  if (!value) {
    throw new Error(errorMessage);
  }
  return value;
}

export function getOptionalIntArg(flagName, fallback) {
  const value = getArgValue(flagName);
  if (!value) return fallback;

  const parsed = Number(value);
  if (Number.isNaN(parsed)) return fallback;
  return parsed;
}

export function normalizeWhitespace(text) {
  return text
    .replace(/\r\n/g, "\n")
    .replace(/\n{3,}/g, "\n\n")
    .replace(/[ \t]{2,}/g, " ")
    .trim();
}

export function truncateText(text, maxLength = 12000) {
  if (!text) return "";
  if (text.length <= maxLength) return text;
  return `${text.slice(0, maxLength)}\n\n[TRUNCATED]`;
}
