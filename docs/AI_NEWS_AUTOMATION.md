# Weekly AI News Automation

This project generates one short Korean `ai-news` post every week from `https://www.aitimes.com/`.

## How it works

`npm run ai-news:weekly` does the following:

1. Collects articles from AI타임스 RSS.
2. Filters items to the previous full week:
   - Monday `00:00`
   - through Sunday `23:59`
   - server timezone must be `Asia/Seoul`
3. Scores items so AI technology stories rank above events, interviews, lawsuits, or generic business news.
4. Selects up to 5 source articles.
5. Uses Claude to rewrite them into a short weekly summary.
6. Saves one markdown post in `src/content/posts/ainews/`.

## Generated files

The script writes:

- `src/content/posts/ainews/YYYY-MM-DD-weekly-ai-news-ko.md`

`YYYY-MM-DD` is the Monday publication date.
If a file for that date already exists, generation is skipped.
If no qualifying article exists inside the exact weekly window, no post is generated.

## GitHub Actions schedule

Workflow file: `.github/workflows/ai-news-weekly.yml`

- Cron: `0 21 * * 0`
- This is Monday `06:00` in Korea Standard Time.

The workflow:

1. installs dependencies
2. runs `npm run ai-news:weekly`
3. builds the Astro site
4. commits only new AI news markdown files

## Required secret

Add this repository secret in GitHub:

- `ANTHROPIC_API_KEY`

## Important operating notes

- The source adapters are intentionally conservative: they prefer feed metadata and links, then rewrite from that material instead of copying article text.
- The keyword scoring is the first filter. Adjust `POSITIVE_KEYWORDS` and `NEGATIVE_KEYWORDS` in `scripts/ai-news-weekly.js` after a few live runs.
- If AI타임스 changes its RSS structure, update the source section in `scripts/ai-news-weekly.js`.
- There is no fallback to older articles. If there is no qualifying article in the exact weekly window, no post is generated.
