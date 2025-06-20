import { forwardRequestToBackend } from "@/lib/api-proxy";
import { NextRequest } from "next/server";
import { ApiUrls } from "@/lib/constants";

/**
 * Create a new account
 *
 * @param req api request
 */
export async function POST(req: NextRequest) {
  const body = await req.json();
  const url = new URL(req.url);
  const queryString = url.search;

  return forwardRequestToBackend(
    req,
    `${ApiUrls.External.Account.CreateAccount}${queryString}`,
    {
      method: "POST",
      body: JSON.stringify(body),
    },
  );
}
