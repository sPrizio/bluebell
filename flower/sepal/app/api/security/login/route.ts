import { SessionData, sessionOptions } from "@/lib/auth/session";
import { getIronSession } from "iron-session";
import { NextRequest, NextResponse } from "next/server";
import { ApiUrls } from "@/lib/constants";
import { ApiResponse, User } from "@/types/apiTypes";

/**
 * Get the session details
 *
 * @param req api request
 */
export async function GET(req: NextRequest) {
  const res = NextResponse.next();

  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  if (!session.isLoggedIn) {
    return NextResponse.json({ error: "Not authenticated" }, { status: 401 });
  }

  return NextResponse.json({
    username: session.username,
    isLoggedIn: session.isLoggedIn,
    token: session.token,
    roles: session.roles,
  });
}

/**
 * Logs the session into bluebell
 *
 * @param req api request
 */
export async function POST(req: NextRequest) {
  const body = await req.json();

  const apiRes = await fetch(`${ApiUrls.External.Security.Login}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!apiRes.ok) {
    const error = await apiRes.json();
    return NextResponse.json(
      { error: error.message ?? "Login failed" },
      { status: 401 },
    );
  }

  const userData: ApiResponse<User> = await apiRes.json();

  if (!userData.success || !userData.data) {
    return NextResponse.json(
      { error: userData.message ?? "Login failed" },
      { status: 401 },
    );
  }

  const res = NextResponse.json({ ok: true });
  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  session.username = userData.data.username;
  session.token = userData.data.apiToken;
  session.roles = userData.data.roles.map((role) => role.code);
  session.isLoggedIn = true;

  await session.save();

  return res;
}
