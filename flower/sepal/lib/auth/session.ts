import { SessionOptions } from "iron-session";

export interface SessionData {
  username: string;
  isLoggedIn: boolean;
  token: string;
  roles: Array<string>;
}

export const defaultSession: SessionData = {
  username: "",
  isLoggedIn: false,
  token: "",
  roles: [],
};

export const sessionOptions: SessionOptions = {
  password: process.env.AUTH_KEY!,
  cookieName: "sepal_session",
  cookieOptions: {
    // secure only works in `https` environments
    // if localhost is not on `https`, use: `secure: process.env.NODE_ENV === "production"`
    //secure: true,
    secure: process.env.NODE_ENV == "production",
  },
};
