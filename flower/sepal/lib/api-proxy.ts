import { getIronSession } from "iron-session";
import { sessionOptions, SessionData } from "@/lib/auth/session";
import { NextRequest, NextResponse } from "next/server";
import { baseUrl } from "@/lib/functions/security-functions";

/**
 * Routes request to the backend api
 *
 * @param req api request
 * @param endpoint url
 * @param options options
 */
export async function forwardRequestToBackend(
  req: NextRequest,
  endpoint: string,
  options: RequestInit = {},
) {
  const res = NextResponse.next();
  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  if (!session?.token) {
    return NextResponse.json(
      { success: false, message: "Unauthorized" },
      { status: 401 },
    );
  }

  const backendRes = await fetch(`${baseUrl()}${endpoint}`, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: `Bearer ${session.token}`,
      "Content-Type": "application/json",
    },
  });

  const data = await backendRes.json();
  return NextResponse.json(data, { status: backendRes.status });
}
