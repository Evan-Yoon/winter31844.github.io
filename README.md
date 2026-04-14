# winter31844.github.io

Evan Yoon's personal devlog — built with [Astro](https://astro.build) and deployed to GitHub Pages.

## Project Structure

```
/
├── .github/workflows/       # CI/CD: deploy, auto-blog, ai-news
├── docs/                    # Documentation & style guides
│   ├── style.md             # Claude blog writing style guide
│   ├── AI_NEWS_AUTOMATION.md
│   └── guide-*.md
├── functions/               # Firebase Cloud Functions (visit tracking)
├── pentest/                 # Firebase security rule tests
├── public/                  # Static assets & downloads
├── reference/               # Study reference materials (AWS, etc.)
├── scripts/                 # Content generation scripts
│   ├── ai-news-weekly.js    # Generate AI news posts from RSS via Claude
│   ├── blog-from-git.js     # Generate posts from git commit history
│   ├── blog-from-doc.js     # Generate posts from documents
│   ├── inputs/              # Drop source files here for blog:doc
│   └── lib/                 # Shared utilities (Claude API, file readers)
└── src/
    ├── components/          # Nav, Footer, PostNavigation, RelatedPosts
    ├── content/
    │   └── posts/           # Markdown blog posts (YYYY-MM-DD-slug.md)
    ├── content.config.ts    # Astro content collection schema
    ├── layouts/             # BaseLayout, PostLayout
    ├── lib/firebase/        # Firebase client
    ├── pages/               # Routes: /, /posts, /categories, /ai-news, /admin
    ├── plugins/             # Custom remark plugins
    └── styles/              # CSS split by page concern
```

## Commands

| Command | Action |
| :--- | :--- |
| `npm install` | Install dependencies |
| `npm run dev` | Start dev server at `localhost:4321` |
| `npm run build` | Build production site to `./dist/` |
| `npm run preview` | Preview build locally |
| `npm run ai-news:weekly` | Generate weekly AI news post from RSS feeds |
| `npm run blog:git` | Generate post from git commit history |
| `npm run blog:doc -- --file <path>` | Generate post from a document file |

## Content

Posts live in `src/content/posts/` as Markdown files named `YYYY-MM-DD-slug.md`.

**Categories:** `study` · `project` · `explore` · `ai-news`

Frontmatter schema is defined in `src/content.config.ts`. Use `src/content/_FRONTMATTER_TEMPLATE.md` as a reference when writing posts manually.

## Automation

- **Deploy** — pushes to `main` trigger an Astro build and deploy to GitHub Pages
- **AI News** — weekly workflow calls `scripts/ai-news-weekly.js` to aggregate RSS feeds and generate a post via Claude
- **Auto Blog** — triggers on changes to `docs/style.md` or `input.txt`, generates a post via Claude
