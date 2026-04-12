# Weekly AI News Automation

This project can generate two short `ai-news` posts every week:

- one English digest from `https://www.artificialintelligence-news.com/`
- one Korean digest from `https://www.aitimes.com/`

## How it works

`npm run ai-news:weekly` does the following:

1. Collects articles from each site using RSS.
2. Filters items to the previous full week:
   - Monday `00:00`
   - through Sunday `23:59`
   - server timezone must be `Asia/Seoul`
3. Scores items so AI technology stories rank above events, interviews, lawsuits, or generic business news.
4. Selects up to 5 source articles per site.
5. Uses Claude to rewrite them into a short weekly summary.
6. Saves two markdown posts in `src/content/posts/ainews/`.

## Generated files

The script writes:

- `src/content/posts/ainews/YYYY-MM-DD-weekly-ai-news-en.md`
- `src/content/posts/ainews/YYYY-MM-DD-weekly-ai-news-ko.md`

`YYYY-MM-DD` is the Monday publication date.
If a file for that date and locale already exists, generation is skipped.
If no qualifying article exists inside the exact weekly window for a source, that locale is skipped entirely.

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
- If one of the sites changes its RSS structure, update the source section in `scripts/ai-news-weekly.js`.
- There is no fallback to older articles. If a site has no qualifying article in the exact weekly window, no post is generated for that site.
