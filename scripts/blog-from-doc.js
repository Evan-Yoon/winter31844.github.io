import "dotenv/config";
import path from "path";
import { readDocumentFile } from "./lib/readers.js";
import { generateAndSavePost } from "./lib/post.js";
import { getRequiredArg } from "./lib/utils.js";

const filePath = getRequiredArg(
  "--file",
  'Usage: npm run blog:doc -- --file "C:\\path\\to\\document.docx"',
);

async function main() {
  const documentText = await readDocumentFile(filePath);
  const fileName = path.basename(filePath);

  const payload = `
Document file name:
${fileName}

Extracted document content:
${documentText}
`;

  await generateAndSavePost({
    sourceType: "external_document",
    sourceTitle: fileName,
    sourcePayload: payload,
    extraInstructions: `
Turn this document into a blog post.
Preserve the core ideas, but rewrite for blog readability.
If the source looks like slides, expand bullets into narrative explanation.
If the source looks like a report, reduce stiffness and make it more human.
`,
  });
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
