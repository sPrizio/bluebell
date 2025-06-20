import { NextRequest } from "next/server";
import { forwardRequestToBackend } from "@/lib/api-proxy";
import { ApiUrls } from "@/lib/constants";

/**
 * Get the current user
 *
 * @param req api request
 */
export async function GET(req: NextRequest) {
  return forwardRequestToBackend(req, ApiUrls.Internal.Security.Me);
}
