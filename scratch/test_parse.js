const fs = require('fs');
const content = fs.readFileSync('c:/dev/project/winter31844.github.io/src/content/posts/test-header.md', 'utf-8');
const headingMatch = content.match(/^## (.*)/m);
console.log('Original line:', headingMatch[1]);

// This is how we might parse it in Astro
const text = headingMatch[1];
const shortMatch = text.match(/(.*)<!--\s*short:\s*(.*)\s*-->/);
if (shortMatch) {
    console.log('Full text:', shortMatch[1].trim());
    console.log('Short text:', shortMatch[2].trim());
} else {
    console.log('No short title found');
}
