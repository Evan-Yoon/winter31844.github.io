// src/content.config.ts
import { defineCollection, z } from "astro:content";
import { glob } from "astro/loaders";

const posts = defineCollection({
  loader: glob({ pattern: "**/*.md", base: "./src/content/posts" }),
  schema: z.object({
    title: z.string(),
    slug: z.string().optional(),
    // z.coerce.date()를 사용하여 다양한 형식의 날짜 텍스트를 안전하게 날짜 객체로 자동 변환합니다.
    date: z.coerce.date(),
    author: z.string().default("Evan Yoon"),
    category: z.enum(["study", "project", "trouble", "tech"]).default("tech"),
    // description 제한을 없애고 선택 사항으로 변경하여 오류를 방지합니다.
    description: z.string().optional().default(""),
    thumbnail: z.string().optional(),
    tags: z.array(z.string()).default([]),
    readTime: z.number().optional(),
    series: z.string().optional(),
    seriesOrder: z.number().optional(),
    featured: z.boolean().default(false),
    draft: z.boolean().default(false),
    toc: z.boolean().default(true),
  }),
});

export const collections = { posts };
