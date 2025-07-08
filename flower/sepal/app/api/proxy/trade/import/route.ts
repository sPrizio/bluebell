export const runtime = "nodejs";

import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { sessionOptions, SessionData } from "@/lib/auth/session";
import {
  ApiUrlMappings,
  extractParamsFromPattern,
  populateUrl,
} from "@/lib/urlMappings";
import { ApiUrls } from "@/lib/constants";

export async function POST(req: NextRequest) {
  const res = NextResponse.next();
  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  if (!session?.token) {
    return NextResponse.json(
      { success: false, message: "Unauthorized" },
      { status: 401 },
    );
  }

  const internalPathWithQuery = req.nextUrl.search;
  const queryParams = extractParamsFromPattern(internalPathWithQuery);
  const mappedEntry = ApiUrlMappings[ApiUrls.Internal.Trade.Import];
  const targetUrl =
    queryParams && Object.keys(queryParams).length > 0
      ? populateUrl(mappedEntry, queryParams)
      : mappedEntry;

  try {
    const headers = new Headers(req.headers);
    headers.delete("host");
    headers.delete("content-length");
    headers.set("authorization", `Bearer ${session.token}`);

    const backendRes = await fetch(targetUrl, {
      method: "POST",
      headers,
      body: req.body,
      ...({ duplex: "half" } as RequestInit),
    });

    const contentType = backendRes.headers.get("content-type") ?? "";
    if (contentType.includes("application/json")) {
      const json = await backendRes.json();
      return NextResponse.json(json, { status: backendRes.status });
    } else {
      const blob = await backendRes.blob();
      return new Response(blob, {
        status: backendRes.status,
        headers: { "content-type": contentType },
      });
    }
  } catch (err: any) {
    console.error(err);
    return NextResponse.json(
      { success: false, message: err.message ?? "Upload failed" },
      { status: 500 },
    );
  }
}
