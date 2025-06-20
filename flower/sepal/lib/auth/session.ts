import { SessionOptions } from "iron-session";

export interface SessionData {
  username: string;
  isLoggedIn: boolean;
  token: string;
}

export const defaultSession: SessionData = {
  username: "",
  isLoggedIn: false,
  token: "",
};

export const sessionOptions: SessionOptions = {
  password: "complex_password_at_least_32_characters_long",
  cookieName: "sepal_session",
  cookieOptions: {
    // secure only works in `https` environments
    // if your localhost is not on `https`, then use: `secure: process.env.NODE_ENV === "production"`
    //secure: true,
    secure: process.env.NODE_ENV == "production",
  },
};
