// astro.config.mjs
import { defineConfig } from "astro/config";
import mdx from "@astrojs/mdx";
import sitemap from "@astrojs/sitemap";

export default defineConfig({
  // 1. 실제 배포될 GitHub Pages 주소를 입력합니다. (아이디: Evan-Yoon)
  site: "https://Evan-Yoon.github.io/winter31844.github.io",

  // 2.
  base: "/winter31844.github.io/",

  integrations: [mdx(), sitemap()],

  // 코드 하이라이팅 설정 (선택 사항)
  markdown: {
    shikiConfig: {
      theme: "one-dark-pro",
      wrap: true,
    },
  },
});
