import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { SessionData, sessionOptions } from "@/lib/auth/session";
import {
  ApiUrlMappings,
  extractParamsFromPattern,
  matchUrlPattern,
  populateUrl,
} from "@/lib/urlMappings";

async function handler(req: NextRequest) {
  const res = NextResponse.next();
  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  if (!session?.token) {
    return NextResponse.json(
      { success: false, message: "Unauthorized" },
      { status: 401 },
    );
  }

  const internalUrl = req.nextUrl.pathname;
  const internalPathWithQuery = req.nextUrl.search;
  const queryParams = extractParamsFromPattern(internalPathWithQuery);

  const mappedEntry = Object.entries(ApiUrlMappings).find(
    ([internal]) =>
      internal === internalUrl || matchUrlPattern(internalUrl, internal),
  );

  if (!mappedEntry) {
    return new Response("Proxy route not mapped.", { status: 404 });
  }

  const [internalPattern, externalPattern] = mappedEntry;
  const targetUrl =
    queryParams && Object.keys(queryParams).length > 0
      ? populateUrl(externalPattern, queryParams)
      : externalPattern;

  try {
    const backendRes = await fetch(targetUrl, {
      method: req.method,
      headers: {
        ...Object.fromEntries(req.headers),
        Authorization: `Bearer ${session.token}`,
      },
      body: ["GET", "HEAD"].includes(req.method) ? undefined : await req.text(),
    });

    const contentType = backendRes.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
      const json = await backendRes.json();
      return NextResponse.json(json, { status: backendRes.status });
    } else {
      const text = await backendRes.text();
      return new NextResponse(text, {
        status: backendRes.status,
        headers: backendRes.headers,
      });
    }
  } catch (error: any) {
    return NextResponse.json(
      { success: false, message: error.message || "Unknown error" },
      { status: 500 },
    );
  }
}

export async function GET(req: NextRequest) {
  return handler(req);
}

export async function POST(req: NextRequest) {
  return handler(req);
}

export async function PUT(req: NextRequest) {
  return handler(req);
}

export async function DELETE(req: NextRequest) {
  return handler(req);
}

export async function PATCH(req: NextRequest) {
  return handler(req);
}
