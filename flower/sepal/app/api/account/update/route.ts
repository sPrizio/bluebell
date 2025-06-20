import { forwardRequestToBackend } from "@/lib/api-proxy";
import { NextRequest } from "next/server";
import { ApiUrls } from "@/lib/constants";

/**
 * Updates an existing account
 *
 * @param req api request
 */
export async function PUT(req: NextRequest) {
  const body = await req.json();
  const url = new URL(req.url);
  const queryString = url.search;

  return forwardRequestToBackend(
    req,
    `${ApiUrls.External.Account.UpdateAccount}${queryString}`,
    {
      method: "PUT",
      body: JSON.stringify(body),
    },
  );
}
