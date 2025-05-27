import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { ApiUrls } from "@/lib/constants";

export async function middleware(request: NextRequest) {
  try {
    const res = await fetch(ApiUrls.System.HealthCheck, {
      method: "GET",
      cache: "no-store", // prevent caching
    });

    if (!res.ok) {
      return NextResponse.redirect(new URL("/maintenance", request.url));
    }

    return NextResponse.next();
  } catch (error) {
    return NextResponse.redirect(new URL("/maintenance", request.url));
  }
}

export const config = {
  matcher: ["/((?!_next|favicon.ico|maintenance).*)"], // apply to all routes except static and /maintenance
};
