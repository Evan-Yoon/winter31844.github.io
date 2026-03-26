import "dotenv/config";
import path from "path";
import { execSync } from "child_process";
import { generateAndSavePost } from "./lib/post.js";
import {
  getRequiredArg,
  getOptionalIntArg,
  normalizeWhitespace,
  truncateText,
} from "./lib/utils.js";

const repoPath = getRequiredArg(
  "--repo",
  'Usage: npm run blog:git -- --repo "C:\\path\\to\\repo" [--commits 10]',
);

const commitCount = getOptionalIntArg("--commits", 8);

function runGit(command) {
  return execSync(command, {
    cwd: repoPath,
    encoding: "utf-8",
    stdio: ["pipe", "pipe", "pipe"],
  }).trim();
}

function safeGit(command) {
  try {
    return runGit(command);
  } catch {
    return "";
  }
}

async function main() {
  const repoName = path.basename(repoPath);
  const branch = safeGit("git rev-parse --abbrev-ref HEAD");
  const recentCommits = safeGit(
    `git log -n ${commitCount} --date=short --pretty=format:"%h | %ad | %an | %s"`,
  );

  const changedFiles = safeGit(
    `git log -n ${Math.max(1, Math.min(commitCount, 5))} --name-only --pretty=format:`,
  );

  const statSummary = safeGit(
    `git log -n ${Math.max(1, Math.min(commitCount, 5))} --stat --pretty=format:"COMMIT %h %s"`,
  );

  const diffSummary = safeGit(
    `git diff HEAD~1 HEAD -- . ":(exclude)package-lock.json" ":(exclude)yarn.lock"`,
  );

  const readme =
    safeGit("git show HEAD:README.md") || safeGit("git show HEAD:readme.md");

  const payload = normalizeWhitespace(`
Repository name:
${repoName}

Repository path:
${repoPath}

Current branch:
${branch}

Recent commits:
${recentCommits || "No commit logs found."}

Recently changed files:
${changedFiles || "No changed files found."}

Git stat summary:
${statSummary || "No stat summary found."}

Latest diff summary:
${truncateText(diffSummary || "No diff available.", 10000)}

README:
${truncateText(readme || "No README found.", 5000)}
`);

  await generateAndSavePost({
    sourceType: "git_repository",
    sourceTitle: repoName,
    sourcePayload: payload,
    extraInstructions: `
Write this as a development blog or project log.
Focus on:
- what changed
- why it mattered
- what problems were addressed
- technical choices
- reflection and next steps

Do not merely summarize commits line by line.
Turn them into a readable human blog post.
`,
  });
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
