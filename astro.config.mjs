// astro.config.mjs
import { defineConfig } from "astro/config";
import mdx from "@astrojs/mdx";
import sitemap from "@astrojs/sitemap";
import { remarkMedia } from "./src/plugins/remark-media.mjs";

export default defineConfig({
  // 아이디는 Evan-Yoon, 주소는 대소문자를 구분하지 않지만 정확히 적어줍니다.
  site: "https://evan-yoon.com",
  base: "/",

  integrations: [mdx(), sitemap()],

  markdown: {
    remarkPlugins: [remarkMedia],
    shikiConfig: {
      theme: "one-dark-pro",
      wrap: true,
    },
  },
});
