import "dotenv/config";

const API_KEY = process.env.ANTHROPIC_API_KEY;

if (!API_KEY) {
  throw new Error("ANTHROPIC_API_KEY is not set in .env");
}

export async function callClaude({
  prompt,
  maxTokens = 4000,
  temperature = 0.8,
}) {
  const response = await fetch("https://api.anthropic.com/v1/messages", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "x-api-key": API_KEY,
      "anthropic-version": "2023-06-01",
    },
    body: JSON.stringify({
      model: "claude-3-5-sonnet-20241022",
      max_tokens: maxTokens,
      temperature,
      messages: [
        {
          role: "user",
          content: prompt,
        },
      ],
    }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `Claude API request failed: ${response.status} ${errorText}`,
    );
  }

  const data = await response.json();

  if (!data.content || !Array.isArray(data.content) || !data.content[0]?.text) {
    throw new Error("Claude response format is invalid.");
  }

  let text = data.content[0].text.trim();

  text = text
    .replace(/^```markdown\s*/i, "")
    .replace(/^```\s*/i, "")
    .replace(/\s*```$/, "");

  return text;
}
