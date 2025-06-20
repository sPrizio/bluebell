import { SessionData, sessionOptions } from "@/lib/auth/session";
import { getIronSession } from "iron-session";
import { NextRequest, NextResponse } from "next/server";

/**
 * Get the current user
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
  });
}
