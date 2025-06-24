import { NextRequest, NextResponse } from "next/server";
import { ApiCredentials, ApiUrls, AUTH_ENABLED } from "@/lib/constants";
import { getIronSession } from "iron-session";
import { SessionData, sessionOptions } from "@/lib/auth/session";
import { ApiResponse, User } from "@/types/apiTypes";
import { UserPrivilege } from "@/lib/enums";

export async function GET(req: NextRequest, options: RequestInit = {}) {
  const res = new NextResponse();
  const session = await getIronSession<SessionData>(req, res, sessionOptions);

  if (!AUTH_ENABLED) {
    const mockSession: SessionData = {
      token: ApiCredentials.TestUserToken,
      username: ApiCredentials.TestUser,
      isLoggedIn: true,
      roles: [
        UserPrivilege.TRADER,
        UserPrivilege.ADMINISTRATOR,
        UserPrivilege.SYSTEM,
      ],
    };

    session.token = mockSession.token;
    session.username = mockSession.username;
    session.isLoggedIn = mockSession.isLoggedIn;
    session.roles = mockSession.roles;

    await session.save();

    return new NextResponse(JSON.stringify(mockSession), {
      status: 200,
      headers: {
        "Content-Type": "application/json",
        ...Object.fromEntries(res.headers),
      },
    });
  }

  if (!session?.token || !session?.username) {
    return NextResponse.json(
      { success: false, message: "Unauthorized: no session" },
      { status: 401 },
    );
  }

  const backendRes = await fetch(
    ApiUrls.External.User.GetUser.replace("{username}", session.username),
    {
      ...options,
      headers: {
        ...(options.headers || {}),
        Authorization: `Bearer ${session.token}`,
        "Content-Type": "application/json",
      },
    },
  );

  if (backendRes.status !== 200) {
    return NextResponse.json(
      { success: false, message: "Unauthorized: backend rejected" },
      { status: 401 },
    );
  }

  const backendData: ApiResponse<User> = await backendRes.json();

  if (
    backendData.data.apiToken !== session.token ||
    backendData.data.username !== session.username
  ) {
    return NextResponse.json(
      { success: false, message: "Unauthorized: token or username mismatch" },
      { status: 401 },
    );
  }

  return NextResponse.json(session, { status: 200 });
}
