import type { APIRoute } from 'astro';
import { getCollection } from 'astro:content';

function getEntrySlug(entry: { id: string; data: { slug?: string } }) {
  return entry.data.slug ?? entry.id.split('/').pop() ?? entry.id;
}

export const GET: APIRoute = async () => {
  const posts = await getCollection('posts');

  const data = posts
    .sort((a, b) => new Date(b.data.date).valueOf() - new Date(a.data.date).valueOf())
    .map(post => ({
      title: post.data.title ?? '',
      slug: getEntrySlug(post),
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
