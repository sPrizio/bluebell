import { NextRequest } from "next/server";
import { forwardRequestToBackend } from "@/lib/api-proxy";
import { ApiUrls } from "@/lib/constants";

/**
 * Fetch the account's details
 *
 * @param req api request
 */
export async function GET(req: NextRequest) {
  return forwardRequestToBackend(req, ApiUrls.External.Account.GetDetails);
}
