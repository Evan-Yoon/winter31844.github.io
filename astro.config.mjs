// astro.config.mjs
import { defineConfig } from "astro/config";
import mdx from "@astrojs/mdx";
import sitemap from "@astrojs/sitemap";

export default defineConfig({
  // 아이디는 Evan-Yoon, 주소는 대소문자를 구분하지 않지만 정확히 적어줍니다.
  site: "https://Evan-Yoon.github.io",
  // 저장소 이름을 경로로 사용합니다. (앞뒤 슬래시 필수)
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
