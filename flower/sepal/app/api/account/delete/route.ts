import { forwardRequestToBackend } from "@/lib/api-proxy";
import { NextRequest } from "next/server";
import { ApiUrls } from "@/lib/constants";

/**
 * Deletes an account
 *
 * @param req api request
 */
export async function DELETE(req: NextRequest) {
  const url = new URL(req.url);
  const queryString = url.search;

  return forwardRequestToBackend(
    req,
    `${ApiUrls.External.Account.DeleteAccount}${queryString}`,
    {
      method: "DELETE",
    },
  );
}
