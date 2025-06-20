import { sessionOptions } from "@/lib/auth/session";
import { getIronSession } from "iron-session";
import { NextRequest, NextResponse } from "next/server";

/**
 * Destroys the session, causing a logout
 *
 * @param req api request
 */
export async function POST(req: NextRequest) {
  const res = NextResponse.json({ ok: true });
  const session = await getIronSession(req, res, sessionOptions);

  session.destroy();

  return res;
}
