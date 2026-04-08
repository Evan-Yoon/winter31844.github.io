import type { APIRoute } from 'astro';
import { getCollection } from 'astro:content';

export const GET: APIRoute = async () => {
  const posts = await getCollection('posts');

  const data = posts
    .sort((a, b) => new Date(b.data.date).valueOf() - new Date(a.data.date).valueOf())
    .map(post => ({
      title: post.data.title ?? '',
      slug: post.data.slug ?? post.id,
      description: post.data.description ?? '',
      category: post.data.category ?? '',
      date: post.data.date ? new Date(post.data.date).toLocaleDateString('ko-KR') : '',
      readTime: post.data.readTime ?? null,
      tags: post.data.tags ?? [],
    }));

  return new Response(JSON.stringify(data), {
    headers: { 'Content-Type': 'application/json' },
  });
};
